package com.csestateconnect.ui.home.projects_ui


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.csestateconnect.R
import com.csestateconnect.adapters.projectAdapters.ProjectVerticalImagesAdapter
import com.csestateconnect.databinding.FragmentProjectVerticalImagesBinding
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.viewmodel.ProjectsViewModel
import kotlinx.android.synthetic.main.fragment_project_vertical_images.*

/**
 * A simple [Fragment] subclass.
 */
class ProjectVerticalImagesFragment : Fragment() {

    lateinit var viewModel: ProjectsViewModel
    var previousId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_vertical_images, container, false)

        val binding: FragmentProjectVerticalImagesBinding = FragmentProjectVerticalImagesBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ProjectsViewModel::class.java)
        binding.projImgViewmodel = viewModel
        binding.lifecycleOwner = this


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        previousId = arguments?.get("projectId").toString()
        val sharedPref: SharedPreferences = view.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        previousId = sharedPref.getString("projectId", "")

        viewModel.getAllProjectData().observe(this, Observer<List<Projectsdata>>{ projectData ->
            setUpImagesInRecyclerView(projectData)

        } )
    }
    fun setUpImagesInRecyclerView(projectImageList : List<Projectsdata>) {
        var projectImagesAdapter: ProjectVerticalImagesAdapter? = null
        for (i in 0..projectImageList.get(0).results!!.size - 1 ) {

        val projectValues = projectImageList.get(0).results!!.get(i)
        if (projectValues!!.id.toString() == previousId){
             projectImagesAdapter =
                 ProjectVerticalImagesAdapter(
                     projectValues.projectImages!!,
                     previousId!!.toInt()
                 )
        }
        }
        project_images_recycler_view.adapter = projectImagesAdapter
        project_images_recycler_view.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
    }

}
