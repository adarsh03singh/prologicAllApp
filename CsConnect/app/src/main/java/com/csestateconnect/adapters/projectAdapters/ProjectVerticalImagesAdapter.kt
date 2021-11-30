package com.csestateconnect.adapters.projectAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.ProjectImage
import java.lang.Exception


class ProjectVerticalImagesAdapter(val users: List<ProjectImage?>?, val projectid: Int) : RecyclerView.Adapter<ProjectVerticalImagesAdapter.ViewHolder>() {

    var navcontroller: NavController? = null
    var countProjectId: Int? = 0
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(com.csestateconnect.R.layout.project_vertical_image_item,p0,false)

        return ViewHolder(
            v
        )
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    try {

            Glide.with(holder.itemView.context).load(users!!.get(position)!!.imageUrl)
                .into(holder.project_images)

        if (users.get(position)!!.imageUrl != null) {
            holder.project_images.setOnClickListener {
                val bundle = bundleOf("projectId" to projectid)
                navcontroller!!.navigate(
                    R.id.action_projectImagesFragment_to_projectHorizontalImagesFragment,
                    bundle
                )
            }
        }


    }catch (e: Exception){
        e.printStackTrace()
    }


    }
    override fun getItemCount(): Int {
        var count: Int = 0


        try {

              count = users!!.size


        }catch (e: Exception){
          e.printStackTrace()
      }

        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val project_images = itemView.findViewById<ImageView>(R.id.project_images)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}