package com.prologicwebsolution.microatm.ui.connectMicroAtm

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.print.PrintHelper
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.databinding.FragmentConnectMicroAtmBinding
import com.prologicwebsolution.microatm.util.Coroutines
import com.prologicwebsolution.microatm.util.CustomDialog
import com.sil.ucubesdk.POJO.UCubeRequest
import com.sil.ucubesdk.UCubeCallBacks
import com.sil.ucubesdk.UCubeManager
import com.sil.ucubesdk.payment.TransactionType
import kotlinx.android.synthetic.main.fragment_connect_micro_atm.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import android.app.ProgressDialog
import android.widget.TextView
import com.prologicwebsolution.microatm.adapter.BlutoothDevicesAdaper
import android.bluetooth.BluetoothSocket
import android.content.IntentFilter
import android.os.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.util.shooterFragment


class ConnectMicroAtmFragment : Fragment(), View.OnClickListener {


    private val TAG: String = ConnectMicroAtmFragment::class.java.getSimpleName()
    private val LICENSEY_KEY = "em1mou2QPtClzkCFV4pWWg1d9Zq5XXqy"
    private val mid = "PRO000000000001"
    private val tid = "PRO00001"
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    var num: Int = 0


    var uCubeManager: UCubeManager? = null
    var uCubeRequest: UCubeRequest? = null
    var balanceEnquiryLayout: androidx.appcompat.widget.LinearLayoutCompat? = null
    var cashWithdrawlLayout: androidx.appcompat.widget.LinearLayoutCompat? = null
    var sellByCardLayout: androidx.appcompat.widget.LinearLayoutCompat? = null
    var responseMessageTv: TextView? = null
    var trasactionId = ""
    var customDialog: CustomDialog? = null
    var bluetoothAdddress: String? = null

    var txnAmount: String? = "0"
    var pairedDeviceAddress: String? = null

    lateinit var viewmodel: ConnectMicroAtmViewModel
    var btArrayAdapter: ArrayAdapter<String>? = null
    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private var bluetoothDevices: ArrayList<BluetoothDevice>? = null
    private val REQUEST_ENABLE_BLUETOOTH = 1


    //    var mBlueAdapter: BluetoothAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connect_micro_atm, container, false)
        val binding: FragmentConnectMicroAtmBinding = FragmentConnectMicroAtmBinding.bind(view)
        viewmodel =
            ViewModelProviders.of(this.requireActivity()).get(ConnectMicroAtmViewModel::class.java)
        binding.connenctMoicroAtmModel = viewmodel
        binding.lifecycleOwner = this

        // use for support file after 24 version of android
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        uCubeManager = UCubeManager.getInstance(view.context, LICENSEY_KEY)
        customDialog = CustomDialog(requireContext())

        cashWithdrawlLayout = view.findViewById(R.id.cashWithdrawlLayout)
        balanceEnquiryLayout = view.findViewById(R.id.balanceEnquiryLayout)
        sellByCardLayout = view.findViewById(R.id.sellBycardLayout)

        cashWithdrawlLayout!!.setOnClickListener(this)
        balanceEnquiryLayout!!.setOnClickListener(this)
        sellByCardLayout!!.setOnClickListener(this)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (m_bluetoothAdapter == null) {
            showToast("this device doesn't support bluetooth")
            return
        } else if (!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            showToast("Kindly enable blutooth")
        }

        blutooth_devices_recycleView.setLayoutManager(LinearLayoutManager(requireContext()))
        blutooth_devices_recycleView.setItemAnimator(DefaultItemAnimator())
        blutooth_devices_recycleView.setNestedScrollingEnabled(false)


        pairedDeviceList()
        scanBlutooth_button.setOnClickListener {
            requireActivity().registerReceiver(receiver,
                IntentFilter(BluetoothDevice.ACTION_FOUND));
            pairedDeviceList()
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(true,true,"Connect Micro ATM")
    }

