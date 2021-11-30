package com.csestateconnect.ui.navdrawer


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentProfileBinding
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.ByteArrayOutputStream


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var viewModel: ProfileViewModel
    var imageView1: ImageView? = null
    var panImage: ImageView? = null
    var chequeImg: ImageView? = null

    internal var REQUEST_CAMERA: Int = 1
    internal var SELECT_FILE: Int = 2
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 3
    internal lateinit var bitmap: Bitmap
    internal var selectedImageUri: Uri? = null
    var profileImage: ImageView? = null
//    var navController: NavController? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getContext()!!.getTheme().applyStyle(R.style.NoActionBarMaterial, true);
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val binding: FragmentProfileBinding = FragmentProfileBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        binding.viewmodel = viewModel
        binding.mContext = this
        binding.lifecycleOwner = this
//        navController = view.findNavController(view)

        imageView1 = view.findViewById(R.id.verify_idProogImg) as ImageView
        chequeImg = view.findViewById(R.id.cancelCheque_image) as ImageView
        panImage = view.findViewById(R.id.panCard_img) as ImageView
        profileImage = view.findViewById(R.id.profile_imageView) as ImageView

        viewModel.idImageGet.observe(this, Observer {
            if (it == "profileImage")
                selectImage()
        })
//        viewModel.getimageFromDatabase()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            viewModel.infoCount.observe(this, Observer { tag ->
                if (tag == 1) {
                    val bundle = bundleOf("profileCount" to tag)
                    findNavController().navigate(
                        R.id.action_header_view_to_personalInfoFragment,
                        bundle
                    )
                    viewModel.infoCount.value = -1
                }
            })
            viewModel.accountCount.observe(this, Observer { tag2 ->
                if (tag2 == 2) {
                    val bundle = bundleOf("profileCount" to tag2)
                    findNavController().navigate(
                        R.id.action_header_view_to_accountDetailsFragment,
                        bundle
                    )
                    viewModel.accountCount.value = -1
                }
            })
            viewModel.updatePanCount.observe(this, Observer { tag3 ->
                if (tag3 == 3) {
                    val bundle = bundleOf("profileCount" to tag3)
                    findNavController().navigate(
                        R.id.action_header_view_to_accountDetailsFragment,
                        bundle
                    )
                    viewModel.updatePanCount.value = -1
                }
            })
            viewModel.verificationCount.observe(this, Observer { tag4 ->
                if (tag4 == 4) {
                    val bundle = bundleOf("profileCount" to tag4)
                    findNavController().navigate(
                        R.id.action_header_view_to_verificationFragment,
                        bundle
                    )
                    viewModel.verificationCount.value = -1
                }
            })
//        }
        //get Profile Data
        viewModel.getProfile()
        viewModel.getVerifyAndAccountData()
        setImageVerificationAndAccount(view)
    }

    fun setImageVerificationAndAccount(view: View) {
        try {
            Coroutines.main {
                val value = viewModel.imgValueVerify
                val panimgVl = viewModel.panImg
                val chequeimgVl = viewModel.chequeimg
                if(value != null){
                    view.verification_layout.visibility = View.VISIBLE
                    Glide.with(this).load(value).into(imageView1!!)
                }else{
                    view.verification_layout.visibility = View.GONE
                }
                if(panimgVl != null) {
                    view.accout_detail_layout.visibility = View.VISIBLE
                    view.accout_detailChequeImg_layout.visibility = View.VISIBLE
                    view.accout_detailPanImg_layout.visibility = View.VISIBLE
                    Glide.with(this).load(panimgVl).into(panImage!!)
                    Glide.with(this).load(chequeimgVl).into(chequeImg!!)
                }
                else{
                    view.accout_detail_layout.visibility = View.GONE
                    view.accout_detailChequeImg_layout.visibility = View.GONE
                    view.accout_detailPanImg_layout.visibility = View.GONE
                }

            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun selectImage() {
        checkPermissions()
        if (checkPermissions() == true) {

            val items = arrayOf<CharSequence>("Camera", "Gallery", "Remove image", "Cancel")
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Upload Image")
            builder.setItems(items) { dialog, i ->
                if (items[i] == "Camera") {

                    if(isNetworkConnected(context!!)) {
                        try {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(
                                intent, REQUEST_CAMERA
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else
                        Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()

                } else if (items[i] == "Gallery") {

                    if(isNetworkConnected(context!!)) {
                        val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryIntent.type = "image/*"

                        startActivityForResult(galleryIntent, SELECT_FILE)
                    }
                    else
                        Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()

                } else if (items[i] == "Remove image") {

                    if(isNetworkConnected(context!!))
                    {
                        viewModel.deleteImage(view!!)
                    }
                    else
                    {
                        Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
                    }
            }

                else if (items[i] == "Cancel") {
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
                profile_imageView.setImageBitmap(bitmap)
                profile_imageView.setBackground(null)
                selectedImageUri = getImageUri(requireActivity(), bitmap)
                viewModel.profileImageSuccessful.value = true
                val idFile = getRealPathFromURI(selectedImageUri!!)
                viewModel.profile_image.value = idFile

            } else if (requestCode == SELECT_FILE) {
                selectedImageUri = data?.data
                profile_imageView.setImageURI(selectedImageUri)
                profile_imageView.setBackground(null)
                viewModel.profileImageSuccessful.value = true
                if (selectedImageUri != null) {
                    val idFile = getRealPathFromURI(selectedImageUri!!)
                    viewModel.profile_image.value = idFile.toString()

                }


            }
            viewModel.updateImage(view!!)
//            viewModel.imageResponse.observe(this, Observer { value->
//                if(value == true){
//                    Coroutines.main{
//                        val imgval = viewModel.imageValue
//                        if(imgval != null) {
//                            profile_imageView.setBackground(null)
//                            Glide.with(this).load(imgval).into(profile_imageView)
//                        }
//                    }
//
//                }
//            })

        }
    }

    fun setImage(){
        Coroutines.main{
            val imgval = viewModel.imageValue
            if(imgval != null ) {
            viewModel.profileImgeView.value = imgval
            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? =
            requireActivity().getContentResolver().query(contentURI, null, null, null, null)
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
            requireActivity(),
            Manifest.permission.CAMERA
        )
        val wtite =
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val read =
            ContextCompat.checkSelfPermission(
                requireActivity(),
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
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
