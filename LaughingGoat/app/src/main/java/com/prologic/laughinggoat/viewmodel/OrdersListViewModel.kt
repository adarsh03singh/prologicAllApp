package com.ecom.prologic.viewmodel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*

import com.prologic.laughinggoat.model.order_list.OrderItem
import com.prologic.laughinggoat.model.order_list.OrdersResult
import com.ecom.prologic.view.fragment.OrderDetails

import com.google.gson.Gson
import com.prologic.laughinggoat.adapter.OrdersAdapter
import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.network.errorException
import com.prologic.laughinggoat.utils.OrderListClickListener
import com.prologic.laughinggoat.utils.SharedPreference
import com.prologic.laughinggoat.utils.addFragmentFadeAnim
import com.prologic.laughinggoat.utils.intent_data
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
            ordersArray.value=(Gson().fromJson(json, OrdersResult::class.java))
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
        bundle.putString(intent_data, Gson().toJson(orderItem))
        val fragment = OrderDetails()
        fragment.arguments = bundle
        addFragmentFadeAnim(fragment)
    }
}