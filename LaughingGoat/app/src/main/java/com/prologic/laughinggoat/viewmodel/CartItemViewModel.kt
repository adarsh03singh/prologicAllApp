package com.prologic.laughinggoat.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologic.laughinggoat.adapter.CartAdapter
import com.prologic.laughinggoat.db.ProductRoom
import com.prologic.laughinggoat.utils.*


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CartItemViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreference = SharedPreference();
    val roomProductDao = getRoomDatabase().productRoomDao()
    val sharedPreferenceProduct = SharedPreferenceProduct()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<String>("")
    var total_amount_view = MutableLiveData<String>("")
    var emptyLay = MutableLiveData<Int>(View.GONE)
    var recyclerViewLay = MutableLiveData<Int>(View.VISIBLE)
    var bottomLay = MutableLiveData<Int>(View.GONE)
    var productRoomArray = roomProductDao.getLive()
    val cartAdapter = CartAdapter(this)
    var product_amt = 0.0
    var product_qtn = 0

    init {
        productRoomArray.observeForever() {
            cartAdapter.setUpdateAdapter(it)
            setAmount()
            if (it.size == 0) {
                emptyLay.value = View.VISIBLE
                recyclerViewLay.value = View.GONE
                bottomLay.value = View.GONE
            } else {
                emptyLay.value = View.GONE
                recyclerViewLay.value = View.VISIBLE
                bottomLay.value = View.VISIBLE
            }
        }
    }


    fun setAmount() {
        product_amt = 0.0
        product_qtn = 0
        for (item in productRoomArray.value!!) {
            product_qtn = product_qtn + item.quantity
            if (item.sale_price.isNotEmpty())
                product_amt = product_amt + (item.quantity * item.sale_price.toDouble())
            else if (item.regular_price.isNotEmpty()) {
                product_amt = product_amt + (item.quantity * item.regular_price.toDouble())
            } else if (item.price.isNotEmpty()) {
                product_amt = product_amt + (item.quantity * item.price.toDouble())
            }
        }
        val total_amount =
            "Total Amount <b>: " + currency + roundOfDecimal(product_amt) + "</b> (" + product_qtn + " Quantity)"
        total_amount_view.postValue(total_amount)
    }


    fun updateQuantity(item: ProductRoom, quantity: Int) {
        is_cart_update = true
        sharedPreferenceProduct.setProduct(item.product_id, item.variation_id, quantity)
        viewModelScope.launch(Dispatchers.IO) {
            if (quantity == 0) {
                roomProductDao.deleteProduct(item.unique_id)
            } else {
                roomProductDao.update(quantity, item.unique_id)
            }
        }

    }

}