package com.csestateconnect.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.adapters.HomeProjectSlideAdapter
import com.csestateconnect.databinding.FragmentHomeFrag1Binding
import com.csestateconnect.db.data.homedata.ProjectDetail
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_home_frag1.*
import kotlinx.coroutines.runBlocking

class HomeFrag1 : Fragment() {

    lateinit var projectPagerAdapter: HomeProjectSlideAdapter

    lateinit var viewPager: ViewPager
    lateinit var tabLayoutProjects: TabLayout
    lateinit var tabLayoutBrokers: TabLayout
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(com.csestateconnect.R.layout.fragment_home_frag1, container, false)

        val binding: FragmentHomeFrag1Binding = FragmentHomeFrag1Binding.bind(view)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(HomeViewModel::class.java)
        binding.homeViewmodel = viewModel
        binding.lifecycleOwner = this

        // For top projects
        val layoutdelhi = view.findViewById(com.csestateconnect.R.id.layout_delhi) as ConstraintLayout
        tabLayoutProjects = view.findViewById<View>(com.csestateconnect.R.id.tabLayout_Projects) as TabLayout
        tabLayoutBrokers = view.findViewById(com.csestateconnect.R.id.tabLayout_Brokers)

//        loadHomeData()

        setTabDetails(1)
        setTabDetails(0)

        fun tab_selected() {

            when (tabLayoutProjects.selectedTabPosition) {
                0 -> {
                    viewModel.setTop5ProjectsData("Delhi NCR")
                }
                1 -> {
                    viewModel.setTop5ProjectsData("Mumbai")
                }
                2 -> {
                    viewModel.setTop5ProjectsData("Bangalore")
                }
                3 -> {
                    val count = 1
                   val bundle = bundleOf("cityPageCount" to count)
                    findNavController().navigate(R.id.action_navigation_home_to_all_CitiesFragment, bundle)
                }
            }
        }

        fun broker_tab_selected() {
            when (tabLayoutBrokers.selectedTabPosition) {
                0 -> {
                    viewModel.setTop5BrokersData("Delhi NCR")
                }
                1 -> {
                    viewModel.setTop5BrokersData("Mumbai")
                }
                2 -> {
                    viewModel.setTop5BrokersData("Bangalore")
                }
                3 -> {
                    val count = 0
                    val bundle = bundleOf("cityPageCount" to count)
                    findNavController().navigate(R.id.action_navigation_home_to_all_CitiesFragment, bundle)
                }
            }
        }

        tabLayoutProjects.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.i("tag", "Tab Select working")
                tab_selected()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
        tabLayoutBrokers.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.i("tag", "Tab Select working")
                broker_tab_selected()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

        viewModel.homePageBannerImage.observe(this, Observer { it ->
            if (it != null){

                val results = it.results
                if (!results.isNullOrEmpty()){
                    viewPager = view.findViewById(com.csestateconnect.R.id.project_view_pager) as ViewPager
                    projectPagerAdapter = HomeProjectSlideAdapter(context!!, results)
                    viewPager.adapter = projectPagerAdapter
                }
            }
        })

        viewModel.setProjectsDataHomePage.observe(this, Observer { it ->
            if (it == 1){
                val top5ProjectData = viewModel.top5ProjectData
                val cityName = viewModel.selectedCityNameForProjects
                val nullDataName =
                    listOf("Project Name", "Project Name", "Project Name", "Project Name", "Project Name")

                when (cityName) {
                    "Mumbai" -> {
                        val data = top5ProjectData!!.filter { it.city == "Mumbai" }
                        val name = data.map { it.name }
                        Coroutines.main {
//                                setImageViewResource()
                            if (!data.isNullOrEmpty()) {
                                if (data.size >= 5) {
                                    setTop5ProjectsDataNotNull(data)
                                } else {
                                    setTop5ProjectsDataNull()
                                }
                            } else {
                                setTop5ProjectsDataNull()
                            }
                        }
                    }

                    "Bangalore" -> {
                        val data = top5ProjectData!!.filter { it.city == "Bangalore" }
                        val name = data.map { it.name }
                        Coroutines.main {
                            if (!data.isNullOrEmpty()) {
                                if (data.size >= 5) {
                                    setTop5ProjectsDataNotNull(data)
                                } else {
                                    setTop5ProjectsDataNull()
                                }
                            } else {
                                setTop5ProjectsDataNull()
                            }
                        }
                    }

                    else -> {
                        val data = top5ProjectData!!
                        val name = data.map { it.name }
                        Coroutines.main {
                            if (!data.isNullOrEmpty()) {
                                if (data.size >= 5) {
                                    setTop5ProjectsDataNotNull(data)
                                } else {
                                    setTop5ProjectsDataNull()
                                }
                            } else {
                                setTop5ProjectsDataNull()
                            }
                        }
                    }
                }
            }
            else if (it == 0){
                setTop5ProjectsDataNull()
            }
        })

