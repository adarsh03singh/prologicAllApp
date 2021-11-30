package com.csestateconnect.ui.navdrawer.clients


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentAddClientsBinding
import com.csestateconnect.databinding.FragmentEditClientsDetailBinding
import com.csestateconnect.db.data.countries.City
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.countries.Location
import com.csestateconnect.viewmodel.ClientsViewModel
import java.lang.IndexOutOfBoundsException

/**
 * A simple [Fragment] subclass.
 */
class EditClientsDetailFragment : Fragment() {

    var clientPreviousId: Int? = null
    lateinit var viewModel: ClientsViewModel
    var countryIndex: Int = 0
    var cityIndex: Int = 1
    var countryDropDown: AutoCompleteTextView? = null
    var cityDropDown: AutoCompleteTextView? = null
    var locationDropDown: AutoCompleteTextView? = null
    lateinit var countryListData: List<Countries>
    var cityData: List<City>? = null
    var locationData: List<Location>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_clients_detail, container, false)

        val binding: FragmentEditClientsDetailBinding = FragmentEditClientsDetailBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)
        binding.editClientViewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.getCountriesData().observe(this, Observer<List<Countries>> { countryData ->
             var locationList:  List<Location>?  = null

            for (i in 0..countryData.size - 1) {
               val ct=  countryData.get(i).cities
                for (i in 0..ct.size - 1) {

                }

                val countryid = countryData.map { it.id.toString() }
                val countryName = countryData.map { it.name }
                setUpcountryDropDown(countryid, countryName)
            }
            countryListData = countryData

            try {
                val sharedPref: SharedPreferences = context!!.getSharedPreferences("PREFERENCE_NAME", 0)
                val countryIndex = sharedPref.getInt("countryIndex", 0)
                val cityIndex =  sharedPref.getInt("cityIndex", 0)

                cityData = countryListData.get(countryIndex).cities
                setUpcityDropDown( cityData!!)
                locationData = cityData!!.get(cityIndex).locations
                setUpLocationDropDown( locationData!!)

            }catch (e: Exception){
                e.printStackTrace()
            }


        })
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref: SharedPreferences = context!!.getSharedPreferences("PREFERENCE_NAME", 0)
        clientPreviousId = sharedPref.getInt("clientList_id", 0)

        viewModel.clientEditId.value = clientPreviousId
        countryDropDown = view.findViewById(R.id.edCountry_autocompleteView) as AutoCompleteTextView
        cityDropDown = view.findViewById(R.id.edCity_autocompleteView) as AutoCompleteTextView
        locationDropDown = view.findViewById(R.id.edLocation_autocompleteView) as AutoCompleteTextView



    }

    @SuppressLint("ClickableViewAccessibility")
    fun setUpcountryDropDown(numberId: List<String>, countryName: List<String>){

        // Country Spinner
        if (countryDropDown != null) {
            val countryAdapter = ArrayAdapter(context!!, R.layout.autocomplete_textview_dropdown_items,
                R.id.autoTextView_item_id,
                countryName
            )

            countryDropDown!!.setAdapter(countryAdapter)
            countryDropDown!!.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
            object : View.OnTouchListener {
                override fun onTouch(
                    paramView: android.view.View?,
                    paramMotionEvent: MotionEvent?
                ): kotlin.Boolean {
                    if (countryName.size > 0) {
                        // show all suggestions
                        if (!countryDropDown!!.getText().toString().equals("")) countryAdapter.getFilter()
                            .filter(null)
                        countryDropDown!!.showDropDown()
                    }
                    return false
                }
            })

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

    @SuppressLint("ClickableViewAccessibility")
    fun setUpcityDropDown(cityData: List<City>) {
        // City Spinner
        val cityListFromCountry = cityData.map { it.name }
        val cityid  = cityData.map { it.id }

        val cityAdapter = ArrayAdapter(context!!, R.layout.autocomplete_textview_dropdown_items,
            R.id.autoTextView_item_id,
            cityListFromCountry
        )

        cityDropDown!!.setAdapter(cityAdapter)
        cityDropDown!!.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(
                paramView: android.view.View?,
                paramMotionEvent: MotionEvent?
            ): kotlin.Boolean {
                if (cityListFromCountry.size > 0) {
                    // show all suggestions
                    if (!cityDropDown!!.getText().toString().equals("")) cityAdapter.getFilter()
                        .filter(null)
                    cityDropDown!!.showDropDown()
                }
                return false
            }
        })

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

                    setUpLocationDropDown( locatData)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setUpLocationDropDown(locationData: List<Location>) {
        // Location Spinner
        val locationListFromCity = locationData.map { it.name }
        val locationid  = locationData.map { it.id }

        val cityAdapter = ArrayAdapter(context!!, R.layout.autocomplete_textview_dropdown_items,
            R.id.autoTextView_item_id,
            locationListFromCity
        )

        locationDropDown!!.setAdapter(cityAdapter)
        locationDropDown!!.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(
                paramView: android.view.View?,
                paramMotionEvent: MotionEvent?
            ): kotlin.Boolean {
                if (locationListFromCity.size > 0) {
                    // show all suggestions
                    if (!locationDropDown!!.getText().toString().equals("")) cityAdapter.getFilter()
                        .filter(null)
                    locationDropDown!!.showDropDown()
                }
                return false
            }
        })

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
