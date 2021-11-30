package com.csestateconnect.ui.navdrawer.clients

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentPdfBinding
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.viewmodel.ClientsViewModel
import kotlinx.android.synthetic.main.fragment_pdf.*


class PdfFragment : Fragment() {

    var docUrl: String?=null
    lateinit var viewModel: ClientsViewModel
    var dcId: Int? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pdf, container, false)

        val binding: FragmentPdfBinding = FragmentPdfBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)
        binding.pdfViewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.getClientDocListFromDatabase().observe(this, Observer<GetClientDocListEntity> { docData ->

            try{
                for (i in 0..docData.results!!.size - 1 ) {

                    val cilData = docData.results.toTypedArray()
                    val resId = cilData.get(i)!!.id

                    if (resId == dcId) {

                             docUrl =  cilData.get(i)!!.docUrl

                        pdf_view.settings.javaScriptEnabled = true
                        pdf_view.settings.allowFileAccess = true
                        pdf_view.getSettings().setDomStorageEnabled(true)
                        pdf_view.getSettings().setLoadsImagesAutomatically(true)
                        pdf_view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
                        pdf_view.getSettings().setBuiltInZoomControls(true);
                        pdf_view.getSettings().setDisplayZoomControls(false)

                        val url = "https://docs.google.com/gview?embedded=true&url=" + docUrl

                        pdf_view.loadUrl(url)

                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         dcId = arguments?.getInt("dcId")
    }
}