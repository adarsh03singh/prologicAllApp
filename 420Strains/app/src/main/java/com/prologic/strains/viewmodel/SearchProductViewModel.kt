package com.prologic.strains.viewmodel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologic.strains.adapter.SearchProductAdapter
import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.ProductItemListener
import com.prologic.strains.utils.SharedPreference
import com.prologic.strains.utils.addFragment
import com.prologic.strains.utils.intent_data
import com.prologic.strains.view.fragment.ProductInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchProductViewModel(application: Application) : AndroidViewModel(application),
    ProductItemListener {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<String>("")
    var search_value = MutableLiveData<String>()
    val searchProductAdapter = SearchProductAdapter(this)

    init {

        search_value.observeForever {
            getProductBySearch()
        }
        searchProductAdapter.setOnProductListItemClickListener(this)
    }


    fun onTextChanged(input: CharSequence?) {
        // getProductBySearch()
    }

    fun getProductBySearch() {

        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    apiRepository.getProductBySearch(search_value.value!!)
                }
            }.onSuccess {
                isLoaderVisible.postValue(false)
                searchProductAdapter.setUpdateAdapter(it)
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