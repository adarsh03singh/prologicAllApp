package com.prologic.strains.view.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.BuildConfig
import com.prologic.strains.R
import com.prologic.strains.databinding.RegisterFragBinding
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.LoginActivity
import com.prologic.strains.viewmodel.RegisterViewModel
import com.prologicwebsolution.gsk.utils.BitmapUtil
import com.prologicwebsolution.gsk.utils.BitmapUtil.getBitmapToFilePath
import kotlinx.android.synthetic.main.register_frag.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class RegisterFrag : Fragment() {
    lateinit var viewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val binding: RegisterFragBinding =
            DataBindingUtil.inflate(inflater, R.layout.register_frag, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.isLoaderVisible.observeForever {
            if (it)
                DialogLoading.show(requireContext())
            else
                DialogLoading.hide()
        }
        viewModel.errorMessage.observeForever {
            if (it.isNotEmpty())
                showToastShort(it)
        }
        password_show.setOnClickListener {
            if (viewModel.password_show.value.equals("Show")) {
                viewModel.password_show.value = "Hide"
            } else
                viewModel.password_show.value = "Show"
        }

        viewModel.password_show.observeForever {
            showHidePassword(it, input_password)
            showHidePassword(it, input_confirm_password)
        }
        back.setOnClickListener {
            (requireActivity() as LoginActivity).onBackPressed()
        }
        register.setOnClickListener {
            registerCheck()
        }
        uploadDriversLicense.setOnClickListener {
            viewModel.clickType = 1
            selectImage(requireContext())
        }
        uploadDoctorRec.setOnClickListener {
            viewModel.clickType = 2
            selectImage(requireContext())
        }
        setHeader()
    }

    fun showHidePassword(it: String, editText: EditText) {
        if (it.equals("Hide", true)) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.length())
        editText.typeface = ResourcesCompat.getFont(requireContext(), R.font.medium)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
    }

    private fun selectImage(context: Context) {
        val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView =
            mInflater.inflate(R.layout.photo_picker_dialog, null)
        val cancel = dialogView.findViewById<ImageView>(R.id.cancel)
        val camera = dialogView.findViewById<TextView>(R.id.camera)
        val gallery = dialogView.findViewById<TextView>(R.id.gallery)
        val mBuilder = AlertDialog.Builder(context).setView(dialogView)
        val mAlertDialog = mBuilder.show()
        cancel.setOnClickListener { mAlertDialog.dismiss() }
        camera.setOnClickListener {
            mAlertDialog.dismiss()
            checkPermission(1001)
        }
        gallery.setOnClickListener {
            mAlertDialog.dismiss()
            checkPermission(1002)
        }


    }

    fun checkPermission(requestCode: Int) {
        val permissionArrays =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissionArrays, requestCode)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.size > 0) {
            openCamera()
        } else if (requestCode == 1002 && grantResults.size > 0) {
            openGallery()
        } else {
            AlertError.show(requireContext(), "Please Grant Camera & Storage Permissions")
        }

    }

    private fun createImageFile(): File {
        val imageName = SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(Date())
        val imageDir = File(requireContext().getFilesDir(), "/images")
        if (imageDir.exists()) {
            imageDir.delete()
        }
        imageDir.mkdirs()
        val imageFile = File.createTempFile(
            imageName, /* prefix */
            ".jpg", /* suffix */
            imageDir      /* directory */
        )
        return imageFile
    }

    var fileName = ""
    fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File = createImageFile()
        if (photoFile != null) {
            fileName = photoFile.absolutePath
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, 1001)
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 1002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val file = File(fileName)
            val uri: Uri = Uri.fromFile(file)
            val bitmap = BitmapUtil.rotateImageIfRequired(requireContext(), uri)
            val filePath = getBitmapToFilePath(requireContext(), bitmap)
            if (filePath != null)
                viewModel.setImageName(filePath)
        } else if (requestCode == 1002 && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data!!
            val bitmap = BitmapUtil.rotateImageIfRequired(requireContext(), uri)
            val filePath = getBitmapToFilePath(requireContext(), bitmap)
            if (filePath != null)
                viewModel.setImageName(filePath)

        }
    }


    fun registerCheck() {
        if (input_first_name.text.toString().isEmpty()) {
            input_first_name.setError("First Name can't be blank!")
            input_first_name.requestFocus()
        } else if (input_last_name.text.toString().isEmpty()) {
            input_last_name.setError("Last Name can't be blank!")
            input_last_name.requestFocus()
        } else if (input_phone.text.toString().length < 10) {
            input_phone.setError("Phone minimum 10 digit")
            input_phone.requestFocus()
        } else if (!isEmailValid(input_email.text.toString())) {
            input_email.setError("Email can't be blank or Invalid!")
            input_email.requestFocus()
        } else if (input_password.text.toString().length < 5) {
            input_password.setError("Password minimum 5 digit")
            input_password.requestFocus()
        } else if (input_confirm_password.text.toString().length < 5) {
            input_confirm_password.setError("Confirm Password minimum 5 digit")
            input_confirm_password.requestFocus()
        } else if (!input_confirm_password.text.toString()
                .equals(input_confirm_password.text.toString())
        ) {
            AlertError.show(requireActivity(), "Password and Confirm Password are not the same!")
        } else {
            viewModel.checkFileValidation()
        }
    }


}

