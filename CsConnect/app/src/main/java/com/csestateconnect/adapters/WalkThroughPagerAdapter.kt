package com.csestateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.csestateconnect.R

class WalkThroughPagerAdapter : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private val layouts = intArrayOf(R.layout.walk_through_slide_1, R.layout.walk_through_slide_2, R.layout.walk_through_slide_3, R.layout.walk_through_slide_4)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(container.context)

        val view = layoutInflater!!.inflate(layouts[position], container, false)
        container.addView(view)

        return view
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}