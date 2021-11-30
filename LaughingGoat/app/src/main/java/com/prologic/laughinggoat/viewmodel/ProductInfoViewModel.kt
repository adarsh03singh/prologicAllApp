package com.prologic.laughinggoat.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologic.laughinggoat.adapter.ProductImageSlider
import com.prologic.laughinggoat.model.product.ProductItem
import com.prologic.laughinggoat.model.product.Variation


import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.utils.SharedPreferenceProduct
import com.prologic.laughinggoat.utils.getRoomDatabase
import com.prologic.laughinggoat.utils.is_cart_update
import com.prologic.laughinggoat.utils.showToast
import kotlinx.coroutines.*


class ProductInfoViewModel(application: Application) : AndroidViewModel(application) {
    val apiRepository = Repository()
    val roomDatabase = getRoomDatabase().productRoomDao()
    val sharedPreferenceProduct = SharedPreferenceProduct()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<String>("")
    var product_quantity = MutableLiveData<Int>(0)
    val title_text = MutableLiveData<String>("Product Details")
    val productItem = MutableLiveData<ProductItem>()
    val variation = MutableLiveData<Variation>()
    val productImageSlider = ProductImageSlider()

/*    fun getProductById(product_id: String) {
        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    productItem.postValue(apiRepository.getProductById(product_id))
                }
            }.onSuccess {
                isLoaderVisible.postValue(false)
            }.onFailure {
                isLoaderVisible.postValue(false)
                errorMessage.postValue(errorException(it))
            }
        }
    }*/

    fun minus(view: View) {
        is_cart_update = true
        product_quantity.value = product_quantity.value?.minus(1)
        minusQuantity(productItem.value!!, variation.value, product_quantity.value!!)
    }

    fun plus(view: View) {
        var price = productItem.value!!.price
        var regular_price = productItem.value!!.regular_price
        var sale_price = productItem.value!!.sale_price

        if (variation.value != null) {
            price = variation.value!!.price
            regular_price = variation.value!!.regular_price
            sale_price = variation.value!!.sale_price
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (regular_price.isEmpty() && sale_price.isEmpty() && price.isEmpty()) {
                errorMessage.postValue("You cannot add this item to cart, please contact admin!")
            } else {
                val roomStoreCategories = getRoomStoreCategories()
                if (roomStoreCategories.size == 0) {
                    addToCart()
                } else {
                    var isAdd = false
                    productItem.value!!.categories.forEach {
                        if (roomStoreCategories.contains(it.id)) {
                            isAdd = true
                        }
                    }
                    if (isAdd)
                        addToCart()
                    else {
                        errorMessage.postValue("You cannot add this item to cart!")
                    }
                }
            }
        }
    }

    fun addToCart() {
        val quantity = product_quantity.value!! + 1
        addQuantity(productItem.value!!, variation.value, quantity)
        product_quantity.postValue(quantity)
        is_cart_update = true
    }

    fun getRoomStoreCategories(): ArrayList<String> {
        val productRoomArray = roomDatabase.get()
        val categoryId = ArrayList<String>()
        productRoomArray?.forEach {
            it.categories?.forEach {
                categoryId.add(it.id)
            }
        }
        return categoryId
    }

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        val error = "Error : " + throwable.localizedMessage
        errorMessage.postValue(error)
    }

    fun minusQuantity(item: ProductItem, variation: Variation?, quantity: Int) {
        sharedPreferenceProduct.updateQuantity(2, item, variation, quantity)
    }

    fun addQuantity(item: ProductItem, variation: Variation?, quantity: Int) {
        sharedPreferenceProduct.updateQuantity(1, item, variation, quantity)
    }

    fun getProductQuantity() {
        product_quantity.value =
            sharedPreferenceProduct.getQuantity(productItem.value!!, variation.value)
    }


}
