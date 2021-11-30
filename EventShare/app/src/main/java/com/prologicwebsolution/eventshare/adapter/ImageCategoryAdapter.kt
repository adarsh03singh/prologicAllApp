package com.prologicwebsolution.eventshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.ui.imageList.ImageListfragment


class ImageCategoryAdapter(val categoryList: ArrayList<ImageListfragment.ImageCategoryModel>) : RecyclerView.Adapter<ImageCategoryAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_category_list_items, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(categoryList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return categoryList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: ImageListfragment.ImageCategoryModel) {
            val textViewName = itemView.findViewById(R.id.categorytxtId) as TextView

            textViewName.text = user.categoryName

        }
    }
}