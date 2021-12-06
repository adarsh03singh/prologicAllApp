package com.prologic.strains.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.prologic.strains.adapter.HomeSliderAdapter
import com.prologic.strains.adapter.IndicatorAdapter
import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.model.product.Variation
import com.prologic.strains.model.slider.SliderResult


import com.prologic.strains.network.Repository
import com.prologic.strains.utils.SharedPreferenceProduct
import com.prologic.strains.utils.getRoomDatabase
import com.prologic.strains.utils.is_cart_update
import com.prologic.strains.utils.showToast
import kotlinx.coroutines.*


class ProductInfoViewModel(application: Application) : AndroidViewModel(application) {
    val apiRepository = Repository()
    val sharedPreferenceProduct = SharedPreferenceProduct()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<String>("")
    var product_quantity = MutableLiveData<Int>(0)
    val title_text = MutableLiveData<String>("Product Details")
    val productItem = MutableLiveData<ProductItem>()
    val variation = MutableLiveData<Variation>()
    var sliderResult = MutableLiveData<SliderResult>()
    val sliderAdapter = HomeSliderAdapter()
    val indicatorAdapter = IndicatorAdapter()
    init {
        sliderResult.observeForever {
            sliderAdapter.updateAdapter(it)
            indicatorAdapter.updateAdapter(it.size)
            startSliding()
        }
    }
    fun setViewPager(viewPager: ViewPager) {
        indicatorAdapter.viewPager = viewPager
    }

    fun startSliding() {
        indicatorAdapter.startSliding()
    }

    fun stopSliding() {
        indicatorAdapter.stopSliding()
    }
    fun minus(view: View) {
        is_cart_update = true
        product_quantity.value = product_quantity.value?.minus(1)
        minusQuantity(productItem.value!!, variation.value, product_quantity.value!!)
    }

    fun plus(view: View) {
        var price = productItem.value?.getProductPrice()

        if (variation.value != null) {
            price = variation.value?.getProductPrice()
        }
        if (price.isNullOrEmpty()) {
            showToast("Product price not available!")
        } else {
            is_cart_update = true
            product_quantity.value = product_quantity.value?.plus(1)
            addQuantity(productItem.value!!, variation.value, product_quantity.value!!)
        }
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
