package com.prologicwebsolution.microatm.ui.transaction

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.print.PrintHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.adapter.TransactionDetailsAdapter
import com.prologicwebsolution.microatm.data.transactionData.Data
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.databinding.FragmentTransactionDetailBinding
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.ui.loginUi.LoginViewModel
import com.prologicwebsolution.microatm.util.shooterFragment
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class TransactionDetailFragment : Fragment() {

    lateinit var viewmodel: TransactionViewModel
    private val mid = "PRO000000000001"
    private val tid = "PRO00001"
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    // all dialog element fields showing transaction details
   var statusHeading: String? = null
    var authId: String? = null
    var invoice: String? = null
    var refId: String? = null
    var rrn: String? = null
    var batchNo: String? = null
    var cardNo: String? = null
    var date: String? = null
    var txnAmount: String? = null
    var amountBalance: String? = null
    var remarks: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_transaction_detail, container, false)
        val binding: FragmentTransactionDetailBinding = FragmentTransactionDetailBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(TransactionViewModel::class.java)
        binding.transactionViewModel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.filteredTransactionData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null ) {
                setUpTransdataInRecyclerView(listOf(it))
            }

        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        val startDatetext = view.findViewById<EditText>(R.id.startDate)
        val endDatetext = view.findViewById<EditText>(R.id.endDate)

        startDatetext.setOnClickListener {
            datePicker(startDatetext)
        }
        endDatetext.setOnClickListener {
            datePicker(endDatetext)
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(true,true,"Transaction Details")
    }

    // transaction data set in recyclerView
    fun setUpTransdataInRecyclerView(transactionList: List<GetTransactionEntity>) {
        var trnsItems: List<Data>? = null
        try {
            trnsItems = transactionList.get(0).data
        } catch (e: Exception) {
            e.printStackTrace()
        }
       val transAdapter = TransactionDetailsAdapter(trnsItems){postion ->
           if(!postion.equals(null)){
               trnsItems!!.forEach {

                   statusHeading = it.statuscode
                    rrn = it.rrn
                   invoice = it.invoicenumber
                    batchNo  = it.bankremarks
                    cardNo = it.cardno
                    date = it.date
                    txnAmount = it.txnamount
                    amountBalance = it.amount
                    remarks = it.bankremarks

               }

               transactionDetailDialog(requireView(),"Success","-",invoice,rrn,"-",cardNo,date,txnAmount,amountBalance,
               remarks)
           }

       }
        transactionDetails_recyrView.adapter = transAdapter
        transactionDetails_recyrView.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        transactionDetails_recyrView.setHasFixedSize(true)
    }

    private fun datePicker(dateText: EditText) {
        val calender = Calendar.getInstance()
        val mYear = calender.get(Calendar.YEAR)
        val mMonth = calender.get(Calendar.MONTH)
        val mDay = calender.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
                DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                            view: DatePicker,
                            year: Int,
                            monthOfYear: Int,
                            dayOfMonth: Int
                    ) {
//                    dateText.setText((monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year)
                        dateText.setText(year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth)
                    }
                }, mYear, mMonth, mDay)
        datePickerDialog.getDatePicker().setMaxDate(calender.getTimeInMillis())
        datePickerDialog.show()
    }

    // showing there transaction detail
    @SuppressLint("SetWorldReadable")
    fun transactionDetailDialog(view: View,statusHeading: String?,authId: String?,invoiceNo: String?,
                           rrn: String?,batchNo: String?,cardNo: String?,
                                date: String?,txnAmount: String?,amountBalance: String?,remarks: String? ) {

        val dialog = Dialog(view.context, android.R.style.Theme_Light_NoTitleBar)//Theme_Black_NoTitleBar_Fullscreen
        dialog.setContentView(R.layout.transaction_status_dialog_item)
        dialog.show()

        val tvStatusHeading = dialog.findViewById<TextView>(R.id.tvStatusHeading)
        val tvAuthId = dialog.findViewById<TextView>(R.id.tvAuthId)
        val tvInvoiceNo = dialog.findViewById<TextView>(R.id.tvInvoiceNo)
        val tvTerminalId = dialog.findViewById<TextView>(R.id.tvTerminalId)
        val tvMid = dialog.findViewById<TextView>(R.id.tvMid)
        val tvRefId = dialog.findViewById<TextView>(R.id.tvRefId)
        val tvRRn = dialog.findViewById<TextView>(R.id.tvRRn)
        val tvbatchNo = dialog.findViewById<TextView>(R.id.tvbatchNo)
        val tvcardNo = dialog.findViewById<TextView>(R.id.tvcardNo)
        val tvDate = dialog.findViewById<TextView>(R.id.tvDate)
        val tvTxnAmount = dialog.findViewById<TextView>(R.id.tvTxnAmount)
        val tvAmountBalance = dialog.findViewById<TextView>(R.id.tvAmountBalance)
        val tvRemarks = dialog.findViewById<TextView>(R.id.tvRemarks)

        val linearLayout = dialog.findViewById<LinearLayoutCompat>(R.id.transactionDetailParentLayout)
        val shareButton = dialog.findViewById<TextView>(R.id.shareButton)
        val printButton = dialog.findViewById<TextView>(R.id.printButton)
        try {
            tvStatusHeading.text = statusHeading
            tvAuthId.text = authId
            tvInvoiceNo.text = invoiceNo
            tvTerminalId.text = tid
            tvMid.text = mid
//            tvRefId.text = refId
            tvRRn.text = rrn
            tvbatchNo.text = batchNo
            tvcardNo.text = cardNo
            tvDate.text = date
            tvTxnAmount.text = txnAmount
            tvAmountBalance.text = amountBalance
            tvRemarks.text = remarks

        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }

        // send image of social sites and save in gallery
        shareButton.setOnClickListener {
            checkPermissions()
            if (checkPermissions() == true) {
                val bitmap: Bitmap = getBitmapFromView(linearLayout)
                try {

                    val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png"
                    )
                    // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
                    val bmpUri = FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", file)

//                    val file = File(requireActivity().externalCacheDir, "one.jpg")
                    val fout = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,fout)
                    fout.flush()
                    fout.close()
                    file.setReadable(true,false)
                    //for the saving images in gallery
//                    MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), bitmap, "Prologic welcome page" ,"Prologic")
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    intent.setType("image/png")
                    startActivity(Intent.createChooser(intent,"share by"))

                }catch (e: FileNotFoundException){
                    e.printStackTrace()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }

        printButton.setOnClickListener {
            val photoPrinter = PrintHelper(requireContext())
            photoPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
            val bitmap = getBitmapFromView(linearLayout)
            photoPrinter.printBitmap("droids.jpg - test print", bitmap)
        }

    }

    // create layout to image
    @SuppressLint("ResourceAsColor")
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val drawable: Drawable = requireView().background

        if(drawable != null){
            drawable.draw(canvas)
        }else{
            canvas.drawColor(android.R.color.white)
        }
        view.draw(canvas)

        return bitmap
    }

    // check permission for camera and gallery
    private fun checkPermissions(): Boolean {
        /*   val camera = ContextCompat.checkSelfPermission(
               this,
               Manifest.permission.CAMERA
           )*/

        val telephonyService = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_PHONE_STATE
        )
        val wtite =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val read =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val listPermissionsNeeded = java.util.ArrayList<String>()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        /*   if (camera != PackageManager.PERMISSION_GRANTED) {
               listPermissionsNeeded.add(Manifest.permission.CAMERA)
           }*/
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (telephonyService != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


}