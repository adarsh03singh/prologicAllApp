package com.csestateconnect.ui.home.deal_Frags


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentDealUploadDocBinding
import com.csestateconnect.viewmodel.DealsViewModel
import kotlinx.android.synthetic.main.fragment_deal_upload_doc.*
import kotlinx.android.synthetic.main.fragment_deal_upload_doc.view.*
import java.io.ByteArrayOutputStream
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DealUploadDocFragment : Fragment() {

    lateinit var viewModel: DealsViewModel
    internal var REQUEST_CAMERA: Int = 1
    internal var SELECT_FILE: Int = 2
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 3
    internal lateinit var bitmap: Bitmap
    internal var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deal_upload_doc, container, false)
        val binding: FragmentDealUploadDocBinding = FragmentDealUploadDocBinding.bind(view)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(DealsViewModel::class.java)
        binding.dealUploadVm = viewModel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        camera_upload.setOnClickListener {
            selectCameraImage()
        }

        gallery_upload.setOnClickListener {
            selectGalleryImage()
        }

        try {
            val dealChequeImage = arguments?.get("updateDealKyc").toString()

            if (!dealChequeImage.isEmpty()) {
                Glide.with(this).load(dealChequeImage).into(view.upload_icon!!)
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun selectCameraImage() {
        if (checkPermissions() == true) {

            val items = arrayOf<CharSequence>("Camera", "Cancel")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Upload Image")
            builder.setItems(items) { dialog, i ->
                if (items[i] == "Camera") {
                    try {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(
                            intent, REQUEST_CAMERA
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else if (items[i] == "Cancel") {
                    dialog.dismiss()
                }
            }
            builder.show()
        }
    }

    private fun selectGalleryImage() {
        if (checkPermissions() == true) {

            val items = arrayOf<CharSequence>("Gallery", "Cancel")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Upload Image")
            builder.setItems(items) { dialog, i ->
               if (items[i] == "Gallery") {

                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryIntent.type = "image/*"

                    startActivityForResult(galleryIntent, SELECT_FILE)

                } else if (items[i] == "Cancel") {
                    dialog.dismiss()
                }
            }
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                val bundle = data?.extras
                bitmap = bundle!!.get("data") as Bitmap
                upload_icon.setImageBitmap(bitmap)
                upload_icon.setBackground(null)
                val tempUri = getImageUri(context!!, bitmap)
                viewModel.chequeImageSuccessful.value = true
                val idFile = getRealPathFromURI(tempUri!!)
                Log.i("tagcheckFilegetreal", idFile)
                viewModel.cheque_image.value = idFile

                setImgSize()
                upload_submit_button.visibility = View.VISIBLE

            } else if (requestCode == SELECT_FILE) {
                selectedImageUri = data?.data
                upload_icon.setImageURI(selectedImageUri)
                upload_icon.setBackground(null)
                viewModel.chequeImageSuccessful.value = true
                if (selectedImageUri != null) {
                    val idFile = getRealPathFromURI(selectedImageUri!!)
                    Log.i("tagcheckFilegetreal", idFile.toString())
                    viewModel.cheque_image.value = idFile.toString() // this is try

                    setImgSize()
                    upload_submit_button.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setImgSize() {
        upload_icon.setLayoutParams(
            LinearLayoutCompat
                .LayoutParams
                    (
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        )
        upload_icon.setScaleType(ImageView.ScaleType.FIT_XY)
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? =
            getActivity()!!.getContentResolver().query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    private fun checkPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
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
        val listPermissionsNeeded = ArrayList<String>()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

}
