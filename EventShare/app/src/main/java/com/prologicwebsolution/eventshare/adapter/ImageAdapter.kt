package com.prologicwebsolution.eventshare.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.prologicwebsolution.eventshare.R

class ImageAdapter internal constructor(private val mContext: Context) : BaseAdapter() {

    // References to our images
    private val mThumbIds = arrayOf(
        R.drawable.one,
        R.drawable.two,
        R.drawable.three,
        R.drawable.four,
        R.drawable.five
    )


    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    // Create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            imageView.layoutParams = ViewGroup.LayoutParams(300, 300)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        } else {
            imageView = (convertView as ImageView?)!!
        }

        imageView.setImageResource(mThumbIds[position])
        return imageView
    }
}
