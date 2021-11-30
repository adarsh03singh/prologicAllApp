package com.csestateconnect.ui.navdrawer.bcp


import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.database.ContentObservable
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.query
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentAccountDetailsBinding
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.NavigationViewModel
import kotlinx.android.synthetic.main.fragment_account_details.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AccountDetailsFragment : Fragment() {

    internal var REQUEST_CAMERA_CLICK_CHEQUE: Int = 1
    internal var REQUEST_CAMERA_CLICK_PAN: Int = 2
    internal lateinit var bitmap: Bitmap
    internal var selectedImageUri: Uri? = null

    internal var SELECT_FILE_CLICK_CHEQUE: Int = 3
    internal var SELECT_FILE_CLICK_PAN: Int = 4
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 5

    var panImageView: ImageView? = null
    var chequeImageView: ImageView? = null

//    var cameraFilePath:String = ""

    lateinit var viewModel: NavigationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_details, container, false)
        val binding: FragmentAccountDetailsBinding = FragmentAccountDetailsBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(NavigationViewModel::class.java)
        binding.accountViewmodel = viewModel
        binding.lifecycleOwner = this

        panImageView = view.findViewById(R.id.panCard_image) as ImageView
        chequeImageView = view.findViewById(R.id.cheque_image) as ImageView
        viewModel.chequeImageGet.observe(this, Observer{
            if (it == "CHEQUE")
                selectImage(it)
        })

        viewModel.panImageGet.observe(this, Observer {
            if (it == "PAN")
            selectImage(it)
        })
        if (checkPermissions()) {
            setOldImage()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val count = arguments?.getInt("profileCount")
        if (count != null){
            viewModel.profileCountValue.value = count
        }
    }

    private fun selectImage(button_name: String) {
        checkPermissions()
        if (checkPermissions() == true) {

            val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Upload Image")
            builder.setItems(items) { dialog, i ->
                if (items[i] == "Camera") {
                    try {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    if (button_name == "CHEQUE") {
                        startActivityForResult(
                            intent, REQUEST_CAMERA_CLICK_CHEQUE)
                    } else if (button_name == "PAN") {
                       startActivityForResult(intent, REQUEST_CAMERA_CLICK_PAN)
                    }
                    } catch (e: Exception){
                        e.printStackTrace()
                    }

                } else if (items[i] == "Gallery") {

                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryIntent.type = "image/*"

                    if (button_name == "CHEQUE") {

                        startActivityForResult(galleryIntent, SELECT_FILE_CLICK_CHEQUE)

                    } else if (button_name == "PAN") {

                        startActivityForResult(galleryIntent, SELECT_FILE_CLICK_PAN)
                    }

                } else if (items[i] == "Cancel") {
                    dialog.dismiss()
                }
            }
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK || requestCode != 0) {

            if (requestCode == REQUEST_CAMERA_CLICK_CHEQUE) {
                val bundle = data?.extras
                bitmap = bundle!!.get("data") as Bitmap
                cheque_image.setImageBitmap(bitmap)
                cheque_image.setBackground(null)
                val tempUri = getImageUri(context!!,bitmap)
                viewModel.chequeImageSuccessful.value = true
                viewModel.canceled_cheque_image.value = getRealPathFromURI(tempUri!!) // this is try

                setChequeImageSizes()
            } else if (requestCode == REQUEST_CAMERA_CLICK_PAN) {
                val bundle = data?.extras
                bitmap = bundle!!.get("data") as Bitmap
                panCard_image.setImageBitmap(bitmap)
                panCard_image.setBackground(null)
                val tempUri = getImageUri(context!!,bitmap)
                viewModel.panImageSuccessful.value = true
                viewModel.pan_card_image.value = getRealPathFromURI(tempUri!!) // this is try
                setPancardImageSizes()
            } else if (requestCode == SELECT_FILE_CLICK_CHEQUE) {
                selectedImageUri = data?.data
                cheque_image.setImageURI(selectedImageUri)
                cheque_image.setBackground(null)
                viewModel.chequeImageSuccessful.value = true

                if (selectedImageUri!= null) {
                    val chequeFile = getRealPathFromURI(selectedImageUri!!)
                    Log.i("tagcheckFilegetreal", chequeFile.toString())
                    viewModel.canceled_cheque_image.value = chequeFile.toString() // this is try

                    setChequeImageSizes()
                }
            } else if (requestCode == SELECT_FILE_CLICK_PAN) {
                selectedImageUri = data?.data
                panCard_image.setImageURI(selectedImageUri)
                panCard_image.setBackground(null)
                viewModel.panImageSuccessful.value = true

                if (selectedImageUri!= null) {
                    val panFile = getRealPathFromURI(selectedImageUri!!)
                    Log.i("tagpanFilegetreal", panFile.toString())
                    viewModel.pan_card_image.value = panFile.toString() // this is try

                    setPancardImageSizes()
                }
            }

        }
    }

    fun setOldImage() {
        Coroutines.main {
            try {

                val value = viewModel.panImgValue
                val value2 = viewModel.chequeImgValue
                if (value != null && value2 != null) {
                Glide.with(this) .asBitmap().load(value).into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        panImageView!!.setImageBitmap(resource)
                        setPancardImageSizes()
                        val tempUri1 = getImageUri(context!!, resource)
                        viewModel.panImageSuccessful.value = true
                        viewModel.pan_card_image.value = getRealPathFromURI(tempUri1!!)

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

                Glide.with(this).asBitmap().load(value2).into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        chequeImageView!!.setImageBitmap(resource)
                        setChequeImageSizes()
                        val tempUri1 = getImageUri(context!!, resource)
                        viewModel.chequeImageSuccessful.value = true
                        viewModel.canceled_cheque_image.value = getRealPathFromURI(tempUri1!!)

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.i("tag", "Log")
                    }
                })

                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = getActivity()!!.getContentResolver().query(contentURI, null, null, null, null)
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


    private fun setChequeImageSizes(){
        cheque_image.setLayoutParams(
            LinearLayoutCompat.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        cheque_image.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    private fun setPancardImageSizes(){
        panCard_image.setLayoutParams(
            LinearLayoutCompat.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        panCard_image.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private fun checkPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        val wtite =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
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

