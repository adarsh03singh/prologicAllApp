package com.prologic.strains.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.prologic.strains.R
import com.prologic.strains.databinding.HomePageBinding
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_page.*



class HomePage : Fragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var binding: HomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.home_page, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.errorMessage.observeForever { it ->
            if (!it.isEmpty())
                AlertError.show(fragmentActivity!!, it)
        }

        viewModel.isLoaderVisible.observeForever { it ->
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.isRefreshing = it
        }
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.initialize()
        }
        setHeader()
        viewModel.setViewPager(viewPager)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getToday(requireContext())
        viewModel.startSliding()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopSliding()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(getString(R.string.app_name), false, true, true, true)
    }


}