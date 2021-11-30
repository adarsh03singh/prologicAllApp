package com.csestateconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.homepagedata.banner.Result
import kotlinx.android.synthetic.main.home_project_items.view.*


class HomeProjectSlideAdapter(val context: Context, val result: List<Result>) : PagerAdapter() {

    var navcontroller: NavController? = null
    internal var layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val image = result.map { it.banner_image }


    override fun getCount(): Int {
        if (result.size < 6)
            return result.size
        else
            return 5
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === (`object` as ConstraintLayout)
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

//        result.sortedBy { it.priority }

        val itemView = layoutInflater.inflate(R.layout.home_project_items, container, false)
        val imageView = itemView.findViewById(R.id.project_image_icon) as ImageView
        Glide.with(context).load(image[position]).into(imageView)
        itemView.banner_projectName.setText(result[position].project.name)

        val low = result[position].low_cost_view
        val high = result[position].high_cost_view
        if (!low.isNullOrBlank() && !high.isNullOrBlank()) {
            itemView.banner_projectPrice.setText("$low - $high")
        }
        else if (!low.isNullOrBlank() && high.isNullOrBlank()){
            itemView.banner_projectPrice.setText("$low onwards")
        }
        else if (low.isNullOrBlank() && !high.isNullOrBlank()){
            itemView.banner_projectPrice.setText("upto $high")
        }
        else {
            itemView.banner_projectPrice.setText("")
        }
        container.addView(itemView)

        imageView.setOnClickListener {
            navcontroller = Navigation.findNavController(container)
//                val bundle = bundleOf("projectId" to userData.id)
            navcontroller!!.navigate(R.id.action_navigation_home_to_projectDetailFragment)
        }
        return itemView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}