package com.prologic.strains.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.R
import com.prologic.strains.databinding.ProdictListBinding
import com.prologic.strains.model.category.CategoryItem
import com.prologic.strains.utils.AlertError
import com.prologic.strains.utils.intent_data
import com.prologic.strains.utils.is_cart_update
import com.prologic.strains.utils.shooterFragment
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.prodict_list.*


class ProductList : Fragment() {
    lateinit var viewModel: ProductListViewModel
    lateinit var binding: ProdictListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.prodict_list, container, false)
        viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(viewModel.categoryItem.name, true, true, true, true)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setHeader()
            if (is_cart_update) {
                viewModel.updateAdapter()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.categoryItem = arguments?.getSerializable(intent_data) as CategoryItem
        viewModel.setData()
        setHeader()
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getProductByCategory()
        }
        viewModel.isLoaderVisible.observe(viewLifecycleOwner, Observer { it ->
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.isRefreshing = it
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { it ->
            if (it.isNotEmpty())
                AlertError.show(requireActivity(), it)
        })


    }

}

