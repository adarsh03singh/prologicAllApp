package com.ecom.prologic.viewmodel

import android.app.Application
import android.os.Handler

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.prologic.strains.model.order_list.OrderItem
import com.prologic.strains.network.Repository
import com.prologic.strains.utils.SharedPreference


class OrderDetailsViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreference = SharedPreference()
    val user_data = sharedPreference.getUser()
    val apiRepository = Repository()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var orderItem = MutableLiveData<OrderItem>()
    var errorMessage = MutableLiveData<String>("")

}