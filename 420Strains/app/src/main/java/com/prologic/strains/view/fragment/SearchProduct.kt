package com.prologic.strains.view.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider



import kotlinx.android.synthetic.main.prodict_list.swipeRefreshLayout
import kotlinx.android.synthetic.main.search_product.*
import android.view.inputmethod.EditorInfo
import com.prologic.strains.R
import com.prologic.strains.databinding.SearchProductBinding
import com.prologic.strains.utils.AlertError
import com.prologic.strains.utils.getAppFragmentManager
import com.prologic.strains.utils.hideSoftKeyBoard
import com.prologic.strains.utils.shooterFragment
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.SearchProductViewModel


class SearchProduct : Fragment() {
    lateinit var viewModel: SearchProductViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: SearchProductBinding = DataBindingUtil.inflate(inflater, R.layout.search_product, container, false)
        viewModel = ViewModelProvider(this).get(SearchProductViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader("", true, false, false, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setHeader()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getProductBySearch()
        }
        viewModel.isLoaderVisible.observeForever {
            swipeRefreshLayout.isRefreshing = it
        }


        viewModel.errorMessage.observeForever { it ->
            if (it.isNotEmpty())
                AlertError.show(requireActivity(), it)
        }
        back_button.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }



    }




}

