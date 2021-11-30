package com.ecom.prologic.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


import com.ecom.prologic.viewmodel.OrdersListViewModel
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.OrdersListBinding
import com.prologic.laughinggoat.utils.AlertError
import com.prologic.laughinggoat.utils.shooterFragment
import com.prologic.laughinggoat.view.activity.MainActivity
import kotlinx.android.synthetic.main.orders_list.*


class OrdersList : Fragment() {
    lateinit var viewModel: OrdersListViewModel
    lateinit var binding: OrdersListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.orders_list, container, false)
        viewModel = ViewModelProvider(this).get(OrdersListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getOrders()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getOrders()
        }
        viewModel.isLoaderVisible.observe(viewLifecycleOwner, Observer { it ->
            swipeRefreshLayout.isRefreshing = it
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { it ->
            if (it.isNotEmpty())
                AlertError.show(requireActivity(), it)
        })

        setHeader()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setHeader()
        }
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(viewModel.title_text.value!!, true, false, true, false)
    }


}

