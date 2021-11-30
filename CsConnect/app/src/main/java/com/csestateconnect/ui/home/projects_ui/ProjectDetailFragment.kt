package com.csestateconnect.ui.home.projects_ui


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.databinding.FragmentProjectDetailBinding
import com.csestateconnect.viewmodel.ProjectsViewModel
import kotlinx.android.synthetic.main.fragment_project_detail.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.csestateconnect.adapters.projectAdapters.*
import com.csestateconnect.db.data.commissionSlab.ProjectCommissionSlab
import com.google.android.material.tabs.TabLayout

import java.lang.Exception
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.view.inputmethod.InputMethodManager
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.db.data.projects.Result
import com.csestateconnect.db.data.projects.UnitPlan
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ProjectDetailFragment : Fragment(),View.OnClickListener {

        private var amenitiesViewPager: ViewPager? = null
    private var unitPlanViewPager: ViewPager? = null
    private var viewPagerAdapter: AmenitiesAdapter? = null
    private var unitPlanviewPagerAdapter: ProjectUnitPlansAdapter? = null
    private var amenitiesTabLayout: TabLayout? = null
    private var unitPlanTabLayout: TabLayout? = null


    lateinit var viewModel: ProjectsViewModel
    var previousId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.csestateconnect.R.layout.fragment_project_detail, container, false)

        val binding: FragmentProjectDetailBinding = FragmentProjectDetailBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ProjectsViewModel::class.java)
        binding.projViewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.getAllProjectData().observe(this, Observer<List<Projectsdata>>{ dataList ->
            viewModel.getProjectDetailByPreviousId(dataList,view)
            try {
                for (i in 0..dataList.get(0).results!!.size - 1 ) {

                    val projectValues = dataList.get(0).results!!.get(i)

                    if (projectValues!!.id.toString() == previousId) {

                        setUpConnectinRoadsInRecyclerView(projectValues)
                        setUpWowFactorsInRecyclerView(projectValues)
                        setUpAmenitiesValuesInTab(projectValues)
                        setUpUnitPlanValuesInTab(projectValues)
                    }

                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            setImage()
        } )

        viewModel.getCommissionDataFromDatabase().observe(this, Observer<List<ProjectCommissionSlab>>{ commissionData ->

            if (!commissionData.isEmpty()) {

                commissionData.forEach {
                    val commissionProjectId = it.project!!.id
                    val checkCommissionSlab = it.slabsAvailable
                    if(commissionProjectId.toString() == previousId){
                        project_commissionView.visibility = View.VISIBLE
                        project_commissionRmButton.visibility = View.GONE
                        if(checkCommissionSlab == true){
                            val commissionSlab = it.commissionSlabs

                            val CommissionTypeArray = commissionSlab!!.split(",").toTypedArray()
                            setUpCommissionData(CommissionTypeArray!!)

                        }

                    }else{
                        project_commissionView.visibility = View.GONE
                        project_commissionRmButton.visibility = View.VISIBLE
                    }

                }
            }
            else{
                project_commissionView.visibility = View.GONE
                project_commissionRmButton.visibility = View.VISIBLE
            }
        } )


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), HIDE_NOT_ALWAYS)


        val sharedPref: SharedPreferences = view.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        previousId = sharedPref.getString("projectId", "")
//        val shareCount = sharedPref.getInt("shareCount", 0)

//         previousId = arguments?.get("projectId").toString()
        Log.i("id",previousId.toString())

        if(!previousId.isNullOrEmpty()) {
            viewModel.previousProjectIds = previousId!!.toInt()
        }
