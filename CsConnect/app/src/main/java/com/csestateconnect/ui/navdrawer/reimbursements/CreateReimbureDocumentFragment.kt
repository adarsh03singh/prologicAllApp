package com.csestateconnect.ui.navdrawer.reimbursements

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentAddDocumentBinding
import com.csestateconnect.databinding.FragmentCreateReimbureDocumentBinding
import com.csestateconnect.viewmodel.ClientsViewModel
import com.csestateconnect.viewmodel.ReimbursementViewModel
import droidninja.filepicker.utils.ContentUriUtils
import kotlinx.android.synthetic.main.fragment_add_document.*
import kotlinx.android.synthetic.main.fragment_create_reimbure_document.*
import kotlinx.android.synthetic.main.fragment_update_document.*
import java.io.ByteArrayOutputStream
import java.io.File

class CreateReimbureDocumentFragment : Fragment() {

    var reimbursePreviousId: Int? = null
    var path: Uri? = null
    lateinit var viewModel: ReimbursementViewModel
    internal lateinit var bitmap: Bitmap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_reimbure_document, container, false)

        val binding: FragmentCreateReimbureDocumentBinding = FragmentCreateReimbureDocumentBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ReimbursementViewModel::class.java)
        binding.addReimburseDocViewmodel = viewModel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref: SharedPreferences = context!!.getSharedPreferences("PREFERENCE_NAME", 0)
        reimbursePreviousId = sharedPref.getInt("reimburseList_id", 0)

        rembrs_upload_doc_image.setOnClickListener {
            val mimeTypes = arrayOf(
                "image/*",
                "application/pdf",
                "application/zip",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"
            )

            val intent = Intent()
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setType("*/*")
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), 100)
        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            100 -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file

                val uri: Uri? = data?.data
                val uriString: String = uri.toString()
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context!!.getContentResolver() , Uri.parse(uri.toString()))
                    rembrs_upload_doc_image.setImageBitmap(bitmap)
                }catch (e: Exception){
                    e.printStackTrace()
                }
                val myFile = File(uriString)
                val pth = ContentUriUtils.getFilePath(context!!, uri!!)
                val path: String = myFile.path
//                var displaydata: String? = null
//                if (uriString.startsWith("content://")) {
//                    displaydata = getDataColumn(uri, null, null);
//
//                }

                viewModel.doc_image_url.value = pth
                viewModel.reimburse_id_forDoc.value = reimbursePreviousId.toString()

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }*/

    fun getDataColumn(uri: Uri?, selection: String?,
                      selectionArgs: Array<String?>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {

            cursor = requireContext().contentResolver.query(uri!!, projection, selection, selectionArgs,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
        finally {
            cursor?.close()
        }
        return null
    }
      override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
          super.onActivityResult(requestCode, resultCode, result)

          if (resultCode == Activity.RESULT_OK) {
              val selectedFileURI: Uri = result!!.data!!
              var tempUri:Uri? = null
              try {
                  bitmap = MediaStore.Images.Media.getBitmap(context!!.getContentResolver() , Uri.parse(selectedFileURI.toString()))
                  rembrs_upload_doc_image.setImageBitmap(bitmap)
                   tempUri = getImageUri(context!!, bitmap)
              }catch (e: Exception){
                  e.printStackTrace()
              }

              if (!selectedFileURI.equals(null)) {
                  var idFile:String? = null
                  var error:Unit? = null
                  try {
                      idFile = getRealPathFromURI(tempUri!!)
                  }catch (e: Exception){
                      error = e.printStackTrace()
                  }
                  if(error == null){
//                        val file = File(selectedFileURI.path.toString())
//                        Log.d("", "File : " + file.name)
                      viewModel.doc_image_url.value = idFile.toString()
                      viewModel.reimburse_id_forDoc.value = reimbursePreviousId.toString()
                  }
                  else {


//                    val filePathFromUri = FileUtils.getPath(context!!, selectedFileURI)
//                    val file = File(filePathFromUri)
//                    val absolutePath = file.absolutePath
//                        val absolutePath = file.absolutePath
//                      val g =  getPDFPath(selectedFileURI)
                      val file = File(selectedFileURI.path.toString())
//                        Log.d("", "File : " + file.name)
                      if(!file.equals(null)){
                          rembrs_upload_doc_image.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon))
                      }
                      viewModel.doc_image_url.value = file.toString()
                      viewModel.reimburse_id_forDoc.value = reimbursePreviousId.toString()
                  }
              }


          }
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

    @SuppressLint("Recycle")
    fun getPDFPath(uri: Uri?): String? {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"),
            java.lang.Long.valueOf(id)
        )
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            context!!.getContentResolver().query(contentUri, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

}