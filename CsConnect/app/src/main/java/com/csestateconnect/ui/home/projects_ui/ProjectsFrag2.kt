package com.csestateconnect.ui.home.projects_ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.ProjectAdapter
import com.csestateconnect.adapters.projectAdapters.ProjectCitySearchAdapter
import com.csestateconnect.databinding.FragmentProjectsFrag2Binding
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.db.data.projects.Result
import com.csestateconnect.viewmodel.ProjectsViewModel
import kotlinx.android.synthetic.main.fragment_projects_frag2.*

class ProjectsFrag2 : Fragment() {

    lateinit var viewModel: ProjectsViewModel
    var citySelectedName: String? = null
    var userCityName: String? = null
    var projectCityLayoutButton: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_projects_frag2, container, false)

        val binding: FragmentProjectsFrag2Binding = FragmentProjectsFrag2Binding.bind(view)
        viewModel = ViewModelProviders.of(this.requireParentFragment()).get(ProjectsViewModel::class.java)
        binding.projViewmodel = viewModel
        binding.lifecycleOwner = this

        projectCityLayoutButton = view.findViewById(R.id.project_citySearch_layout)
        if(isNetworkConnected(context!!))
        {
            try {
//                viewModel.callGetFavouriteProjectAPIAndStore()
                viewModel.callCommissionAPIAndStore()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else
        {
            Toast.makeText(context,"No internet found.",Toast.LENGTH_LONG).show()
        }

        viewModel.getAllProjectData().observe(this, Observer<List<Projectsdata>> { projectData ->

            val sharedPref: SharedPreferences = view!!.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
           val applyButtonEnableValue =  sharedPref.getString("applyButtonEnabled", "")

             if (!projectData.isEmpty()) {

                 if (applyButtonEnableValue == "true") {

                     val listOfProject = viewModel.filteredProjectsData.value?.results!!

                     setUpProjectRecyclerView(listOfProject)
                     projectSearch_cardView_layout.visibility = View.GONE

                 } else {

                     projectSearch_cardView_layout.visibility = View.VISIBLE
                     Log.i("projectDataSize", projectData.size.toString())
                     val projectResults = projectData.get(0).results

                     val cityMapList = projectResults!!.groupBy({ it!!.city!!.name }, { it })
                     setUpCityRecyclerView(cityMapList)
                     userCityName = arguments?.get("city_name").toString()
                     if (userCityName == "null") {

                         // sharedPref Code Use for check city When We Coming From ProjectDetail
                         try {
                             val sharedPref: SharedPreferences =
                                 view!!.context.getSharedPreferences("PREFERENCE_NAME", 0)
                             userCityName = sharedPref.getString("cityName", "")
                         } catch (e: Exception) {
                             e.printStackTrace()
                         }

                         if (userCityName.isNullOrEmpty()) {
                             userCityName = viewModel.userCity
                         }
                     }
                     project_city_text.text = userCityName

                     for (position in 0..cityMapList.size - 1) {
                         citySelectedName = cityMapList.map { it.key }.get(position)
                         if (citySelectedName == userCityName) {
                             val projectsList = cityMapList.getValue(citySelectedName)
                             setUpProjectRecyclerView(projectsList)
                         }
                     }
                 }
             }

        } )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        projectCityLayoutButton!!.setOnClickListener {
            if (project_city_recycler_card_view.visibility == View.GONE) {
                project_city_recycler_card_view.visibility = View.VISIBLE
                project_city_down_arrow.visibility = View.GONE
                project_city_up_arrow.visibility = View.VISIBLE
            } else {
                project_city_recycler_card_view.visibility = View.GONE
                project_city_down_arrow.visibility = View.VISIBLE
                project_city_up_arrow.visibility = View.GONE
            }
        }
    }

    fun setUpProjectRecyclerView(projectResultList : List<Result?>)
    {
        val projectRecyclerViewAdapter =
            ProjectAdapter(projectResultList)
        project_recycler_view.adapter = projectRecyclerViewAdapter
        project_recycler_view.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        project_recycler_view.setHasFixedSize(true)
    }

    fun setUpCityRecyclerView(projectCityList: Map<String?,List<Result?>>) {

        val cityRecyclerViewAdapter = ProjectCitySearchAdapter(projectCityList)
        { position ->

            //save cityName in Local Storage For Shown this Fragment When come to ProjectDetails
            userCityName = projectCityList.map { it.key }.get(position)
            val sharedPref: SharedPreferences = view!!.context.getSharedPreferences("PREFERENCE_NAME", 0)
            val editor = sharedPref.edit()
            editor.putString("cityName",userCityName )
            editor.apply()

                    val projectsViewList =  projectCityList.getValue(userCityName)
                    setUpProjectRecyclerView(projectsViewList)
            project_city_recycler_card_view.visibility = View.GONE
            project_city_down_arrow.visibility = View.VISIBLE
            project_city_up_arrow.visibility = View.GONE

            project_city_text.text = userCityName

        }

        project_city_recycler_view.adapter = cityRecyclerViewAdapter
        project_city_recycler_view.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        project_city_recycler_view.setHasFixedSize(true)
    }


    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
