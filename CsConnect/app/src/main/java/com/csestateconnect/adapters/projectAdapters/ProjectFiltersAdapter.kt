package com.csestateconnect.adapters.projectAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*


class ProjectFiltersAdapter(val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ProjectFilterViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProjectFilterViewHolder {
        return ProjectFilterViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.filter_checkbox_item,
                p0,
                false
            )

        )
    }

    override fun onBindViewHolder(p0: ProjectFilterViewHolder, p1: Int) {
        p0.propertyType?.text = items.get(p1)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

}



class ProjectFilterViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to

    val propertyType = view.filter_checkbox
}