package com.csestateconnect.ui.home.projects_ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

import com.csestateconnect.R
import com.csestateconnect.adapters.projectAdapters.ProjectHorizontalImagesAdapter
import com.csestateconnect.databinding.FragmentProjectHorizontalImagesBinding
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.viewmodel.ProjectsViewModel

/**
 * A simple [Fragment] subclass.
 */
class ProjectHorizontalImagesFragment : Fragment() {



    lateinit var viewModel: ProjectsViewModel
    var previousId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_horizontal_images, container, false)

        val binding: FragmentProjectHorizontalImagesBinding = FragmentProjectHorizontalImagesBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ProjectsViewModel::class.java)
        binding.projHorizontalImgViewmodel = viewModel
        binding.lifecycleOwner = this


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousId = arguments?.get("projectId").toString()
        viewModel.getAllProjectData().observe(this, androidx.lifecycle.Observer<List<Projectsdata>>{ projectData ->
            setImagesInViewPager(projectData)

        } )
    }

    fun setImagesInViewPager(projectImageList:  List<Projectsdata>) {

        var projectImagesAdapter: ProjectHorizontalImagesAdapter? = null
        for (i in 0..projectImageList.get(0).results!!.size - 1 ) {

            val projectValues = projectImageList.get(0).results!!.get(i)
            if (projectValues!!.id.toString() == previousId){
                projectImagesAdapter =
                    ProjectHorizontalImagesAdapter(
                        projectValues.projectImages!!,
                        context!!
                    )
            }
        }

        val mPager = view!!.findViewById<ViewPager>(R.id.project_images_slider)
        mPager.setAdapter(projectImagesAdapter)
    }


}


