package com.csestateconnect.ui.home.lead_Frags


import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.csestateconnect.DecimalDigitsInputFilter
import com.csestateconnect.R
import com.csestateconnect.adapters.lead_create_adapters.LeadCompletionStatusAdapter
import com.csestateconnect.adapters.lead_create_adapters.LeadPropertyTypeAdapter
import com.csestateconnect.adapters.lead_create_adapters.LeadSelectLocationAdapter
import com.csestateconnect.databinding.FragmentCreateLeadBinding
import com.csestateconnect.db.data.GetCompletionStatus
import com.csestateconnect.db.data.GetPreferredProperty
import com.csestateconnect.db.data.countries.City
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.countries.Location
import com.csestateconnect.db.data.leads.LeadStatus
import com.csestateconnect.viewmodel.LeadsViewModel
import kotlinx.android.synthetic.main.fragment_create_lead.*
import java.util.regex.Pattern


class CreateLeadFragment : Fragment() {
    lateinit var viewModel: LeadsViewModel
    lateinit var countryListData: List<Countries>
//    lateinit var cityListData: List<City>
    var preferredPropertyList : List<GetPreferredProperty>? = null
    var constructionStatusList : List<GetCompletionStatus>? = null
    var selectLocationAllDataList : List<Location>? = null

    var selectedCountry = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_lead, container, false)
        val binding: FragmentCreateLeadBinding = FragmentCreateLeadBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(LeadsViewModel::class.java)
        binding.createLeadVm = viewModel
        binding.lifecycleOwner = this



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPreferredPropertyData().observe(this, androidx.lifecycle.Observer<List<GetPreferredProperty>>
        { prefPropretyStatus ->
            preferredPropertyList = prefPropretyStatus
            setUpPrefPropertySpinner(prefPropretyStatus.map { it.name } as MutableList<String>, listOf())
        } )

        viewModel.getCompletionStatusData().observe(this, androidx.lifecycle.Observer<List<GetCompletionStatus>>
        { completionStatus ->
            constructionStatusList = completionStatus
            setUpCompletionSpinner(completionStatus.map { it.name } as MutableList<String>, listOf())
        } )

        viewModel.getCountriesData().observe(this, androidx.lifecycle.Observer<List<Countries>>
        { countryData ->

            for (i in 0..countryData.size - 1) {
                val numberCode = countryData.map { it.number_code }
                val countryName = countryData.map { it.name }

                setUpCountrySpinner(numberCode, countryName)
            }
            countryListData = countryData
//            cityListData = countryData.map { it.cities }
        })

        viewModel.createLeadCountry.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                val index = countryListData.map { it.name }.indexOf(it)
                viewModel.countryId = countryListData.get(index).id
            }
        })

        viewModel.createLeadCity.observe(this, Observer {city ->
            if (!city.isNullOrEmpty()){

                val countryidList = countryListData.map { it.id }
                val countryIndex = countryidList.indexOf(viewModel.countryId)
                val citydata = countryListData.get(countryIndex).cities
                val cityIndex = citydata.map { it.name }.indexOf(city)
                viewModel.cityId = citydata.get(cityIndex).id
            }
        })

        setUpBudgetSpinner()

        // Lead Status Spinner

        val status: LeadStatus = LeadStatus(6, "Fresh", "#d6d6d6")
        val status2: LeadStatus = LeadStatus(7, "Contacted", "#0096ff")
        val status3: LeadStatus = LeadStatus(8, "Interested", "#fffb00")
        val status4: LeadStatus = LeadStatus(9, "Not Interested", "#ff2600")
        val status5: LeadStatus = LeadStatus(10, "Follow Up", "#73fcd6")
        val status6: LeadStatus = LeadStatus(11, "Site Visit", "#009193")
        val status7: LeadStatus = LeadStatus(12, "Closed / Won", "8efa00")

        val statusList = listOf(status.name, status2.name, status3.name, status4.name, status5.name,
            status6.name, status7.name)

        val statusId = listOf(status.id, status2.id, status3.id, status4.id, status5.id, status6.id, status7.id)

        val statusSpinner = view.findViewById<AutoCompleteTextView>(R.id.createlead_status_text)
        if (statusSpinner != null) {
            val adapter = ArrayAdapter(
                view.context,
                R.layout.autocomplete_textview_dropdown_items, R.id.autoTextView_item_id, statusList
            )
            statusSpinner.setAdapter(adapter)
        }

        statusSpinner.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (view as TextView).text = null
