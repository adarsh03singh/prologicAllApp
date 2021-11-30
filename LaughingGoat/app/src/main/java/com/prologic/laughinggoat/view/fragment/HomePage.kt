package com.prologic.laughinggoat.view.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.blog.prologic.imageslider.constants.ScaleTypes
import com.blog.prologic.imageslider.models.SlideModel


import com.prologic.laughinggoat.R

import com.prologic.laughinggoat.databinding.HomePageBinding
import com.prologic.laughinggoat.utils.*

import com.prologic.laughinggoat.view.activity.MainActivity
import com.prologic.laughinggoat.viewmodel.HomeViewModel

import kotlinx.android.synthetic.main.home_page.*
import kotlinx.android.synthetic.main.home_page.swipeRefreshLayout
import kotlinx.android.synthetic.main.prodict_list.*
import sqip.CardNonceBackgroundHandler


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
        viewModel.setData()

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
        viewModel.initialize()

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