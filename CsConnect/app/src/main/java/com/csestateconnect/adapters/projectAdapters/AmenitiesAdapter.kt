package com.csestateconnect.adapters.projectAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.ProjectAmenity
import java.lang.Exception


class AmenitiesAdapter(context: Context,val amenityItems: Map<String?,List<ProjectAmenity?>?> ):
    PagerAdapter() {

    internal var context:Context
        internal var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return amenityItems.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return amenityItems.map { it.key!! }.get(position)
    }

    init{
            this.context = context
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }


        override fun isViewFromObject(view:View, `object`:Any):Boolean {
            return view.equals(`object`)
        }

        override fun instantiateItem(container:ViewGroup, position:Int):Any {
            val amenityCategoryLayout =layoutInflater.inflate(R.layout.ameneties_recycler_view, container, false)
            val recyclerView = amenityCategoryLayout.findViewById<RecyclerView>(R.id.project_amenities_recyclerview)
            recyclerView.setLayoutManager(LinearLayoutManager(context))

            var projectAmenitiesAdapter: ProjectAmenitiesItemAdapter? = null

            try {
                for (i in 0..amenityItems.size - 1 ) {

                    val amenityKey = amenityItems.map { it.key }.get(i)
                    if (amenityKey == getPageTitle(position)){

                        val amenitiesValue =  amenityItems.getValue(amenityKey)
                        projectAmenitiesAdapter = ProjectAmenitiesItemAdapter(amenitiesValue)
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

            recyclerView.adapter = projectAmenitiesAdapter
            recyclerView.layoutManager = LinearLayoutManager(context,
                RecyclerView.HORIZONTAL,false)

            container.addView(amenityCategoryLayout, 0)

            return amenityCategoryLayout
        }

        override fun destroyItem(container: ViewGroup, position:Int, `object`:Any) {
            container.removeView(container)
        }
    }