//                status_text.setText(parent.getItemAtPosition(position).toString())

                val index = statusList.indexOf(parent.getItemAtPosition(position).toString())
                val leadstatusid = statusId.get(index)
                viewModel.createLeadStatus.value = leadstatusid
            }
        })

    }

    // Property Spinner

    val propertyList: MutableList<String> = mutableListOf()
    fun setUpPrefPropertySpinner(preferredList: MutableList<String>, mergeList: List<String>) {

        preferredList.addAll(mergeList)

        val propertySpinner = view!!.findViewById<AutoCompleteTextView>(R.id.property_type_text)
        if (propertySpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                R.layout.autocomplete_textview_dropdown_items, R.id.autoTextView_item_id, preferredList
            )
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//            propertySpinner.adapter = adapter
            propertySpinner.setAdapter(adapter)
        }

        propertySpinner.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (view as TextView).text = null
//                property_type_text.setText(parent.getItemAtPosition(position).toString())

                property_type_text.text = null

                propertyList.add(parent.getItemAtPosition(position).toString())

                var cancelList = listOf<String>()
                // Property Cross Recycler
                val leadRecyclerViewAdapter =
                    LeadPropertyTypeAdapter(propertyList){
                            it -> cancelList = it
                        setUpPrefPropertySpinner(preferredList, cancelList)
                    }
                property_recycler_view.adapter = leadRecyclerViewAdapter
                property_recycler_view.layoutManager = GridLayoutManager(context,3,
                    LinearLayoutManager.VERTICAL, false)
                // changed the value down to false //
                property_recycler_view.setHasFixedSize(false)

                preferredList.remove(parent.getItemAtPosition(position).toString())
                setUpPrefPropertySpinner(preferredList, cancelList)
            }
        })

        val allPrefName = preferredPropertyList?.map { it.name }
        val selectedIdsList : MutableList<Int> = mutableListOf()
        for (i in 0..propertyList.size - 1) {
            selectedIdsList.add(preferredPropertyList!!.get(allPrefName!!.indexOf(propertyList.get(i))).id)
        }
        viewModel.createLeadPropertyType.value = selectedIdsList
    }

    // Construction/ Completion/ Preffered Status Spinner

    val completionList: MutableList<String> = mutableListOf()
    fun setUpCompletionSpinner(constructionList: MutableList<String>, mergeList: List<String>) {

        constructionList.addAll(mergeList)

        val constructionSpinner = view!!.findViewById<AutoCompleteTextView>(R.id.construction_status_text)
        if (constructionSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                R.layout.autocomplete_textview_dropdown_items, R.id.autoTextView_item_id, constructionList
            )
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//            constructionSpinner.adapter = adapter
            constructionSpinner.setAdapter(adapter)
        }


        constructionSpinner.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (view as TextView).text = null
//                construction_status_text.setText(parent.getItemAtPosition(position).toString())

                construction_status_text.text = null

                completionList.add(parent.getItemAtPosition(position).toString())

                var cancelList = listOf<String>()
                // Completion Cross Recycler
                val leadRecyclerViewAdapter =
                    LeadCompletionStatusAdapter(completionList){
                            it -> cancelList = it
                        setUpCompletionSpinner(constructionList, cancelList)
                    }
                completion_recycler_view.adapter = leadRecyclerViewAdapter
                completion_recycler_view.layoutManager = GridLayoutManager(context,3,
                    LinearLayoutManager.VERTICAL, false)
                // changed the value down to false //
                completion_recycler_view.setHasFixedSize(false)

                constructionList.remove(parent.getItemAtPosition(position).toString())
                setUpCompletionSpinner(constructionList, cancelList)
            }
        })

        val allComStatusName = constructionStatusList?.map { it.name }
        val selectedIdsList : MutableList<Int> = mutableListOf()
        for (i in 0..completionList.size - 1) {
            selectedIdsList.add(constructionStatusList!!.get(allComStatusName!!.indexOf(completionList.get(i))).id)
        }
        viewModel.createLeadPreferredStatus.value = selectedIdsList
    }

    
    fun setUpCountrySpinner(numberCode: List<String>, countryName: List<String>) {

        // Country Code Spinner
        val countryCodeSpinner = view!!.findViewById<Spinner>(R.id.country_code_spinner)
        if (countryCodeSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_item, numberCode
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            countryCodeSpinner.adapter = adapter
        }

        countryCodeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                viewModel.createLeadNumberCode.value = parent.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        // Country Spinner
        val countrySpinner = view!!.findViewById<AutoCompleteTextView>(R.id.country_text)
        if (countrySpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                R.layout.autocomplete_textview_dropdown_items,
                R.id.autoTextView_item_id,
                countryName
            )
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//            countrySpinner.adapter = adapter
            countrySpinner.setAdapter(adapter)
        }

        countrySpinner!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (view as TextView).text = null
