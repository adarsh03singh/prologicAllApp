package com.ecom.prologic.viewmodel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

import com.prologic.strains.model.order_list.OrderItem
import com.prologic.strains.model.order_list.OrdersResult
import com.ecom.prologic.view.fragment.OrderDetails

import com.google.gson.Gson
import com.prologic.strains.adapter.OrdersAdapter
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersListViewModel(application: Application) : AndroidViewModel(application),
    OrderListClickListener {
    val sharedPreference = SharedPreference()
    val user_data = sharedPreference.getUser()
    val apiRepository = Repository()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var ordersArray = MutableLiveData<OrdersResult>()
    var errorMessage = MutableLiveData<String>("")
    val customer_id = user_data?.id.toString()
    val title_text = MutableLiveData<String>("My Orders")
    val ordersAdapter = OrdersAdapter(this)

    init {
        ordersArray.observeForever {
            ordersAdapter.setUpdateAdapter(it)
        }
        ordersAdapter.setOnOrderListClickListener(this)
    }


    private fun getLocalData() {
        val json = sharedPreference.getString("orders_" + customer_id)
        if (json!!.isNotEmpty())
            ordersArray.value=(gson.fromJson(json, OrdersResult::class.java))
    }

    fun getOrders() {
        getLocalData()
        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    ordersArray.postValue(apiRepository.getOrders(customer_id))
                }
            }.onSuccess {
                isLoaderVisible.postValue(false)

            }.onFailure {
                isLoaderVisible.postValue(false)
                errorMessage.postValue(errorException(it))
            }
        }
    }



    override fun onClick(orderItem: OrderItem) {
        val bundle = Bundle()
        bundle.putString(intent_data, gson.toJson(orderItem))
        val fragment = OrderDetails()
        fragment.arguments = bundle
        addFragmentFadeAnim(fragment)
    }
}