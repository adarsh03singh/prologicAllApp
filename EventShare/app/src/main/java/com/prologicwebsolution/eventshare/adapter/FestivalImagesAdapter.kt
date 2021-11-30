package com.prologicwebsolution.eventshare.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.data.FestivalImageModel
import com.prologicwebsolution.eventshare.data.imageData.*
import com.prologicwebsolution.eventshare.ui.imageList.ImageListfragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var x1: List<X1X> = ArrayList()
var x2: List<X2> = ArrayList()
var x3: List<X3> = ArrayList()
var x4: List<X4> = ArrayList()
var x5: List<X5> = ArrayList()
var x6: List<X6> = ArrayList()
var x7: List<X7> = ArrayList()
var x8: List<X8> = ArrayList()
var x9: List<X9> = ArrayList()

class FestivalImagesAdapter(val festivalImageList: ArrayList<FestivalImageModel>,  private val data: ArrayList<Date>,val itemClick: (Int) -> Unit): RecyclerView.Adapter<FestivalImagesAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.festival_image_item, parent, false)

        v.setOnClickListener {
            itemClick(viewType)
        }
        return ViewHolder(v)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindItems(festivalImageList[position])

        val currentDay = data[position]

   holder.imageView.setImageResource(festivalImageList[position].images)
        holder.imageView.setOnClickListener {
            itemClick(position)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return data.size
    }



    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        fun bindItems(festiValImages: FestivalImageModel) {
            val imageView = itemView.findViewById(R.id.festival_imageId) as ImageView

//            imageView.setImageResource(festiValImages.images)


//        }
    }

}


