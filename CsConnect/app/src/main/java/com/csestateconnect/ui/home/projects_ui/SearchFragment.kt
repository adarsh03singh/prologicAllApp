package com.csestateconnect.ui.home.projects_ui


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.SearchResultListAdapter
import com.csestateconnect.databinding.FragmentSearchBinding
import com.csestateconnect.db.data.SearchResult
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.HomeViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {


    private var snackbar: Snackbar? = null
    lateinit var viewModel: HomeViewModel
    var searchView: SearchView? = null
    var mRecyclerView: RecyclerView? = null
    var mAdapter: SearchResultListAdapter? = null
    private val searchResultList = LinkedList<SearchResult>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val binding: FragmentSearchBinding = FragmentSearchBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.searchViewModel = viewModel
        binding.lifecycleOwner = this

        searchView = view.findViewById<SearchView>(R.id.search_field)
        mRecyclerView = view.findViewById(R.id.search_result_recyclerview)
        view?.context!!.registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )


        return view
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun showNetworkMessage(isConnected: Boolean) {

        if (!isConnected) {
            snackBarView()


        } else {
            snackbar?.dismiss()

        }
    }

    fun snackBarView() {
        snackbar = Snackbar.make(searchFragment_constraint, "You are offline", Snackbar.LENGTH_LONG)
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        val view = snackbar!!.getView()
        view.setBackgroundColor(resources.getColor(R.color.colorchery))
        snackbar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView!!.setIconifiedByDefault(false)
        searchView!!.setQueryHint("Search Projects, Cities..")

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                searchResultList.clear()
//                mAdapter!!.notifyDataSetChanged()
                getSearchResults(query)

                return false
            }
        })
    }



    private fun getSearchResults(query: String){

            Coroutines.main {
                try {
                val cityData = viewModel.callSearchProjectAPIAndStore(query)
                    val projectData = viewModel.callSearchProjectByProjectName(query)
                        val jObjCity = JSONObject(cityData!!.body()!!.string())
                    val jObjProject = JSONObject(projectData!!.body()!!.string())

                val cities = ArrayList<String>()
                val citySuggestionsArray = jObjCity!!.getJSONArray("city_name_suggest__completion")
                val firstSuggestions = citySuggestionsArray.getJSONObject(0)
                val firstSuggestionsArray = firstSuggestions.getJSONArray("options")

                for (i in 0..firstSuggestionsArray.length() - 1) {
                    val suggestion = firstSuggestionsArray.getJSONObject(i)
                    val city = suggestion.getString("text")
                    if (!cities.contains(city)) {
                        cities.add(city)
                        searchResultList.add(SearchResult(city, "CITY"))

                    }
                }
                    val projectSuggestionsArray = jObjProject!!.getJSONArray("name_suggest__completion")
                    val projectFirstSuggestions = projectSuggestionsArray.getJSONObject(0)
                    val projectFirstSuggestionsArray =
                        projectFirstSuggestions.getJSONArray("options")

                    for (i in 0 until projectFirstSuggestionsArray.length()) {
                        val suggestion = projectFirstSuggestionsArray.getJSONObject(i)
                        val project = suggestion.getJSONObject("_source")
                        val id = project.getInt("id")
                        val name = project.getString("name")
                        val location = project.getJSONObject("location").getString("name")
                        val city = project.getJSONObject("city").getString("name")
                        val locationName = location + ", " + city
                        searchResultList.add(SearchResult(id, name, "PROJECT", locationName))
                    }



                    mAdapter!!.notifyDataSetChanged()
//                    getSearchResults(searchResultList)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }



        mAdapter = SearchResultListAdapter(searchResultList)
        mRecyclerView!!.setAdapter(mAdapter)
        // Give the RecyclerView a default layout manager.
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(view!!.context))
        mRecyclerView!!.setItemAnimator(DefaultItemAnimator())

    }
}
