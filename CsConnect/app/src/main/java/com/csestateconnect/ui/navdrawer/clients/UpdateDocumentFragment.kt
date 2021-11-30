package com.csestateconnect.ui.navdrawer.clients

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentUpdateDocumentBinding
import com.csestateconnect.viewmodel.ClientsViewModel
import kotlinx.android.synthetic.main.fragment_add_document.*
import kotlinx.android.synthetic.main.fragment_update_document.*
import java.io.ByteArrayOutputStream
import java.io.File

class UpdateDocumentFragment : Fragment() {

    var previousPdfUrl: String? = null
    var document_id: Int? = null
    var imgUrl: String? = null
    var docName: String? = null
    var path: Uri? = null
    lateinit var viewModel: ClientsViewModel
    internal lateinit var bitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view = inflater.inflate(R.layout.fragment_update_document, container, false)

                val binding: FragmentUpdateDocumentBinding = FragmentUpdateDocumentBinding.bind(view)
                viewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)
                binding.updateDocViewmodel = viewModel
                binding.lifecycleOwner = this

                return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref: SharedPreferences = context!!.getSharedPreferences("PREFERENCE_NAME", 0)
        document_id = sharedPref.getInt("document_id", 0)
        imgUrl = sharedPref.getString("imgUrl", "")
        docName = sharedPref.getString("docName", "")
        previousPdfUrl = sharedPref.getString("previousPdfUrl", "")
        setOldData()
//        document_id = arguments?.getInt("document_id")

        upload_update_doc_image.setOnClickListener {

//            val mimeTypes = arrayOf(
//                "image/*",
//                "application/pdf",
//                "application/zip",
//                "application/vnd.ms-powerpoint",
//                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
//                "application/msword",
//                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
//                "text/plain"
//            )
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.setType("application/msword,application/pdf")// = "application/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)

        if (resultCode == Activity.RESULT_OK) {
            val selectedFileURI: Uri = result!!.data!!
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context!!.getContentResolver() , Uri.parse(selectedFileURI.toString()))
                upload_update_doc_image.setImageBitmap(bitmap)
            }catch (e: Exception){
                e.printStackTrace()
            }

            if (!selectedFileURI.equals(null)) {
                var idFile:String? = null
                var error:Unit? = null
                try {
                    idFile = getRealPathFromURI(selectedFileURI!!)
                }catch (e: Exception){
                    error = e.printStackTrace()
                }
                if(error == null){
                    val file = File(selectedFileURI.path.toString())
                    Log.d("", "File : " + file.name)
                    viewModel.doc_image_url.value = idFile.toString()
                    viewModel.doc_Id.value = document_id
                }
                else {

                    val file = File(selectedFileURI.path.toString())
                    Log.d("", "File : " + file.name)
                    viewModel.doc_image_url.value = file.toString()
//                        viewModel.doc_name.value = file.name
                    viewModel.doc_Id.value = document_id
                }
            }


        }
    }
    fun setOldData(){

        viewModel.document_name.value = docName
        Glide.with(this).asBitmap().load(imgUrl).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
            ) {
                upload_update_doc_image!!.setImageBitmap(resource)
                val tempUri1 = getImageUri(context!!, resource)
                var idFile:String? = null
                var error:Unit? = null
                try {
                    idFile = getRealPathFromURI(tempUri1!!)
                }catch (e: Exception){
                    error = e.printStackTrace()
                }
                if(error == null){
                    val file = File(tempUri1!!.path.toString())
                    Log.d("", "File : " + file.name)
                    viewModel.doc_image_url.value = idFile.toString()
                    viewModel.doc_Id.value = document_id
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

        if( viewModel.doc_image_url.value.equals(null)){
            val uriImg =  Uri.parse( imgUrl)
            val file = File(uriImg.path!!.toString())
            Log.d("", "File : " + file.name)
            upload_update_doc_image.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon))
            viewModel.doc_image_url.value = file.toString()
            viewModel.doc_Id.value = document_id
        }

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

    fun getRealPathFromURI(contentURI: Uri): String? {
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

}