package com.prologic.strains.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.prologic.strains.R
import com.prologic.strains.model.slider.SliderResult
import com.prologic.strains.utils.loadImageOriginal


class HomeSliderAdapter : PagerAdapter() {
    var arrayList = SliderResult()

    fun updateAdapter(arrayList: SliderResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val inflater =
            container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.home_slider_adapter, container, false)
        val image = view.findViewById<ImageView>(R.id.image)
        loadImageOriginal(image, arrayList[position].url)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ViewGroup)
    }
}