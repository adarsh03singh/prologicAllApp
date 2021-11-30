package com.prologic.laughinggoat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.viewpager.widget.PagerAdapter

import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.utils.ItemClickListener
import com.prologic.laughinggoat.utils.loadImage


class ProductImageSlider : PagerAdapter() {
    private var arrayList = ArrayList<String>()
    private var itemClickListener: ItemClickListener? = null

    fun setUpdateAdapter(arrayList: ArrayList<String>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val layoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val view = layoutInflater!!.inflate(R.layout.product_image_slider, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        container.addView(view)
        loadImage(imageView,arrayList[position])
        imageView.setOnClickListener {
            itemClickListener?.onItemClicked(position)
        }
        return view
    }

}