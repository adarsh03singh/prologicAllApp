package com.csestateconnect.adapters.projectAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.ProjectImage
import com.github.chrisbanes.photoview.PhotoView
import java.lang.Exception


class ProjectHorizontalImagesAdapter(val users: List<ProjectImage?>?, private val mContext: Context) :
    PagerAdapter() {


    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return users!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.project_horizontal_image_item, viewGroup,
            false) as ViewGroup
        val myImage = layout.findViewById(R.id.project_Horizontalimages) as PhotoView
        try {
            if(users!!.get(position)!!.imageUrl != null) {
                Glide.with(mContext).load(users.get(position)!!.imageUrl).into(myImage)
                viewGroup.addView(layout)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return layout
    }
}

