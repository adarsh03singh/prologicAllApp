package com.csestateconnect.ui.navdrawer


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.csestateconnect.R
import com.csestateconnect.adapters.FavoriteProjectsAdapter
import com.csestateconnect.adapters.ProjectAdapter
import com.csestateconnect.databinding.FragmentFavoritesBinding
import com.csestateconnect.databinding.FragmentProjectsFrag2Binding
import com.csestateconnect.db.data.favouriteProject.FavouriteProject
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.viewmodel.HomeViewModel
import com.csestateconnect.viewmodel.ProjectsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_project_item.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_projects_frag2.*

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {

    lateinit var viewModel: HomeViewModel
    var projectRecyclerViewAdapter: FavoriteProjectsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val binding: FragmentFavoritesBinding = FragmentFavoritesBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.favProjViewmodel = viewModel
        binding.lifecycleOwner = this

        if(isNetworkConnected(context!!))
        {
            try {
                viewModel.callGetFavouriteProjectAPIAndStore()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else
        {
            Toast.makeText(context,"No internet found.",Toast.LENGTH_LONG).show()
        }

        viewModel.getFavouriteProjectsDatabase().observe(this, Observer<List<GetFavouriteProjectsEntity>> { favProjectData ->

            try{

                val favoriteProjList = favProjectData.get(0).favouriteProjects

            val FavData = favoriteProjList!!.map { it } as MutableList
                setUpProjectRecyclerView(FavData)
                if(!favoriteProjList.isNullOrEmpty()) {

                    blankFavorites_layout.visibility = View.GONE
                    favorites_recycler_view.visibility = View.VISIBLE

                } else{
                blankFavorites_layout.visibility = View.VISIBLE
                favorites_recycler_view.visibility= View.GONE
            }
                }catch (e: Exception){
                    e.printStackTrace()
                }
        })


        return view
    }


    fun setUpProjectRecyclerView(projectResultList : MutableList<FavouriteProject?>)
    {
        val preferences = view!!.context.getSharedPreferences("PREFERENCE_NAME", 0)
        val editor = preferences.edit()
        val selectedId = preferences.getString("favProjeId", "")
            projectRecyclerViewAdapter = FavoriteProjectsAdapter(projectResultList) {
                viewModel.callRemoveFavouriteProjectsApi(it)
                if(viewModel.checkResponseRemove.value == true) {
                    Snackbar.make(
                        view!!,
                        "project has been removed.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                if(it.toString() == selectedId){
                    editor.remove("favProjeId").apply()
                }
            }
            favorites_recycler_view.adapter = projectRecyclerViewAdapter
            favorites_recycler_view.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
            favorites_recycler_view.setHasFixedSize(true)

    }


    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
