package com.prologic.strains.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prologic.strains.R
import com.prologic.strains.databinding.ProdictListBinding
import com.prologic.strains.model.category.CategoryItem
import com.prologic.strains.utils.*
import com.prologic.strains.view.CategoryDialog
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.prodict_list.*


class ProductList : Fragment() {
    lateinit var viewModel: ProductListViewModel
    lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ProdictListBinding =
            DataBindingUtil.inflate(inflater, R.layout.prodict_list, container, false)
        viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(viewModel.categoryItem.name, true, true, false, true)
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
        viewModel.selectedItem.value = 0
        setHeader()
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.page = 1
            viewModel.productArray.clear()
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
        sortingClick.setOnClickListener {
       viewModel.showPopupWindow(it)
        }

        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        addScrollerListener()

        categoryClick.setOnClickListener {
            val categoryDialog = CategoryDialog(requireActivity())
            categoryDialog.setOnItemClickListener(object : CategoryItemListener {
                override fun onClick(item: CategoryItem) {
                    categoryName.text = item.name
                    viewModel.categoryItem = item
                    viewModel.selectedItem.value = 0
                    setHeader()
                }
            })
        }
    }


    private fun addScrollerListener() {
        nestedScrollView?.viewTreeObserver?.addOnScrollChangedListener {
            if (viewModel.isLoading)
                return@addOnScrollChangedListener
            val view = nestedScrollView.getChildAt(nestedScrollView.childCount - 1)
            val diff = view.bottom - (nestedScrollView.height + nestedScrollView.scrollY)
            if (diff == 0) {
                viewModel.isLoading = true
                viewModel.getProductByCategory()
            }
        }
/*
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!viewModel.isLoading) {
                    val totalItemCount = layoutManager.getItemCount()
                    val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    Log.d(TAG, "LayoutManager-pro : " + totalItemCount + " " + lastVisiblePosition)
                    if (totalItemCount == (lastVisiblePosition + 1)) { //visibleThreshold
                        viewModel.isLoading = true
                        viewModel.getProductByCategory()
                        Log.d(
                            TAG,
                            "LayoutManager-pro Call-API : " + totalItemCount + " " + lastVisiblePosition
                        )
                    }
                }
            }
        })
*/
    }


}

