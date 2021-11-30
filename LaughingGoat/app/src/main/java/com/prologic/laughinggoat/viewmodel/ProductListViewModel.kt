package com.prologic.laughinggoat.viewmodel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import com.google.gson.Gson
import com.prologic.laughinggoat.adapter.ProductAdapter
import com.prologic.laughinggoat.model.category.CategoryItem
import com.prologic.laughinggoat.model.product.ProductItem
import com.prologic.laughinggoat.model.product.ProductResult
import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.network.errorException
import com.prologic.laughinggoat.utils.ProductItemListener
import com.prologic.laughinggoat.utils.SharedPreference
import com.prologic.laughinggoat.utils.addFragment
import com.prologic.laughinggoat.utils.intent_data
import com.prologic.laughinggoat.view.fragment.ProductInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel(application: Application) : AndroidViewModel(application),
    ProductItemListener {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var productResult = MutableLiveData<ProductResult>()

    var errorMessage = MutableLiveData<String>("")
    lateinit var categoryItem: CategoryItem
    val productAdapter = ProductAdapter(this)

    fun updateAdapter() {
        productAdapter.setUpdateAdapter(productResult.value!!)
    }

    fun setData() {
        var json = sharedPreference.getString("product_" + categoryItem.id)
        if (json!!.isNotEmpty())
            productResult.value = (Gson().fromJson(json, ProductResult::class.java))

        productResult.observeForever {
            productAdapter.setUpdateAdapter(it)
        }
        getProductByCategory()
        productAdapter.setOnProductItemListener(this)
    }

    fun getProductByCategory() {
        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val response = apiRepository.getProductByCategory(categoryItem.id)
                    if (response.size > 0) {
                        productResult.postValue(response)
                    } else {
                        errorMessage.postValue("Oh No, there are no items in this category!")
                    }

                }
            }.onSuccess {
                isLoaderVisible.postValue(false)

            }.onFailure {
                isLoaderVisible.postValue(false)
                errorMessage.postValue(errorException(it))
            }
        }
    }

    override fun onClick(item: ProductItem) {
        val bundle = Bundle()
        bundle.putSerializable(intent_data, item)
        val fragment = ProductInfo()
        fragment.arguments = bundle
        addFragment(fragment)
    }


}