        if(isNetworkConnected(context!!))
        {
            viewModel.getProjectFromAPIAndStore()
        }
        else
        {
            Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        loadHomeData()
        viewModel.getChannelPartnersData(context!!)

    }

    fun loadHomeData(){
        viewModel.progressBarHomeLoad.value = View.VISIBLE

        viewModel.getHomeBanner(view)

        viewModel.setHomeCommission(view)

//        viewModel.setTop5BrokersData("Delhi NCR")

//        viewModel.setTop5ProjectsData("Delhi NCR")

//        viewModel.getHomeData()


        viewModel.progressBarHomeLoad.value = View.GONE
    }

    fun setTabDetails(tab_Number: Int){

        val image = arrayOf(
            com.csestateconnect.R.drawable.tabselected_delhi, com.csestateconnect.R.drawable.tabselected_mumbai,
            com.csestateconnect.R.drawable.tabselected_bangalore, com.csestateconnect.R.drawable.tabselected_allcities)
        val text = arrayOf("Delhi NCR", "Mumbai", "Bangalore", "All Cities")
        val customTab = arrayOf(com.csestateconnect.R.layout.custom_tab, com.csestateconnect.R.layout.custom_tab)
        val tabNumber = tab_Number

        for ( i in 0..3) {
            val view1 = layoutInflater.inflate(customTab[tabNumber], null)
            view1.findViewById<View>(com.csestateconnect.R.id.cityImage).setBackgroundResource(image[i])
            view1.findViewById<TextView>(com.csestateconnect.R.id.cityNameText).setText(text[i])
            if (tabNumber == 0)
                tabLayoutBrokers.addTab(tabLayoutBrokers.newTab().setCustomView(view1))
            else
                tabLayoutProjects.addTab(tabLayoutProjects.newTab().setCustomView(view1))
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun setTop5ProjectsDataNotNull(data: List<ProjectDetail>){
        val name = data.map { it.name }
        val address = data.map { it.city }
        val image = data.map { it.icon_image }

        for (i in name.indices){
            val idList = listOf<TextView>(projectNameMain, projectName2, projectName3, projectName4, projectName5)
            idList.get(i).setText(name.get(i))
        }

        for (i in address.indices){
            val idList = listOf<TextView>(projectAddressMain, projectAddress2, projectAddress3, projectAddress4, projectAddress5)
            idList.get(i).setText(address.get(i))
        }

        for (i in image.indices){
            val idList = listOf<ImageView>(centerBuildProjects, imageView2, imageView3, imageView4, imageView5)
            Glide.with(this).load(image.get(i)).into(idList.get(i))

        }
    }

    fun setTop5ProjectsDataNull(){
        projectNameMain.setText("Project Name")
        projectName2.setText("Project Name")
        projectName3.setText("Project Name")
        projectName4.setText("Project Name")
        projectName5.setText("Project Name")

        projectAddressMain.setText("Project Address")
        projectAddress2.setText("Project Address")
        projectAddress3.setText("Project Address")
        projectAddress4.setText("Project Address")
        projectAddress5.setText("Project Address")
    }
}
