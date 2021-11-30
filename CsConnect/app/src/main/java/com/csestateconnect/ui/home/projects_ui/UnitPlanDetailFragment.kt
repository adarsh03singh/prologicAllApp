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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide

import com.csestateconnect.R
import com.csestateconnect.adapters.projectAdapters.ProjectUnitPlanItemsAdapter
import com.csestateconnect.adapters.projectAdapters.UnitPlanDetailComponentlAdapter
import com.csestateconnect.databinding.FragmentUnitPlanDetailBinding
import com.csestateconnect.db.data.projects.*
import com.csestateconnect.viewmodel.ProjectsViewModel
import kotlinx.android.synthetic.main.fragment_unit_plan_detail.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class UnitPlanDetailFragment : Fragment() {


    var unitImages: ArrayList<List<UnitPlanImage?>?>? = null
    var unitPlans: ArrayList<UnitPlan?>? = null
    var viewModel: ProjectsViewModel? = null
    var previousProjectId: String? = null
    var unitPlanId: String? = null
    var unitPlanKey: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_unit_plan_detail, container, false)

        val binding: FragmentUnitPlanDetailBinding = FragmentUnitPlanDetailBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ProjectsViewModel::class.java)
        binding.unitPlanViewmodel = viewModel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref: SharedPreferences =
            view.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        previousProjectId = sharedPref.getString("projectId", "")
        unitPlanId = sharedPref.getString("unitPlanId", "")
        unitPlanKey = sharedPref.getString("unitPlanKey", "")



        viewModel!!.getAllProjectData().observe(this, Observer<List<Projectsdata>> { dataList ->

            try {
                for (i in 0..dataList.get(0).results!!.size - 1) {

                    val projectValues = dataList.get(0).results!!.get(i)

                    if (projectValues!!.id.toString() == previousProjectId) {

                        setUpUnitPlanValues(projectValues)
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    fun setUpUnitPlanValues(resultValues: Result) {
        try {

            val unitPlans = ArrayList<UnitPlan>()

            for (j in 0..resultValues.projectTowers!!.size - 1) {

                resultValues.projectTowers.get(j)!!.unitPlans!!.forEach { unitPlanData ->
                    if (unitPlanData != null) {
                        unitPlans.add(unitPlanData)
                    }
                }

            }


            val unitPlanList = unitPlans.groupBy({ it.bhkType!!.name }, { it })

            var projectUnitPlanAdapter: ProjectUnitPlanItemsAdapter? = null
            var unitPlanValue: List<UnitPlan>? = null
            try {
                for (i in 0..unitPlanList.size - 1 ) {

                    val unitKey = unitPlanList.map { it.key }.get(i)
                    if (unitKey == unitPlanKey){

                         unitPlanValue =  unitPlanList.getValue(unitPlanKey)

                    }
                }
                var unitAreaComponents: ArrayList<UnitInsideAreaComponent?>? = ArrayList()
                unitPlanValue!!.forEach {
                        val id =  it.id
                        if(id.toString() == unitPlanId){
                            Glide.with(this).load(it.iconImage).into(unitPlan_imge)
                            unit_type_and_bhk_view.text = it.propertyType!!.name + " , " + it.bhkType!!.name
                            unit_price_range_view.text = it.lowCostView + " , " + it.highCostView
                            unit_carper_area_view.text = it.carpetAreaView

                            superArea_progressBar.progress = it.superAreaPercentage!!
                            builtupArea_progressBar.progress = it.builtUpAreaPercentage!!
                            carpetArea_progressBar.progress = it.carpetAreaPercentage!!

                            superArea_progressBarTxt.text = it.superAreaPercentage!!.toString()
                            builtupArea_progressBarTxt.text = it.builtUpAreaPercentage!!.toString()
                            carpetArea_progressBarTxt.text = it.carpetAreaPercentage!!.toString()

                             it.unitInsideAreaComponents!!.forEach {
                                 unitAreaComponents!!.add (it)
                            }
                        }

                }
                setUpRecyclerViewData(unitAreaComponents!!.map { it!!.name } as MutableList<String?>)

            }catch (e: Exception){
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUpRecyclerViewData(areaComponentsData :MutableList<String?>){

    val componentAdapter = UnitPlanDetailComponentlAdapter(areaComponentsData)
        unit_plan_details_recycler_view.adapter = componentAdapter
        unit_plan_details_recycler_view.layoutManager = GridLayoutManager(context, 2)
        unit_plan_details_recycler_view.setHasFixedSize(false)
    }

}




