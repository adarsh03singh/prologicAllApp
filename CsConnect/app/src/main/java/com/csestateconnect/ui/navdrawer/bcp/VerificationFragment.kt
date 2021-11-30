package com.csestateconnect.ui.navdrawer.bcp


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentVerificationBinding
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.utils.Coroutines.io
import com.csestateconnect.viewmodel.NavigationViewModel
import kotlinx.android.synthetic.main.fragment_verification.*
import java.io.ByteArrayOutputStream
import java.util.*


class VerificationFragment : Fragment() {

    lateinit var viewModel: NavigationViewModel
    internal var REQUEST_CAMERA: Int = 1
    internal var SELECT_FILE: Int = 2
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 3
    internal lateinit var bitmap: Bitmap
    internal var selectedImageUri: Uri? = null
    var imageView: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verification, container, false)
        val binding: FragmentVerificationBinding = FragmentVerificationBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(NavigationViewModel::class.java)
        binding.verifyViewmodel = viewModel
        binding.lifecycleOwner = this

        imageView = view.findViewById(R.id.id_proofImage) as ImageView

        viewModel.tabItem.observe(this, Observer { tab ->
            // No need for 0th and 2nd position
            if (tab == 3) {
                findNavController().navigate(R.id.action_verificationFragment_to_accountDetailsFragment)
                viewModel.tabItem.value = -1
            }
        })

        if (checkPermissions()) {
            setOldImage()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        id_proofImage.setOnClickListener {
            selectImage()
        }

        val count = arguments?.getInt("profileCount")
        if (count != null){
            viewModel.profileCountValue.value = count
        }
    }

    private fun selectImage() {
        checkPermissions()
        if (checkPermissions() == true) {

            val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
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

                } else if (items[i] == "Gallery") {

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
                id_proofImage.setImageBitmap(bitmap)
                id_proofImage.setBackground(null)
                val tempUri = getImageUri(context!!, bitmap)
                viewModel.idProofImageSuccessful.value = true
                val idFile = getRealPathFromURI(tempUri!!)
                Log.i("tagcheckFilegetreal", idFile)
                viewModel.id_proof_image.value = idFile

                setImgSize()

            } else if (requestCode == SELECT_FILE) {
                selectedImageUri = data?.data
                id_proofImage.setImageURI(selectedImageUri)
                id_proofImage.setBackground(null)
                viewModel.idProofImageSuccessful.value = true
                if (selectedImageUri != null) {
                    val idFile = getRealPathFromURI(selectedImageUri!!)
                    Log.i("tagcheckFilegetreal", idFile.toString())
                    viewModel.id_proof_image.value = idFile.toString() // this is try

                    setImgSize()
                }
            }
        }
    }

    fun setOldImage() {
        try {
            Coroutines.main {

                    val value = viewModel.imgValue
                    if (value != null) {
                        Glide.with(this) .asBitmap().load(value).into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                            ) {
                                imageView!!.setImageBitmap(resource)
                                val tempUri1 = getImageUri(context!!, resource)
                                viewModel.idProofImageSuccessful.value = true
                                viewModel.id_proof_image.value = getRealPathFromURI(tempUri1!!)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                    }
                setImgSize()
            }


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun setImgSize() {
        id_proofImage.setLayoutParams(
            LinearLayoutCompat
                .LayoutParams
                    (
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        )
        id_proofImage.setScaleType(ImageView.ScaleType.FIT_XY)
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
