package com.csestateconnect.adapters

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.favouriteProject.FavouriteProject
import com.csestateconnect.db.data.listOfClients.Result
import com.csestateconnect.ui.navdrawer.FavoritesFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.client_document_items.view.*
import kotlinx.android.synthetic.main.clients_list_items.view.*
import kotlinx.android.synthetic.main.favorite_project_item.view.*
import java.lang.Exception

class ReimburseDocListAdapter(
    var reimburseDocList : List<com.csestateconnect.db.data.reimbursements.reimbursementDocs.Result?>, val itemClick: (Int) -> Unit)
    : RecyclerView.Adapter<ReimburseDocListAdapter.ViewHolder>() {

    var navcontroller: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.client_document_items,p0,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

           val  rData = reimburseDocList.get(position)

        try {
        holder.name?.text = rData!!.name
            if (!rData.docUrl.isNullOrEmpty()) {
                Glide.with(holder.itemView.context).load(rData.docUrl).into(holder.doc_imag)

                 holder.view_docs.setOnClickListener {
                     val dcId = rData.id
                     val bundle = bundleOf("dcId" to dcId)
                    navcontroller!!.navigate(R.id.pdfFragment,bundle)
                }
            }

            holder.update_docs.setOnClickListener {
                val docId = rData.id
                val docName = rData.name
                val imgurl = rData.docUrl
                val sharedPreference = holder.itemView.context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("docName",docName)
                editor.putString("imgUrl",imgurl)
                editor.putInt("document_id",docId!!)
                editor.apply()

                navcontroller!!.navigate(R.id.updateDocument)

            }

            holder.delete_docs.setOnClickListener {
                    itemClick(rData.id!!)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, getItemCount())
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
    override fun getItemCount(): Int {
        return reimburseDocList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.document_name
        val doc_imag = view.document_image
        val delete_docs = view.doc_delete_item
        val update_docs = view.update_doc_item
        val view_docs = view.view_doc_item

    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}