//                country_text.setText(parent.getItemAtPosition(position).toString())
                city_text.text = null
                select_location_text.text = null

                selectedCountry.value = country_text.text.toString()

                try {
                    var countryIndex: Int = 0
                    var cityData : List<City>? = null

                    countryIndex = countryName.indexOf(selectedCountry.value!!)

                    cityData = countryListData.get(countryIndex).cities

                    setUpCitySpinner(countryIndex, cityData)

                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })

    }

    fun setUpCitySpinner(countryIndex: Int, cityData: List<City>) {
        // City Spinner
        val citySpinner = view!!.findViewById<AutoCompleteTextView>(R.id.city_text)
        val cityListFromCountry = cityData.map { it.name }

        if (citySpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                R.layout.autocomplete_textview_dropdown_items, R.id.autoTextView_item_id, cityListFromCountry
            )
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//            citySpinner.adapter = adapter
            citySpinner.setAdapter(adapter)
        }

        citySpinner.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (view as TextView).text = null
//                city_text.setText(parent.getItemAtPosition(position).toString())
                select_location_text.text = null

                val selectedCity = city_text.text.toString()

                try {
                    var cityIndex: Int = 0
                    var selectLocationData : List<Location>? = null

                    cityIndex = cityListFromCountry.indexOf(selectedCity)

                    selectLocationData = cityData.get(cityIndex).locations

                    val locationList: MutableList<String> = (selectLocationData.map { it.name }).toMutableList()

                    selectLocationAllDataList = selectLocationData
                    setUpSelectLocationSpinner(locationList, listOf())

                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })
    }

    val selectLocationList: MutableList<String> = mutableListOf()
    fun setUpSelectLocationSpinner(locationList: MutableList<String>, mergeList: List<String>) {

        locationList.addAll(mergeList)

        // Select Location Spinner
        val locationSpinner = view!!.findViewById<AutoCompleteTextView>(R.id.select_location_text)
        if (locationSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                R.layout.autocomplete_textview_dropdown_items, R.id.autoTextView_item_id, locationList
            )
            locationSpinner.setAdapter(adapter)

        }

        locationSpinner.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
//                (view as AutoCompleteTextView).text = null
                select_location_text.text = null

                selectLocationList.add(parent.getItemAtPosition(position).toString())

                var cancelList = listOf<String>()
                // Cross Recycler Select Location
                val leadRecyclerViewAdapter =
                    LeadSelectLocationAdapter(selectLocationList){
                        it -> cancelList = it
                        setUpSelectLocationSpinner(locationList, cancelList)
                    }
                locations_recyclerview.adapter = leadRecyclerViewAdapter
                locations_recyclerview.layoutManager = GridLayoutManager(context,3,
                    LinearLayoutManager.VERTICAL, false)
                // changed the value down to false //
                locations_recyclerview.setHasFixedSize(false)

                locationList.remove(parent.getItemAtPosition(position).toString())
                setUpSelectLocationSpinner(locationList, cancelList)
            }
        })

        val allComStatusName = selectLocationAllDataList?.map { it.name }
        val selectedIdsList : MutableList<Int> = mutableListOf()
        for (i in 0..selectLocationList.size - 1) {
            selectedIdsList.add(selectLocationAllDataList!!.get(allComStatusName!!.indexOf(selectLocationList.get(i))).id)
        }
        viewModel.createLeadPreferredLocation.value = selectedIdsList
    }

    fun setUpBudgetSpinner(){
        val budgetCurrencyList =  listOf("Cr", "Lac", "K", "INR")

        // Minimum Budget Spinner
        val minimumSpinner = view!!.findViewById<Spinner>(R.id.min_budget_spinner)
        if (minimumSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_dropdown_item, budgetCurrencyList
            )
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            minimumSpinner.adapter = adapter
        }

        minimumSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                (view as TextView).text = null
                min_budget_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2)))
                viewModel.budgetMinimumCurrency = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        // Maximum Budget Spinner
        val maximumSpinner = view!!.findViewById<Spinner>(R.id.max_budget_spinner)
        if (maximumSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_dropdown_item, budgetCurrencyList
            )
            maximumSpinner.adapter = adapter
        }

        maximumSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                (view as TextView).text = null
                max_budget_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2)))
                viewModel.budgetMaximumCurrency = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        // Currency Spinner
        val currencyList = listOf("INR")
        val currencySpinner = view!!.findViewById<Spinner>(R.id.currency_spinner)
        if (currencySpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_item, currencyList
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            currencySpinner.adapter = adapter
        }

        currencySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                (view as TextView).text = null
                currency_text.setText(parent.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

}

