package com.csestateconnect.ui.navdrawer.clients


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentAddClientsBinding
import com.csestateconnect.databinding.FragmentClientsListBinding
import com.csestateconnect.db.data.countries.City
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.countries.Location
import com.csestateconnect.viewmodel.ClientsViewModel

/**
 * A simple [Fragment] subclass.
 */
class AddClientsFragment : Fragment() {

    lateinit var viewModel: ClientsViewModel
    var countryIndex: Int = 0
    var cityIndex: Int = 1
    var countryDropDown:AutoCompleteTextView? = null
    var cityDropDown:AutoCompleteTextView? = null
    var locationDropDown:AutoCompleteTextView? = null
    lateinit var countryListData: List<Countries>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_clients, container, false)

        val binding: FragmentAddClientsBinding = FragmentAddClientsBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)
        binding.addClientViewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.getCountriesData().observe(this, Observer<List<Countries>> { countryData ->
            for (i in 0..countryData.size - 1) {
                val countryid = countryData.map { it.id.toString() }
                val countryName = countryData.map { it.name }
                setUpcountryDropDown(countryid, countryName)
            }
            countryListData = countryData
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryDropDown = view.findViewById(R.id.country_autocompleteView) as AutoCompleteTextView
        cityDropDown = view.findViewById(R.id.city_autocompleteView) as AutoCompleteTextView
        locationDropDown = view.findViewById(R.id.location_autocompleteView) as AutoCompleteTextView
    }
    fun setUpcountryDropDown(numberId: List<String>, countryName: List<String>){

        // Country Spinner
        if (countryDropDown != null) {
            val countryAdapter = ArrayAdapter(context, R.layout.autocomplete_textview_dropdown_items,
                R.id.autoTextView_item_id,
                countryName
            )
            countryDropDown!!.setAdapter(countryAdapter)
        }

        countryDropDown!!.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val countryNamePosition = parent!!.getItemAtPosition(position).toString()
                try {

                    val countryId = numberId.get(position)
                    var cityData: List<City>? = null

                    countryIndex = countryName.indexOf(countryNamePosition)

                    val sharedPreference = context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putInt("countryIndex",countryIndex)
                    editor.apply()
                    viewModel.countryIndexValue.value =  countryId.toInt()


                    cityData = countryListData.get(countryIndex).cities

                    setUpcityDropDown( cityData)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun setUpcityDropDown(cityData: List<City>) {
        // City Spinner
        val cityListFromCountry = cityData.map { it.name }
        val cityid  = cityData.map { it.id }

        val cityAdapter = ArrayAdapter(context!!, R.layout.autocomplete_textview_dropdown_items,
            R.id.autoTextView_item_id,
            cityListFromCountry
        )

        cityDropDown!!.setAdapter(cityAdapter)

        cityDropDown!!.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val cityNamePosition = parent!!.getItemAtPosition(position).toString()
                try {

                    val citId = cityid.get(position)
                    var locatData: List<Location>? = null

                    cityIndex = cityListFromCountry.indexOf(cityNamePosition)
                    val sharedPreference = context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putInt("cityIndex",cityIndex)
                    editor.apply()
                    viewModel.cityIndexValue.value =  citId


                    locatData = cityData.get(cityIndex).locations

                    setUpLocationDropDown(cityIndex, locatData)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

    fun setUpLocationDropDown(countryIndex: Int, locationData: List<Location>) {
        // Location Spinner
        val locationListFromCity = locationData.map { it.name }
        val locationid  = locationData.map { it.id }

        val cityAdapter = ArrayAdapter(context!!, R.layout.autocomplete_textview_dropdown_items,
            R.id.autoTextView_item_id,
            locationListFromCity
        )

        locationDropDown!!.setAdapter(cityAdapter)

        locationDropDown!!.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    val  Id = locationid.get(position)

                    viewModel.locationIndexValue.value =  Id


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }
}
