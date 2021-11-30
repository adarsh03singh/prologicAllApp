package com.csestateconnect.ui.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.AllCitiesDataAdapter
import com.csestateconnect.databinding.FragmentAllCitiesBinding
import com.csestateconnect.databinding.FragmentHomeFrag1Binding
import com.csestateconnect.db.data.homedata.Brokers
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_all__cities.*
import kotlinx.android.synthetic.main.fragment_projects_frag2.*


/**
 * A simple [Fragment] subclass.
 */
class All_CitiesFragment : Fragment() {


    lateinit var viewModel: HomeViewModel
    var countValue: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_all__cities, container, false)

        val binding: FragmentAllCitiesBinding = FragmentAllCitiesBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.cityViewmodel = viewModel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         countValue = arguments?.getInt("cityPageCount")
        if(countValue != null){
            viewModel.countValue = countValue.toString()
            viewModel.setBrokerOrProjectUI(countValue.toString())

        }
        viewModel.getAllBrokersWithCity()
        setCityData()

    }


    fun setCityData(){
        Coroutines.main {
            var cityList: List<String>? = null
                if(viewModel.allBrokersDetails != null || viewModel.allProjectDetails != null) {

                    if(countValue.toString() == "0"){
                        val brokerCity = viewModel.allBrokersDetails
                         cityList = brokerCity!!.cities
                    }else if(countValue.toString() == "1"){
                        val projectCity = viewModel.allProjectDetails
                         cityList = projectCity!!.cities
                    }

                    val cityViewAdapter = AllCitiesDataAdapter(cityList){selectedName ->
                        viewModel.getBrokersAndProjectName(selectedName)
                    }
                    allCity_recyclerView.adapter = cityViewAdapter
                    allCity_recyclerView.layoutManager = LinearLayoutManager(
                        context,
                        RecyclerView.VERTICAL, false
                    )
                    allCity_recyclerView.setHasFixedSize(true)
                }

        }
    }
}
