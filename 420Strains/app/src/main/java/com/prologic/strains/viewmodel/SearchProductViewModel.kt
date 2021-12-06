package com.prologic.strains.viewmodel

import android.app.Application
import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.gson.Gson
import com.prologic.strains.adapter.SearchProductAdapter
import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.model.product.ProductResult
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import com.prologic.strains.view.fragment.ProductInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchProductViewModel(application: Application) : AndroidViewModel(application),
    ProductItemListener {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var search_visibility = MutableLiveData<Int>(View.VISIBLE)
    var cancel_visibility = MutableLiveData<Int>(View.GONE)
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var search_enabled = MutableLiveData<Boolean>(true)
    var productDataArray = MutableLiveData<ProductResult>()
    var errorMessage = MutableLiveData<String>("")
    var search_value = MutableLiveData<String>("")
    val searchProductAdapter = SearchProductAdapter(this)

    init {
        productDataArray.observeForever {
            searchProductAdapter.setUpdateAdapter(it)
        }
        search_value.observeForever {
            // getProductBySearch(it)
        }
        searchProductAdapter.setOnProductListItemClickListener(this)
    }

    fun searchProduct() {
        if (search_value.value!!.isNotEmpty()) {
            search_visibility.value = View.GONE
            cancel_visibility.value = View.VISIBLE
            search_enabled.value=false
            getProductBySearch(search_value.value!!)
        } else {
            showToastShort("Please enter product name")
        }
    }

    fun cancelProduct() {
        search_visibility.value = View.VISIBLE
        cancel_visibility.value = View.GONE
        search_value.value = ""
        search_enabled.value=true
        getProductBySearch(search_value.value!!)
    }

    fun onTextChanged(search_value: CharSequence?) {
    }

    fun getProductBySearch(search_value: String) {
        if (search_value.isEmpty()) {
            productDataArray.value = ProductResult()
            return
        }
        Log.d(TAG, "onTextChanged : " + search_value)
        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    productDataArray.postValue(apiRepository.getProductBySearch(search_value))
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