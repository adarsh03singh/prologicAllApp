package com.prologicwebsolution.eventshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.ui.imageShare.ImageShareFragment


class ColorAdapter(val colorList: ArrayList<ImageShareFragment.ColorModel>, val itemClick: (Int) -> Unit) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.color_list_items,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colorList.get(position).buttonColor

        holder.colorImageButton.setBackgroundColor(color)
        holder.colorImageButton.setOnClickListener {
            itemClick(color)
        }

    }

    override fun getItemCount(): Int {

          return colorList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val colorImageButton = itemView.findViewById(R.id.colorimageButton) as ImageButton

//        colorImageButton.setBackgroundColor(imageColor.buttonColor)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
   /* //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.color_list_items, parent, false)

        v.setOnClickListener {
            itemClick(viewType)
        }
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ColorAdapter.ViewHolder, position: Int) {
        holder.bindItems(colorList[position])

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return colorList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(imageColor: ImageShareActivity.ColorModel) {
            val colorImageButton = itemView.findViewById(R.id.colorimageButton) as ImageButton

            colorImageButton.setBackgroundColor(imageColor.buttonColor)
        }
    }*/
}