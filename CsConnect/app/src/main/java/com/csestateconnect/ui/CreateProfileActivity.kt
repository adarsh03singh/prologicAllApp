package com.csestateconnect.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.ActivityCreateprofileBinding
import com.csestateconnect.db.data.countries.City
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_createprofile.*
import kotlinx.android.synthetic.main.fragment_create_lead.*


class CreateProfileActivity : AppCompatActivity(),
    ConnectivityReceiver.ConnectivityReceiverListener {

    var countryIndex: Int = 0
    var countryDropDown:AutoCompleteTextView? = null
    var cityDropDown:AutoCompleteTextView? = null

    var viewModel: ProfileViewModel? = null
    lateinit var countryListData: List<Countries>
    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityCreateprofileBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_createprofile
        )
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

         countryDropDown = findViewById(R.id.profile_country_dropdown) as AutoCompleteTextView
         cityDropDown = findViewById(R.id.profile_city_dropdown) as AutoCompleteTextView


        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        viewModel!!.getCountriesData().observe(this, Observer<List<Countries>> { countryData ->
            for (i in 0..countryData.size - 1) {
                val countryid = countryData.map { it.id.toString() }
                val countryName = countryData.map { it.name }
                setUpcountryDropDown(countryid, countryName)
            }
            countryListData = countryData
        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {

        if (!isConnected) {
            snackBar()

        } else {
            snackbar?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }


    private fun snackBar() {
        snackbar =
            Snackbar.make(createProfile_constraintView, "You are offline", Snackbar.LENGTH_LONG)
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        var view = snackbar!!.getView()
        view.setBackgroundColor(resources.getColor(R.color.colorchery))
        snackbar?.show()
    }

    fun setUpcountryDropDown(numberId: List<String>, countryName: List<String>){

        // Country Spinner
        if (countryDropDown != null) {
            val countryAdapter = ArrayAdapter(applicationContext, R.layout.autocomplete_textview_dropdown_items,
                R.id.autoTextView_item_id,
                countryName
            )
            countryDropDown!!.setAdapter(countryAdapter)
        }

        countryDropDown!!.setOnItemClickListener(object:AdapterView.OnItemClickListener {
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

            val cityAdapter = ArrayAdapter(applicationContext, R.layout.autocomplete_textview_dropdown_items,
                R.id.autoTextView_item_id,
                cityListFromCountry
            )

            cityDropDown!!.setAdapter(cityAdapter)

    cityDropDown!!.setOnItemClickListener(object:AdapterView.OnItemClickListener {
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
