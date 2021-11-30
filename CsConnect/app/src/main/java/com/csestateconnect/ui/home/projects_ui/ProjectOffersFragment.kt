package com.csestateconnect.ui.home.projects_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.csestateconnect.R
import com.csestateconnect.adapters.ProjectOffersAdapter
import kotlinx.android.synthetic.main.fragment_project_offers.*

class ProjectOffersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager: GridLayoutManager = GridLayoutManager(
            context, // Context
            2,
            GridLayoutManager.VERTICAL, false
        )

        offerRecyclerView?.layoutManager = gridLayoutManager

        val services_items = communityList()
        val adapterServices = ProjectOffersAdapter(services_items) {
            if (it.equals("Local Services")) {
//                view.findNavController().navigate(R.id.localServicesFragment)
            }
        }
        offerRecyclerView?.adapter = adapterServices

    }

    private fun communityList(): ArrayList<offersModel> {
        val data_list = ArrayList<offersModel>()


        // Put some user's in data_list list
        data_list.add(ProjectOffersFragment.offersModel(R.drawable.img))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img_1))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.bildings_image))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.bildings_image))

        data_list.add(ProjectOffersFragment.offersModel(R.drawable.bildings_image))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img))
        data_list.add(ProjectOffersFragment.offersModel( R.drawable.img))

        return data_list
    }


    data class offersModel( var img: Int)

}