    @SuppressLint("HardwareIds")
    fun callBalanceInquiry() {

        //Getting imei no of Phone by this code
        var IMEINumber: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            IMEINumber = Settings.Secure.getString(requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } else {
            val mTelephony =
                requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                IMEINumber = mTelephony.deviceId
            } else {
                IMEINumber = Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }

        if (pairedDeviceAddress != null) {
            bluetoothAdddress = pairedDeviceAddress
            uCubeRequest = UCubeRequest()
            uCubeRequest!!.setUsername("8929879302")
            uCubeRequest!!.setPassword("2106")
            uCubeRequest!!.setRefCompany("PROLOGIC")
            uCubeRequest!!.setMid(mid)
            uCubeRequest!!.setTid(tid)
            uCubeRequest!!.setImei(IMEINumber)
            uCubeRequest!!.setImsi("404277270869423")
            uCubeRequest!!.setTxn_amount(txnAmount)
            Log.d(TAG, "bluetoothAdddress: $bluetoothAdddress")
            uCubeRequest!!.setBt_address(bluetoothAdddress)
            uCubeRequest!!.setRequestCode(TransactionType.INQUIRY) //INQUIRY, DEBIT, WITHDRAWAL

        }
        showStatusDialog(true)
        trasactionId = uCubeManager!!.transactionId
        uCubeRequest!!.setTransactionId(trasactionId) //you can have your own 12digit integer Id or create one using UCubeManager;
        uCubeManager!!.execute(uCubeRequest, object : UCubeCallBacks {
            override fun successCallback(jsonObject: JSONObject) {
                hideDialog()
                Log.d(TAG, "successCallback: $jsonObject")
                try {
                    var status: String? = "Success"
                    var responseCode = -1
                    var responseMessage = JSONObject()
                    if (jsonObject.has("Msg")) {
                        status = jsonObject.getString("Msg")
                    }
                    if (jsonObject.has("ResponseCode")) {
                        responseCode = jsonObject.getInt("ResponseCode")
                    }
                    if (jsonObject.has("Response")) {
                        responseMessage = jsonObject.getJSONObject("Response")
                    }

                    try {
                        val StatusHeading = status
                        val InvoiceNo = responseMessage["invoiceNumber"]
                        val authid = responseMessage["authid"]
                        val RRn = responseMessage["rrn"]
                        val batchNo = responseMessage["batchNo"]
                        val cardNo = responseMessage["cardno"]
                        val date = responseMessage["date"]
                        val amountBalance = responseMessage["ed"].toString()

                        val s = "#"
                        val arr = amountBalance.split(s).toTypedArray()
                        val result = arr[0]
                        val remarks = responseMessage["msg"]

                        viewmodel.amount = result
                        viewmodel.invoicenumber = InvoiceNo.toString()
                        viewmodel.rrn = RRn.toString()
                        viewmodel.cardno = cardNo.toString()
                        viewmodel.date = date.toString()
                        viewmodel.bankremarks = remarks.toString()
                        viewmodel.txnAmount = txnAmount
                        viewmodel.txnType = "INQUIRY"
                        viewmodel.refstan = trasactionId
                        viewmodel.statuscode = responseCode.toString()
                        viewmodel.clientrefid = RRn.toString()



                        setMessage(StatusHeading, authid.toString(), InvoiceNo.toString(),
                            RRn.toString(), batchNo.toString(), cardNo.toString(),
                            date.toString(), txnAmount, result, remarks.toString())

                        callSaveTransactionApi()

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
//                    setMessage(status, responseCode, responseMessage.toString())
                } catch (jsonexception: JSONException) {
                    jsonexception.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun progressCallback(message: String) {
                Log.d(TAG, "progressCallback: $message")
                updateTransactionMessage(message)
            }

            override fun failureCallback(jsonObject: JSONObject) {
                hideDialog()
                Log.d(TAG, "failureCallback: $jsonObject")
                try {
                    var status: String? = "Success"
                    var responseCode = -1
                    if (jsonObject.has("Msg")) {
                        status = jsonObject.getString("Msg")
                    }
                    if (jsonObject.has("ResponseCode")) {
                        responseCode = jsonObject.getInt("ResponseCode")
                    }
                    if (responseCode == 100) {
                        try {
                            var responseMessage = JSONObject()
                            if (jsonObject.has("Response")) {
                                responseMessage = jsonObject.getJSONObject("Response")
                                if (status.equals("Failed")) {
                                    Toast.makeText(requireContext(),
                                        responseMessage.toString(),
                                        Toast.LENGTH_LONG).show()
                                }
                                val StatusHeading = status
                                val InvoiceNo = responseMessage["invoiceNumber"]
                                val authid = responseMessage["authid"]
                                val RRn = responseMessage["rrn"]
                                val batchNo = responseMessage["batchNo"]
                                val cardNo = responseMessage["cardno"]
                                val date = responseMessage["date"]
                                val amountBalance = responseMessage["ed"].toString()
                                val s = "#"
                                val arr = amountBalance.split(s).toTypedArray()
                                val result = arr[0]
                                val remarks = responseMessage["msg"]

                                viewmodel.amount = result
                                viewmodel.invoicenumber = InvoiceNo.toString()
                                viewmodel.rrn = RRn.toString()
                                viewmodel.cardno = cardNo.toString()
                                viewmodel.date = date.toString()
                                viewmodel.bankremarks = remarks.toString()
                                viewmodel.txnAmount = txnAmount
                                viewmodel.txnType = "INQUIRY"
                                viewmodel.refstan = trasactionId
                                viewmodel.statuscode = responseCode.toString()


                                setMessage(StatusHeading, authid.toString(), InvoiceNo.toString(),
                                    RRn.toString(), batchNo.toString(), cardNo.toString(),
                                    date.toString(), txnAmount, result, remarks.toString())

                                callSaveTransactionApi()
//                                setMessage(status, responseCode, responseMessage.toString())
                            }
                        } catch (jsonexception: JSONException) {
                            var responseMessage: String? = ""
                            if (jsonObject.has("Response")) {
                                responseMessage = jsonObject.getString("Response")
                            }
                            exceptionMessagedialog(status, responseMessage)
//                            setMessageError(status, responseCode, responseMessage)
                            jsonexception.printStackTrace()
                        }
                    } else {
                        var responseMessage: String? = ""
                        if (jsonObject.has("Response")) {
                            responseMessage = jsonObject.getString("Response")
                        }
                        exceptionMessagedialog(status, responseMessage)
//                        setMessage(status, responseCode, responseMessage)
                    }
                } catch (jsonexception: JSONException) {
                    jsonexception.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    @SuppressLint("HardwareIds")
    fun callWS(txnvalue: String) {

        var IMEINumber: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            IMEINumber = Settings.Secure.getString(requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } else {
            val mTelephony =
                requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                IMEINumber = mTelephony.deviceId
            } else {
                IMEINumber = Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }

        if (pairedDeviceAddress != null) {
            bluetoothAdddress = pairedDeviceAddress
            uCubeRequest = UCubeRequest()
            uCubeRequest!!.setUsername("8929879302")
            uCubeRequest!!.setPassword("2106")
            uCubeRequest!!.setRefCompany("PROLOGIC")
            uCubeRequest!!.setMid("PRO000000000001")
            uCubeRequest!!.setTid("PRO00001")
            uCubeRequest!!.setImei(IMEINumber)
//            trasactionId = uCubeManager!!.transactionId
            uCubeRequest!!.setTransactionId(trasactionId)
            uCubeRequest!!.setImsi("404277270869423")
            uCubeRequest!!.setTxn_amount(txnvalue)
            Log.d(TAG, "bluetoothAdddress: $bluetoothAdddress")
            uCubeRequest!!.setBt_address(bluetoothAdddress)
            uCubeRequest!!.setRequestCode(TransactionType.WITHDRAWAL) //INQUIRY, DEBIT, WITHDRAWAL

        }

        showStatusDialog(true)
        trasactionId = uCubeManager!!.transactionId
        uCubeRequest!!.setTransactionId(trasactionId) //you can have your own 12digit integer Id or create one using UCubeManager;

        uCubeManager!!.execute(uCubeRequest, object : UCubeCallBacks {
            override fun successCallback(jsonObject: JSONObject) {
                hideDialog()
                Log.d(TAG, "successCallback: $jsonObject")
                try {
                    var status: String? = "Success"
                    var responseCode = -1
                    var responseMessage = JSONObject()
                    if (jsonObject.has("Msg")) {
                        status = jsonObject.getString("Msg")
                    }
                    if (jsonObject.has("ResponseCode")) {
                        responseCode = jsonObject.getInt("ResponseCode")
                    }
                    if (jsonObject.has("Response")) {
                        responseMessage = jsonObject.getJSONObject("Response")
                    }
                    val StatusHeading = status
                    val InvoiceNo = responseMessage["invoiceNumber"]
                    val authid = responseMessage["authid"]
                    val RRn = responseMessage["rrn"]
                    val batchNo = responseMessage["batchNo"]
                    val cardNo = responseMessage["cardno"]
                    val date = responseMessage["date"]
                    val amountBalance = responseMessage["msg"].toString()
                    val s = "#"
                    val arr = amountBalance.split(s).toTypedArray()
                    val result = arr[0]
                    val remarks = responseMessage["msg"]

                    viewmodel.amount = result
                    viewmodel.invoicenumber = InvoiceNo.toString()
                    viewmodel.rrn = RRn.toString()
                    viewmodel.cardno = cardNo.toString()
                    viewmodel.date = date.toString()
                    viewmodel.bankremarks = remarks.toString()
                    viewmodel.txnAmount = txnAmount
                    viewmodel.txnType = "WITHDRAWL"
                    viewmodel.refstan = trasactionId
                    viewmodel.statuscode = responseCode.toString()
                    viewmodel.clientrefid = RRn.toString()

                    setMessage(StatusHeading, authid.toString(), InvoiceNo.toString(),
                        RRn.toString(), batchNo.toString(), cardNo.toString(),
                        date.toString(), txnAmount, result, remarks.toString())

                    callSaveTransactionApi()
//                    setMessage(status, responseCode, responseMessage.toString())
                } catch (jsonexception: JSONException) {
                    jsonexception.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun progressCallback(message: String) {
                Log.d(TAG, "progressCallback: $message")
                updateTransactionMessage(message)
            }

            override fun failureCallback(jsonObject: JSONObject) {
                hideDialog()
                Log.d(TAG, "failureCallback: $jsonObject")
                try {
                    var status: String? = "Success"
                    var responseCode = -1
                    if (jsonObject.has("Msg")) {
                        status = jsonObject.getString("Msg")
                    }
                    if (jsonObject.has("ResponseCode")) {
                        responseCode = jsonObject.getInt("ResponseCode")
                    }
                    if (responseCode == 100) {
                        try {
                            var responseMessage = JSONObject()
                            if (jsonObject.has("Response")) {
                                responseMessage = jsonObject.getJSONObject("Response")

                                if (status.equals("Failed")) {
                                    Toast.makeText(requireContext(),
                                        responseMessage.toString(),
                                        Toast.LENGTH_LONG).show()
                                }
                                val StatusHeading = status
                                val InvoiceNo = responseMessage["invoiceNumber"]
                                val authid = responseMessage["authid"]
                                val RRn = responseMessage["rrn"]
                                val batchNo = responseMessage["batchNo"]
                                val cardNo = responseMessage["cardno"]
                                val date = responseMessage["date"]
                                val amountBalance = responseMessage["msg"].toString()
                                val s = "#"
                                val arr = amountBalance.split(s).toTypedArray()
                                val result = arr[0]
                                val remarks = responseMessage["msg"]

                                viewmodel.amount = result
                                viewmodel.invoicenumber = InvoiceNo.toString()
                                viewmodel.rrn = RRn.toString()
                                viewmodel.cardno = cardNo.toString()
                                viewmodel.date = date.toString()
                                viewmodel.bankremarks = remarks.toString()
                                viewmodel.txnAmount = txnAmount
                                viewmodel.txnType = "WITHDRAWL"
                                viewmodel.refstan = trasactionId
                                viewmodel.statuscode = responseCode.toString()
                                viewmodel.clientrefid = RRn.toString()

                                setMessage(StatusHeading, authid.toString(), InvoiceNo.toString(),
                                    RRn.toString(), batchNo.toString(), cardNo.toString(),
                                    date.toString(), txnAmount, result, remarks.toString())

                                callSaveTransactionApi()
//                                setMessage(status, responseCode, responseMessage.toString())
                            }
                        } catch (jsonexception: JSONException) {
                            var responseMessage: String? = ""
                            if (jsonObject.has("Response")) {
                                responseMessage = jsonObject.getString("Response")
                            }
                            exceptionMessagedialog(status, responseMessage)
//                            setMessage(status, responseCode, responseMessage)
                            jsonexception.printStackTrace()
                        }
                    } else {
                        var responseMessage: String? = ""
                        if (jsonObject.has("Response")) {
                            responseMessage = jsonObject.getString("Response")
                        }
                        exceptionMessagedialog(status, responseMessage)
//                        setMessage(status, responseCode, responseMessage)
                    }
                } catch (jsonexception: JSONException) {
                    jsonexception.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    @SuppressLint("HardwareIds")
    fun callSBCMethod(txnvalue: String) {

        var IMEINumber: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            IMEINumber = Settings.Secure.getString(requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } else {
            val mTelephony =
                requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                IMEINumber = mTelephony.deviceId
            } else {
                IMEINumber = Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }

        if (pairedDeviceAddress != null) {
            bluetoothAdddress = pairedDeviceAddress
            uCubeRequest = UCubeRequest()
            uCubeRequest!!.setUsername("9205041785")
            uCubeRequest!!.setPassword("2106")
            uCubeRequest!!.setRefCompany("PROLOGIC")
            uCubeRequest!!.setMid("441072227302905")
            uCubeRequest!!.setTid("41716955")
            uCubeRequest!!.setImei(IMEINumber)
//            trasactionId = uCubeManager!!.transactionId
            uCubeRequest!!.setTransactionId(trasactionId)
            uCubeRequest!!.setImsi("404277270869423")
            uCubeRequest!!.setTxn_amount(txnvalue)
            Log.d(TAG, "bluetoothAdddress: $bluetoothAdddress")
            uCubeRequest!!.setBt_address(bluetoothAdddress)
            //for Sell by card using TransactionType.DEBIT
            uCubeRequest!!.setRequestCode(TransactionType.DEBIT) //INQUIRY, DEBIT, WITHDRAWAL

        }

        showStatusDialog(true)
        trasactionId = uCubeManager!!.transactionId
        uCubeRequest!!.setTransactionId(trasactionId) //you can have your own 12digit integer Id or create one using UCubeManager;

        uCubeManager!!.execute(uCubeRequest, object : UCubeCallBacks {
            override fun successCallback(jsonObject: JSONObject) {
                hideDialog()
                Log.d(TAG, "successCallback: $jsonObject")
                try {
                    var status: String? = "Success"
                    var responseCode = -1
                    var responseMessage = JSONObject()
                    if (jsonObject.has("Msg")) {
                        status = jsonObject.getString("Msg")
                    }
                    if (jsonObject.has("ResponseCode")) {
                        responseCode = jsonObject.getInt("ResponseCode")
                    }
                    if (jsonObject.has("Response")) {
                        responseMessage = jsonObject.getJSONObject("Response")
                    }
                    val StatusHeading = status
                    val InvoiceNo = responseMessage["invoiceNumber"]
                    val authid = responseMessage["authid"]
                    val RRn = responseMessage["rrn"]
                    val batchNo = responseMessage["batchNo"]
                    val cardNo = responseMessage["cardno"]
                    val date = responseMessage["date"]
                    val amountBalance = responseMessage["msg"].toString()
                    val s = "#"
                    val arr = amountBalance.split(s).toTypedArray()
                    val result = arr[0]
                    val remarks = responseMessage["msg"]

                    viewmodel.amount = result
                    viewmodel.invoicenumber = InvoiceNo.toString()
                    viewmodel.rrn = RRn.toString()
                    viewmodel.cardno = cardNo.toString()
                    viewmodel.date = date.toString()
                    viewmodel.bankremarks = remarks.toString()
                    viewmodel.txnAmount = txnvalue
                    viewmodel.txnType = "DEBIT"
                    viewmodel.refstan = trasactionId
                    viewmodel.statuscode = responseCode.toString()
                    viewmodel.clientrefid = RRn.toString()

                    setMessage(StatusHeading, authid.toString(), InvoiceNo.toString(),
                        RRn.toString(), batchNo.toString(), cardNo.toString(),
                        date.toString(), txnvalue, result, remarks.toString())

                    callSaveTransactionApi()
//                    setMessage(status, responseCode, responseMessage.toString())
                } catch (jsonexception: JSONException) {
                    jsonexception.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun progressCallback(message: String) {
                Log.d(TAG, "progressCallback: $message")
                updateTransactionMessage(message)
            }

            override fun failureCallback(jsonObject: JSONObject) {
                hideDialog()
                Log.d(TAG, "failureCallback: $jsonObject")
                try {
                    var status: String? = "Success"
                    var responseCode = -1
                    if (jsonObject.has("Msg")) {
                        status = jsonObject.getString("Msg")
                    }
                    if (jsonObject.has("ResponseCode")) {
                        responseCode = jsonObject.getInt("ResponseCode")
                    }
                    if (responseCode == 100) {
                        try {
                            var responseMessage = JSONObject()
                            if (jsonObject.has("Response")) {
                                responseMessage = jsonObject.getJSONObject("Response")

                                if (status.equals("Failed")) {
                                    Toast.makeText(requireContext(),
                                        responseMessage.toString(),
                                        Toast.LENGTH_LONG).show()
                                }
                                val StatusHeading = status
                                val InvoiceNo = responseMessage["invoiceNumber"]
                                val authid = responseMessage["authid"]
                                val RRn = responseMessage["rrn"]
                                val batchNo = responseMessage["batchNo"]
                                val cardNo = responseMessage["cardno"]
                                val date = responseMessage["date"]
                                val amountBalance = responseMessage["msg"].toString()
                                val s = "#"
                                val arr = amountBalance.split(s).toTypedArray()
                                val result = arr[0]
                                val remarks = responseMessage["msg"]

                                viewmodel.amount = result
                                viewmodel.invoicenumber = InvoiceNo.toString()
                                viewmodel.rrn = RRn.toString()
                                viewmodel.cardno = cardNo.toString()
                                viewmodel.date = date.toString()
                                viewmodel.bankremarks = remarks.toString()
                                viewmodel.txnAmount = txnvalue
                                viewmodel.txnType = "WITHDRAWL"
                                viewmodel.refstan = trasactionId
                                viewmodel.statuscode = responseCode.toString()
                                viewmodel.clientrefid = RRn.toString()

                                setMessage(StatusHeading, authid.toString(), InvoiceNo.toString(),
                                    RRn.toString(), batchNo.toString(), cardNo.toString(),
                                    date.toString(), txnvalue, result, remarks.toString())

                                callSaveTransactionApi()
//                                setMessage(status, responseCode, responseMessage.toString())
                            }
                        } catch (jsonexception: JSONException) {
                            var responseMessage: String? = ""
                            if (jsonObject.has("Response")) {
                                responseMessage = jsonObject.getString("Response")
                            }
                            exceptionMessagedialog(status, responseMessage)
//                            setMessage(status, responseCode, responseMessage)
                            jsonexception.printStackTrace()
                        }
                    } else {
                        var responseMessage: String? = ""
                        if (jsonObject.has("Response")) {
                            responseMessage = jsonObject.getString("Response")
                        }
                        exceptionMessagedialog(status, responseMessage)
//                        setMessage(status, responseCode, responseMessage)
                    }
                } catch (jsonexception: JSONException) {
                    jsonexception.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }


    private fun hideDialog() {
        requireActivity().runOnUiThread(Runnable { showStatusDialog(false) })
    }

    private fun updateTransactionMessage(message: String) {
        requireActivity().runOnUiThread(Runnable {
            if (customDialog != null && customDialog!!.isShowing) {
                customDialog!!.setMessage(message)
            }
        })
    }

    private fun showStatusDialog(show: Boolean) {
        if (customDialog != null && customDialog!!.isShowing && !show) {
            customDialog!!.dismiss()
            cashWithdrawlLayout!!.isEnabled = true
            balanceEnquiryLayout!!.isEnabled = true
            sellByCardLayout!!.isEnabled = true
        } else {
            customDialog = CustomDialog(requireContext())
            customDialog!!.show()
            cashWithdrawlLayout!!.isEnabled = false
            balanceEnquiryLayout!!.isEnabled = false
            sellByCardLayout!!.isEnabled = false
        }
    }


    fun openWithdrawlAmountDialog() {

//        val amountFielddata: Int? = null
        val mDialogView =
            LayoutInflater.from(activity)
                .inflate(R.layout.cash_withdrawl_dialog_item, null)

        val closeButton = mDialogView.findViewById<ImageView>(R.id.imageCloseDialog)
        val okButton = mDialogView.findViewById<TextView>(R.id.tvOk)
        val etAmount = mDialogView.findViewById<EditText>(R.id.etAmount)

        val mBuilder = android.app.AlertDialog.Builder(activity).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        closeButton?.setOnClickListener {
            mAlertDialog.dismiss()
        }
        txnAmount = etAmount.text.toString()
        okButton?.setOnClickListener {
            if (etAmount.text.toString().isEmpty()) {
                Toast.makeText(
                    activity,
                    "Please enter amount", Toast.LENGTH_SHORT
                ).show()
            }

//           else if (txnAmount!! < checkAmount) {
//           Toast.makeText(activity, "Amount should be more than or equal to 1000.0", Toast.LENGTH_SHORT).show()
//       }
            else {

                if (num.equals(0)) {

                    callWS(etAmount.text.toString())
                    mAlertDialog.dismiss()
                } else if (num.equals(1)) {
                    callSBCMethod(etAmount.text.toString())
                    mAlertDialog.dismiss()
                }
            }
        }

    }

    override fun onClick(v: View?) {
        val id = v!!.id
        if (id == R.id.cashWithdrawlLayout) {

            callWithdrawlMethod(v)

        } else if (id == R.id.balanceEnquiryLayout) {
            callBalanceEnuiryMethod()

        } else if (id == R.id.sellBycardLayout) {
            callSellByCardMethod(v)

        }

    }

    fun callWithdrawlMethod(view: View) {
        connectmicroAtmProgressBar.visibility = View.VISIBLE
        cashWithdrawlLayout!!.isEnabled = false
        Coroutines.main {
            checkPhoneStatePermissions()
            if (checkPhoneStatePermissions() == true) {
                if (!pairedDeviceAddress.equals(null)) {
                    if (uCubeManager!!.isBluetoothConnected(pairedDeviceAddress)) {
                        openWithdrawlAmountDialog()
                        connectmicroAtmProgressBar.visibility = View.VISIBLE
                        cashWithdrawlLayout!!.isEnabled = true
                    } else {
                        showToast("Please turn on MicroAtm & blutooth, try again")
                        connectmicroAtmProgressBar!!.visibility = View.GONE
                        cashWithdrawlLayout!!.isEnabled = true
                    }

                } else {
                    connectmicroAtmProgressBar.visibility = View.GONE
                    cashWithdrawlLayout!!.isEnabled = true
                    showToast("Please select blutooth device")
                }


            } else {
                connectmicroAtmProgressBar.visibility = View.GONE
                cashWithdrawlLayout!!.isEnabled = true
                showToast("Please enable the permission")
            }


        }
    }

    fun callBalanceEnuiryMethod() {

        connectmicroAtmProgressBar.visibility = View.VISIBLE
        balanceEnquiryLayout!!.isEnabled = false
        Coroutines.main {
            checkPhoneStatePermissions()
            if (checkPhoneStatePermissions() == true) {
                if (pairedDeviceAddress != null) {
                    if (uCubeManager!!.isBluetoothConnected(pairedDeviceAddress)) {

                        callBalanceInquiry()
                        connectmicroAtmProgressBar!!.visibility = View.GONE
                        balanceEnquiryLayout!!.isEnabled = true
                    } else {
                        showToast("Please turn on MicroAtm & blutooth, try again")
                        connectmicroAtmProgressBar.visibility = View.GONE
                        balanceEnquiryLayout!!.isEnabled = true
                    }
                } else {
                    connectmicroAtmProgressBar!!.visibility = View.GONE
                    showToast("Please select blutooth device")
                    balanceEnquiryLayout!!.isEnabled = true
                }
            } else {
                showToast("Please enable the permission")
                connectmicroAtmProgressBar!!.visibility = View.GONE
                balanceEnquiryLayout!!.isEnabled = true
            }
        }

    }

    fun callSellByCardMethod(view: View) {
        connectmicroAtmProgressBar.visibility = View.VISIBLE
        sellByCardLayout!!.isEnabled = false
        Coroutines.main {
            checkPhoneStatePermissions()
            if (checkPhoneStatePermissions() == true) {
                if (!pairedDeviceAddress.equals(null)) {
                    if (uCubeManager!!.isBluetoothConnected(pairedDeviceAddress)) {
                        num = 1
                        openWithdrawlAmountDialog()
                        connectmicroAtmProgressBar.visibility = View.VISIBLE
                        sellByCardLayout!!.isEnabled = true
                    } else {
                        showToast("Please turn on MicroAtm & blutooth, try again")
                        connectmicroAtmProgressBar!!.visibility = View.GONE
                        sellByCardLayout!!.isEnabled = true
                    }

                } else {
                    connectmicroAtmProgressBar.visibility = View.GONE
                    sellByCardLayout!!.isEnabled = true
                    showToast("Please select blutooth device")
                }


            } else {
                connectmicroAtmProgressBar.visibility = View.GONE
                sellByCardLayout!!.isEnabled = true
                showToast("Please enable the permission")
            }


        }
    }


  private fun pairedDeviceList() {
      blutoothScanProgressbar.isVisible = true
      Handler().postDelayed({
          blutoothScanProgressbar.isVisible = false

      }, 2000)

        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices

        val list: ArrayList<BluetoothDevice> = ArrayList()

        if (!m_pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)
            }
        } else {
            showToast("no paired bluetooth devices found")
        }

        blutooth_devices_recycleView.adapter = BlutoothDevicesAdaper(requireView(),list){
            val device: BluetoothDevice = list[it]
            callConfirmationDialog(device)
        }

    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
                if (resultCode == Activity.RESULT_OK) {
                    if (m_bluetoothAdapter!!.isEnabled) {
                        showToast("Bluetooth has been enabled")
                    } else {
                        showToast("Bluetooth has been disabled")
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    showToast("Bluetooth enabling has been canceled")
                }
            }
        }


    private fun callConfirmationDialog(bluetoothDevice: BluetoothDevice) {
        val localBuilder = AlertDialog.Builder(activity)
        localBuilder.setTitle("Select Device")
        localBuilder.setMessage("Are you sure to select " + bluetoothDevice.name + " ?")
        localBuilder.setCancelable(false)
        localBuilder.setPositiveButton(
            "YES"
        ) { dialog, which ->

            // add address of paired device
            val address: String = bluetoothDevice.address
            pairedDeviceAddress = address

        }
        localBuilder.setNegativeButton(
            "NO"
        ) { dialog, which -> }
        localBuilder.show()
    }


    fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device!!.name
                    val deviceHardwareAddress = device.address // MAC address
                }
            }
        }
    }

    //set transaction messsage
    @SuppressLint("SetTextI18n")
    private fun setMessage(
        statusHeading: String?, authId: String?, tvInvoiceNo: String?,
        tvRRn: String?, tvbatchNo: String?, tvcardNo: String?,
        tvDate: String?, tvTxnAmount: String?, tvAmountBalance: String?, tvRemarks: String?
    ) {

        Coroutines.main {
            transactionDetailDialog(
                requireView(), statusHeading, authId, tvInvoiceNo,
                tvRRn, tvbatchNo, tvcardNo,
                tvDate, tvTxnAmount, tvAmountBalance, tvRemarks)
        }
    }

    // using for save transaction detail on server
    private fun callSaveTransactionApi() {

        Coroutines.main {
            viewmodel.SavetransactionApiCall(requireView())
        }
    }

    // showing there transactionError
    fun exceptionMessagedialog(status: String?, errormsg: String?) {
        Coroutines.main {
            val dialog = Dialog(
                requireContext(),
                android.R.style.Theme_Light_NoTitleBar
            )//Theme_Black_NoTitleBar_Fullscreen
            dialog.setContentView(R.layout.transaction_error_status_dialog_item)
            dialog.show()
            val tvErrorStatus = dialog.findViewById<TextView>(R.id.tvErrorStatus)
            val tvErrorMsg = dialog.findViewById<TextView>(R.id.tvErrorMsg)

            tvErrorStatus.text = status
            tvErrorMsg.text = errormsg
        }
    }

    // showing there transaction detail
    @SuppressLint("SetWorldReadable")
    fun transactionDetailDialog(
        view: View, statusHeading: String?, authId: String?, invoiceNo: String?,
        rrn: String?, batchNo: String?, cardNo: String?,
        date: String?, txnAmount: String?, amountBalance: String?, remarks: String?
    ) {

        val dialog = Dialog(view.context,
            android.R.style.Theme_Light_NoTitleBar)//Theme_Black_NoTitleBar_Fullscreen
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

        val linearLayout =
            dialog.findViewById<LinearLayoutCompat>(R.id.transactionDetailParentLayout)
        val shareButton = dialog.findViewById<TextView>(R.id.shareButton)
        val printButton = dialog.findViewById<TextView>(R.id.printButton)
        try {
            if (num.equals(0)) {
                tvTerminalId.text = tid
                tvMid.text = mid
            } else if (num.equals(1)) {
                tvTerminalId.text = "41716955"
                tvMid.text = "441072227302905"
            }
            tvStatusHeading.text = statusHeading
            tvAuthId.text = authId
            tvInvoiceNo.text = invoiceNo
//           tvRefId.text = refId
            tvRRn.text = rrn
            tvbatchNo.text = batchNo
            tvcardNo.text = cardNo
            tvDate.text = date
            tvTxnAmount.text = txnAmount
            tvAmountBalance.text = amountBalance
            tvRemarks.text = remarks

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        // send image of social sites and save in gallery
        shareButton.setOnClickListener {
            checkPermissions()
            if (checkPermissions() == true) {
                val bitmap: Bitmap = getBitmapFromView(linearLayout)
                try {

                    val file =
                        File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "share_image_" + System.currentTimeMillis() + ".png"
                        )
                    // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
                    val bmpUri = FileProvider.getUriForFile(requireContext(),
                        "com.codepath.fileprovider",
                        file)

//                    val file = File(requireContext().externalCacheDir, ".jpg")
                    val fout = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)
                    fout.flush()
                    fout.close()
                    file.setReadable(true, false)
                    //for the saving images in gallery
                    MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(),
                        bitmap,
                        "Prologic welcome page",
                        "Prologic")
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    intent.setType("image/png")
                    startActivity(Intent.createChooser(intent, "share by"))

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
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
    fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
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

    private fun checkPhoneStatePermissions(): Boolean {

        val telephonyService = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_PHONE_STATE
        )

        val listPermissionsNeeded = java.util.ArrayList<String>()

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