//        if(shareCount == 1) {
//           screenshoot()
//        }



        val wow_down_button = view.findViewById(com.csestateconnect.R.id.wow_down_button) as ConstraintLayout
        wow_down_button.setOnClickListener(this)
        val wow_Up_button = view.findViewById(com.csestateconnect.R.id.wow_Up_button) as ConstraintLayout
        wow_Up_button.setOnClickListener(this)
        val connectingroads_down_button = view.findViewById(com.csestateconnect.R.id.connectingroads_down_button) as ConstraintLayout
        connectingroads_down_button.setOnClickListener(this)
        val connectingroads_up_button = view.findViewById(com.csestateconnect.R.id.connectingroads_up_button) as ConstraintLayout
        connectingroads_up_button.setOnClickListener(this)

    }
    override fun onClick(view: View?) {
        when (view!!.getId()) {

           com.csestateconnect.R.id.wow_down_button -> {
                wow_down_button.visibility = View.GONE
                wow_Up_button.visibility = View.VISIBLE
                val setWidhtHeight = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                wow_factors_recyclerviewTxt.layoutParams = setWidhtHeight
            }

           com.csestateconnect.R.id.wow_Up_button -> {
                wow_Up_button.visibility = View.GONE
                wow_down_button.visibility = View.VISIBLE
                val setWidhtHeight = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 260)
                wow_factors_recyclerviewTxt.layoutParams = setWidhtHeight
            }

           com.csestateconnect.R.id.connectingroads_down_button -> {
                connectingroads_down_button.visibility = View.GONE
                connectingroads_up_button.visibility = View.VISIBLE
                val setWidhtHeight = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                connecting_road_recyclerview.layoutParams = setWidhtHeight
            }

         com.csestateconnect.R.id.connectingroads_up_button -> {
                connectingroads_up_button.visibility = View.GONE
                connectingroads_down_button.visibility = View.VISIBLE
                val setWidhtHeight = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 345)
                connecting_road_recyclerview.layoutParams = setWidhtHeight
            }

        }
    }

    fun setImage(){
            val imgval = viewModel.project_image_icon
        val developerIcon = viewModel.project_developer_imageIcon

            if (imgval != null) {
                project_image_view.setBackground(null)
                Glide.with(this).load(imgval).into(project_image_view!!)
            }
        if(developerIcon != null){
                Glide.with(this).load(developerIcon).into(developer_icon_image!!)
            }

    }


    fun setUpConnectinRoadsInRecyclerView(resultValues: com.csestateconnect.db.data.projects.Result)
    {
        val roadList = resultValues.distances

        if(roadList!!.size < 3){
            connecting_road_buttons.visibility = View.GONE
        }else{
            connecting_road_buttons.visibility = View.VISIBLE
        }

        val projectRoadRecyclerViewAdapter = ProjectConnectingRoadAdapter(roadList!!)
        connecting_road_recyclerview.adapter = projectRoadRecyclerViewAdapter
        connecting_road_recyclerview.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        connecting_road_recyclerview.setHasFixedSize(true)
    }

    fun setUpWowFactorsInRecyclerView(resultValues: com.csestateconnect.db.data.projects.Result)
    {
        val wowFactorList = resultValues.projectWowFactors
        if(wowFactorList!!.size < 3){
            wow_factors_buttons.visibility = View.GONE
        }else{
            wow_factors_buttons.visibility = View.VISIBLE
        }

        val projectWowFactorRecyclerViewAdapter = ProjectWowFactorAdapter(wowFactorList!!)

        wow_factors_recyclerviewTxt.adapter = projectWowFactorRecyclerViewAdapter
        wow_factors_recyclerviewTxt.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        wow_factors_recyclerviewTxt.setHasFixedSize(true)
    }

    fun setUpAmenitiesValuesInTab(resultValues: com.csestateconnect.db.data.projects.Result) {
        try {
                    val amenities = resultValues.projectAmenities!!.toTypedArray()

                    val mapOfList = amenities.groupBy({ it!!.amenityCategory!!.name }, {it})

                    viewPagerAdapter =
                        AmenitiesAdapter(
                            context!!,
                            mapOfList
                        )

        }catch (e: Exception){
            e.printStackTrace()
        }


        amenitiesTabLayout = view!!.findViewById<TabLayout>(com.csestateconnect.R.id.amenities_tab_layout)
        amenitiesViewPager = view!!.findViewById(com.csestateconnect.R.id.amenities_view_pager)

        amenitiesViewPager?.setAdapter(viewPagerAdapter)
        amenitiesTabLayout!!.setupWithViewPager(amenitiesViewPager)

    }

    fun setUpUnitPlanValuesInTab(resultValues: Result) {
        try {

           val unitPlans = ArrayList<UnitPlan>()

                    for (j in 0..resultValues.projectTowers!!.size - 1 ) {

                        resultValues.projectTowers.get(j)!!.unitPlans!!.forEach { unitPlanData ->
                            if (unitPlanData != null) {
                                unitPlans.add(unitPlanData)
                            }
                        }

                    }


                    val mapOfList = unitPlans.groupBy({ it.bhkType!!.name }, { it })
                    unitPlanviewPagerAdapter = ProjectUnitPlansAdapter(context!!,  mapOfList)


        }catch (e: Exception){
            e.printStackTrace()
        }

        unitPlanViewPager = view!!.findViewById(com.csestateconnect.R.id.unitPlan_view_pager)
        unitPlanViewPager?.setAdapter(unitPlanviewPagerAdapter)

        unitPlanTabLayout = view!!.findViewById<TabLayout>(com.csestateconnect.R.id.units_tab_layout)
        unitPlanTabLayout?.setupWithViewPager(unitPlanViewPager)

    }
    fun setUpCommissionData(commissionValues: Array<String>) {

        val projectCommissionValuesAdapter = ProjectCommissionValuesAdapter(commissionValues)

        project_commission_recyclerView.adapter = projectCommissionValuesAdapter
        project_commission_recyclerView.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        project_commission_recyclerView.setHasFixedSize(true)
    }

}
