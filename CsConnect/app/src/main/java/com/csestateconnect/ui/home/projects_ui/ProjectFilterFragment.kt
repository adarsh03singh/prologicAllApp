package com.csestateconnect.ui.home.projects_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appyvet.materialrangebar.RangeBar

import com.csestateconnect.R
import com.csestateconnect.adapters.DealFilterStatusAdapter
import com.csestateconnect.adapters.projectAdapters.project_filter_adapters.*
import com.csestateconnect.databinding.FragmentProjectFilterBinding
import com.csestateconnect.db.data.projects.*
import com.csestateconnect.viewmodel.ProjectsViewModel
import kotlinx.android.synthetic.main.fragment_project_filter.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ProjectFilterFragment : Fragment() {


    var propertyRecyclerViewAdapter : ProjectFilterPropertyTypeAdapter? = null
    var locationRecyclerViewAdapter : ProjectFilterLocationAdapter? = null
    var statusRecyclerViewAdapter : ProjectFilterProjectStatusAdapter? = null
    var amenitiesRecyclerViewAdapter : ProjectFilterAmenitiesAdapter? = null
    var unitPlanRecyclerViewAdapter : ProjectFilterUnitPlanAdapter? = null

    var price_rangebar: RangeBar? = null
    var sq_foot_price_rangebar: RangeBar? = null

//    val forResetList: Facets? = null
    private var mSearchFilter: Facets? = null
    lateinit var viewModel: ProjectsViewModel
    private val c = arrayOf("T", "L", "Cr", "Ar")

    private fun priceFormat(n: Double, iteration: Int): String {
        val d: Double
        if (iteration == 0) {
            d = n.toLong() / 100 / 10.0
        } else {
            d = n.toLong() / 10 / 10.0
        }
        val isRound =
            d * 10 % 10 == 0.0//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return if (d < 100) (if (d > 99.9 || isRound || !isRound && d > 9.9) d.toInt() * 10 / 10 else d.toString() + "").toString() + "" + c[iteration] else priceFormat(
            d,
            iteration + 1
        )

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_filter, container, false)

        val binding: FragmentProjectFilterBinding = FragmentProjectFilterBinding.bind(view)
        viewModel =
            ViewModelProviders.of(this.requireParentFragment()).get(ProjectsViewModel::class.java)
        binding.projFilterViewmodel = viewModel
        binding.lifecycleOwner = this


        viewModel.getAllProjectData().observe(this, Observer<List<Projectsdata>> { dataList ->
            try {
                val facetsData = dataList.get(0).facets!!

//                setUpProjectPropertyType(facetsData)

                mSearchFilter = facetsData

                setUpProjectLocation(facetsData)
                setUpProjectAmenities(facetsData)
                setUpProjectStatus(facetsData)
                setUpProjectUnitPlan(facetsData)
                setUpProjectPrice(facetsData)
                setUpPerSqFootPriceRangeBar(facetsData)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        price_rangebar = view.findViewById(R.id.filter_total_price_rangebar)
        sq_foot_price_rangebar = view.findViewById(R.id.per_sq_foot_price_rangebar)

        viewModel.clearButtonClicked.observe(this, androidx.lifecycle.Observer { it ->
            if (it){
                locationRecyclerViewAdapter?.notifyDataSetChanged()
                statusRecyclerViewAdapter?.notifyDataSetChanged()
                amenitiesRecyclerViewAdapter?.notifyDataSetChanged()
                unitPlanRecyclerViewAdapter?.notifyDataSetChanged()

                val priceList = mSearchFilter!!.filterPrice!!.price!!.buckets!!
                val minPriceFilter = priceList.get(0)!!.key
                val maxPriceFilter = priceList.get(priceList.size - 1)!!.key

                selected_min_total_price.text = minPriceFilter.toString()
                selected_max_total_price.text = maxPriceFilter.toString()
//         price_rangebar = view.findViewById(R.id.filter_total_price_rangebar)
//        sq_foot_price_rangebar

                price_rangebar!!.setTickEnd(maxPriceFilter!!.toFloat())
                price_rangebar!!.setTickStart(minPriceFilter!!.toFloat())


                val perSqFeetpriceList = mSearchFilter!!.filterBsp!!.bsp!!.buckets!!
                val minSqFeetPriceFilter = perSqFeetpriceList.get(0)!!.key
                val maxSqFeetPriceFilter = perSqFeetpriceList.get(perSqFeetpriceList.size - 1)!!.key

                selected_min_per_sq_foot_price.text = minSqFeetPriceFilter.toString()
                selected_max_per_sq_foot_price.text = maxSqFeetPriceFilter.toString()

                sq_foot_price_rangebar!!.setTickEnd(maxSqFeetPriceFilter!!.toFloat())
                sq_foot_price_rangebar!!.setTickStart(minSqFeetPriceFilter!!.toFloat())
//                resetProjectPrices()

                viewModel.clearButtonClicked.value = false
            }
        })

    }

    //  Project Property Type adapter and click listeners
    fun setUpProjectPropertyType(facetsData: Facets) {

        val propertyData = ArrayList<BucketXXXXXX?>()
        for (i in 0..facetsData.filterProperty!!.property!!.buckets!!.size - 1) {
            propertyData.add(facetsData.filterProperty!!.property!!.buckets!!.get(i))
        }

        val propertyList = mutableListOf<String>()
         propertyRecyclerViewAdapter =
            ProjectFilterPropertyTypeAdapter(propertyData) { selectedPropertyData ->
                propertyList.add(selectedPropertyData)
                viewModel.selectedProjectsProprtyList.value = propertyList
            }

        property_type_recycler_view.adapter = propertyRecyclerViewAdapter
        property_type_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        property_type_recycler_view.setHasFixedSize(true)

        proprety_drop_down.setOnClickListener {
            if (property_recycler_view_layout.visibility == View.GONE) {
                property_recycler_view_layout.visibility = View.VISIBLE
                proprety_drop_down.visibility = View.GONE
                property_drop_close.visibility = View.VISIBLE
            } else {
                property_recycler_view_layout.visibility = View.GONE
                proprety_drop_down.visibility = View.VISIBLE
                property_drop_close.visibility = View.GONE
            }
        }

        property_drop_close.setOnClickListener {
            if (property_recycler_view_layout.visibility == View.VISIBLE) {
                property_recycler_view_layout.visibility = View.GONE
                proprety_drop_down.visibility = View.VISIBLE
                property_drop_close.visibility = View.GONE
            }

        }
    }

    //  Project Location adapter and click listeners
    fun setUpProjectLocation(facetsData: Facets) {

        val locations = ArrayList<BucketXXXX?>()
        for (i in 0..facetsData.filterLocations!!.locations!!.buckets!!.size - 1) {
            locations.add(facetsData.filterLocations.locations!!.buckets!!.get(i))
        }

        val locationLFList = mutableListOf<String>()
         locationRecyclerViewAdapter =
            ProjectFilterLocationAdapter(locations, viewModel.selectedProjectsLocationId.value)
            { selectedLocation ->
                locationLFList.add(selectedLocation)
                viewModel.selectedProjectsLocationId.value = locationLFList
            }



        projFilter_location_recycler_view.adapter = locationRecyclerViewAdapter
        projFilter_location_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        projFilter_location_recycler_view.setHasFixedSize(true)

        location_drop_down.setOnClickListener {
            if (location_recycler_view_layout.visibility == View.GONE) {
                location_recycler_view_layout.visibility = View.VISIBLE
                location_drop_down.visibility = View.GONE
                location_drop_close.visibility = View.VISIBLE
            } else {
                location_recycler_view_layout.visibility = View.GONE
                location_drop_down.visibility = View.VISIBLE
                location_drop_close.visibility = View.GONE
            }
        }

        location_drop_close.setOnClickListener {
            if (location_recycler_view_layout.visibility == View.VISIBLE) {
                location_recycler_view_layout.visibility = View.GONE
                location_drop_down.visibility = View.VISIBLE
                location_drop_close.visibility = View.GONE
            }

        }
    }

    //  Project Amenities adapter and click listeners
    fun setUpProjectAmenities(facetsData: Facets) {

        val amenities = ArrayList<Bucket?>()
        for (i in 0..facetsData.filterAmenities!!.amenities!!.buckets!!.size - 1) {
            amenities.add(facetsData.filterAmenities.amenities!!.buckets!!.get(i))
        }

        val amentyList = mutableListOf<String>()
         amenitiesRecyclerViewAdapter =
            ProjectFilterAmenitiesAdapter(amenities!!, viewModel.selectedProjectsAmenitiesId.value) { selectedAmenities ->
                amentyList.add(selectedAmenities)
                viewModel.selectedProjectsAmenitiesId.value = amentyList
            }
        projFilter_amenities_recycler_view.adapter = amenitiesRecyclerViewAdapter
        projFilter_amenities_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        projFilter_amenities_recycler_view.setHasFixedSize(true)

        amenities_drop_down.setOnClickListener {
            if (amenities_recycler_view_layout.visibility == View.GONE) {
                amenities_recycler_view_layout.visibility = View.VISIBLE
                amenities_drop_down.visibility = View.GONE
                amenities_drop_close.visibility = View.VISIBLE
            } else {
                amenities_recycler_view_layout.visibility = View.GONE
                amenities_drop_down.visibility = View.VISIBLE
                amenities_drop_close.visibility = View.GONE
            }
        }

        amenities_drop_close.setOnClickListener {
            if (amenities_recycler_view_layout.visibility == View.VISIBLE) {
                amenities_recycler_view_layout.visibility = View.GONE
                amenities_drop_down.visibility = View.VISIBLE
                amenities_drop_close.visibility = View.GONE
            }

        }
    }

    //  Project Status adapter and click listeners
    fun setUpProjectStatus(facetsData: Facets) {

        val projectStatus = ArrayList<BucketXXXXXXXX?>()
        for (i in 0..facetsData.filterStatus!!.status!!.buckets!!.size - 1) {
            projectStatus.add(facetsData.filterStatus.status!!.buckets!!.get(i))
        }
        val statusList = mutableListOf<String>()
         statusRecyclerViewAdapter =
            ProjectFilterProjectStatusAdapter(projectStatus , viewModel.selectedProjectsStatusId.value) { selectedLeadId ->
                statusList.add(selectedLeadId)
                viewModel.selectedProjectsStatusId.value = statusList
            }

        projFilter_status_recycler_view.adapter = statusRecyclerViewAdapter
        projFilter_status_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        projFilter_status_recycler_view.setHasFixedSize(true)

        project_status_drop_down.setOnClickListener {
            if (project_status_recycler_view_layout.visibility == View.GONE) {
                project_status_recycler_view_layout.visibility = View.VISIBLE
                project_status_drop_down.visibility = View.GONE
                project_status_drop_close.visibility = View.VISIBLE
            } else {
                project_status_recycler_view_layout.visibility = View.GONE
                project_status_drop_down.visibility = View.VISIBLE
                project_status_drop_close.visibility = View.GONE
            }
        }

        project_status_drop_close.setOnClickListener {
            if (project_status_recycler_view_layout.visibility == View.VISIBLE) {
                project_status_recycler_view_layout.visibility = View.GONE
                project_status_drop_down.visibility = View.VISIBLE
                project_status_drop_close.visibility = View.GONE
            }

        }

    }

    //  Project Unit Plan adapter and click listeners
    fun setUpProjectUnitPlan(facetsData: Facets) {

        val projectUnitPlan = ArrayList<BucketXX?>()
        for (i in 0..facetsData.filterBhk!!.bhk!!.buckets!!.size - 1) {
            projectUnitPlan.add(facetsData.filterBhk!!.bhk!!.buckets!!.get(i))
        }

        val unitBhkList = mutableListOf<String>()
         unitPlanRecyclerViewAdapter =
            ProjectFilterUnitPlanAdapter(projectUnitPlan, viewModel.selectedProjectsUnitPlanId.value) { selectedBhk ->
                unitBhkList.add(selectedBhk)
                viewModel.selectedProjectsUnitPlanId.value = unitBhkList
            }

        projFilter_unit_plan_recycler_view.adapter = unitPlanRecyclerViewAdapter
        projFilter_unit_plan_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        projFilter_unit_plan_recycler_view.setHasFixedSize(true)

        unit_plan_drop_down.setOnClickListener {
            if (unit_plan_recycler_view_layout.visibility == View.GONE) {
                unit_plan_recycler_view_layout.visibility = View.VISIBLE
                unit_plan_drop_down.visibility = View.GONE
                unit_plan_drop_close.visibility = View.VISIBLE
            } else {
                unit_plan_recycler_view_layout.visibility = View.GONE
                location_drop_down.visibility = View.VISIBLE
                location_drop_close.visibility = View.GONE
            }
        }

        unit_plan_drop_close.setOnClickListener {
            if (unit_plan_recycler_view_layout.visibility == View.VISIBLE) {
                unit_plan_recycler_view_layout.visibility = View.GONE
                unit_plan_drop_down.visibility = View.VISIBLE
                unit_plan_drop_close.visibility = View.GONE
            }

        }


    }

    //  Project price plan
    fun setUpProjectPrice(facetsData: Facets) {

        val priceList = facetsData.filterPrice!!.price!!.buckets!!
        val minPriceFilter = priceList.get(0)!!.key
        val maxPriceFilter = priceList.get(priceList.size - 1)!!.key

        selected_min_total_price.text = minPriceFilter.toString()
        selected_max_total_price.text = maxPriceFilter.toString()

        price_rangebar!!.setTickInterval(100000f)
        price_rangebar!!.setTickEnd(maxPriceFilter!!.toFloat())
        price_rangebar!!.setTickStart(minPriceFilter!!.toFloat())

        price_rangebar!!.setPinTextFormatter(RangeBar.PinTextFormatter { value ->
            priceFormat(java.lang.Double.parseDouble(value), 0)

        })

        price_rangebar!!.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onRangeChangeListener(
                rangeBar: RangeBar,
                leftPinIndex: Int,
                rightPinIndex: Int,
                leftPinValue: String,
                rightPinValue: String
            ) {
                selected_min_total_price?.text = leftPinValue
                selected_max_total_price?.text = rightPinValue

                min_price_edittext.setText(leftPinValue)
                max_price_edittext.setText(rightPinValue)

                if (selected_min_total_price != null || selected_max_total_price != null) {

                    val leftValue =
                        (rangeBar.tickStart + leftPinIndex * rangeBar.tickInterval).toLong()
                    val rightValue =
                        (rangeBar.tickStart + rightPinIndex * rangeBar.tickInterval).toLong()

                    viewModel.filterMinPriceTxt.value = leftValue.toString()
                    viewModel.filterMaxPriceTxt.value = rightValue.toString()
                }


            }
        })

    }

    // Per Sq. Foot Price Range Bar
    fun setUpPerSqFootPriceRangeBar(facetsData: Facets) {

        val perSqFeetpriceList = facetsData.filterBsp!!.bsp!!.buckets!!
        val minPriceFilter = perSqFeetpriceList.get(0)!!.key
        val maxPriceFilter = perSqFeetpriceList.get(perSqFeetpriceList.size - 1)!!.key

        selected_min_per_sq_foot_price.text = minPriceFilter.toString()
        selected_max_per_sq_foot_price.text = maxPriceFilter.toString()

        sq_foot_price_rangebar!!.setTickInterval(100f)
        sq_foot_price_rangebar!!.setTickEnd(maxPriceFilter!!.toFloat())
        sq_foot_price_rangebar!!.setTickStart(minPriceFilter!!.toFloat())

        sq_foot_price_rangebar!!.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onRangeChangeListener(
                rangeBar: RangeBar,
                leftPinIndex: Int,
                rightPinIndex: Int,
                leftPinValue: String,
                rightPinValue: String
            ) {
                selected_min_per_sq_foot_price?.text = leftPinValue
                selected_max_per_sq_foot_price?.text = rightPinValue

                if (selected_min_per_sq_foot_price != null || selected_max_per_sq_foot_price != null) {
                    viewModel.filterMinPerSqFtPriceTxt.value =
                        selected_min_per_sq_foot_price?.text.toString()
                    viewModel.filterMaxPerSqFtPriceTxt.value =
                        selected_max_per_sq_foot_price?.text.toString()
                }
            }
        })


    }

    fun resetProjectPrices() {

        val priceList = mSearchFilter!!.filterPrice!!.price!!.buckets!!
        val minPriceFilter = priceList.get(0)!!.key
        val maxPriceFilter = priceList.get(priceList.size - 1)!!.key

        selected_min_total_price.text = minPriceFilter.toString()
        selected_max_total_price.text = maxPriceFilter.toString()
//         price_rangebar = view.findViewById(R.id.filter_total_price_rangebar)
//        sq_foot_price_rangebar

        price_rangebar!!.setTickEnd(maxPriceFilter!!.toFloat())
        price_rangebar!!.setTickStart(minPriceFilter!!.toFloat())


        val perSqFeetpriceList = mSearchFilter!!.filterBsp!!.bsp!!.buckets!!
        val minSqFeetPriceFilter = perSqFeetpriceList.get(0)!!.key
        val maxSqFeetPriceFilter = perSqFeetpriceList.get(perSqFeetpriceList.size - 1)!!.key

        selected_min_per_sq_foot_price.text = minSqFeetPriceFilter.toString()
        selected_max_per_sq_foot_price.text = maxSqFeetPriceFilter.toString()

        sq_foot_price_rangebar!!.setTickEnd(maxSqFeetPriceFilter!!.toFloat())
        sq_foot_price_rangebar!!.setTickStart(minSqFeetPriceFilter!!.toFloat())
    }

}