package com.csestateconnect.ui.navdrawer.bcp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentPersonalInfoBinding
import com.csestateconnect.db.data.countries.City
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.viewmodel.NavigationViewModel

/**
 * A simple [Fragment] subclass.
 */
class PersonalInfoFragment : Fragment() {

    var viewModel: NavigationViewModel? = null
    var countryIndex: Int = 0
    var countryDropDown: AutoCompleteTextView? = null
    var cityDropDown: AutoCompleteTextView? = null
    lateinit var countryListData: List<Countries>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_personal_info, container, false)
        val binding: FragmentPersonalInfoBinding = FragmentPersonalInfoBinding.bind(view)
         viewModel = ViewModelProviders.of(this).get(NavigationViewModel::class.java)
        binding.personalViewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel!!.tabItem.observe(this, Observer { tab ->
               // No need for 0th and 2nd position
            if (tab == 2) {
                findNavController().navigate(R.id.action_personalInfoFragment_to_verificationFragment)
                viewModel!!.tabItem.value = 1
            }
        })

        countryDropDown = view.findViewById(R.id.info_country_dropdown) as AutoCompleteTextView
        cityDropDown =  view.findViewById(R.id.info_city_dropdown) as AutoCompleteTextView

        viewModel!!.getCountriesData().observe(this, Observer<List<Countries>> { countryData ->
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

        val count = arguments?.getInt("profileCount")
        if (count != null){
            viewModel!!.profileCountValue.value = count
        }


    }

    fun setUpcountryDropDown(numberId: List<String>, countryName: List<String>){

        // Country Spinner
        if (countryDropDown != null) {
            val countryAdapter = ArrayAdapter(view!!.context, R.layout.autocomplete_textview_dropdown_items,
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
                    viewModel!!.countryIndexValue.value =  countryId.toInt()


                    cityData = countryListData.get(countryIndex).cities

                    setUpcityDropDown(countryIndex, cityData)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun setUpcityDropDown(countryIndex: Int, cityData: List<City>) {
        // City Spinner
        val cityListFromCountry = cityData.map { it.name }
        val cityid  = cityData.map { it.id }

        val cityAdapter = ArrayAdapter(view!!.context, R.layout.autocomplete_textview_dropdown_items,
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
                try {
                    val  Id = cityid.get(position)

                    viewModel!!.cityIndexValue.value =  Id


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }

}
