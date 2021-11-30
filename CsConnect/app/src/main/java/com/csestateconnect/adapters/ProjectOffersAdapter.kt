package com.csestateconnect.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.ui.home.projects_ui.ProjectOffersFragment
import kotlinx.android.synthetic.main.offers_recycleview_items.view.*

class ProjectOffersAdapter(val users: ArrayList<ProjectOffersFragment.offersModel>,
                           val clickItem: (String) ->Unit) :
    RecyclerView.Adapter<ProjectOffersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.offers_recycleview_items,p0,false)
        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

    p0.images.setImageResource(users.get(p1).img)

       clickItem(users.get(p1).img!!.toString())

    }

    override fun getItemCount(): Int {
        return users.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var images = itemView.offer_image

    }

    override fun getItemId(position: Int): Long {
//        return position.toLong()
        if(TextUtils.isEmpty(users.get(position).img.toString())){
            return position.toLong()
        }else
            return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}