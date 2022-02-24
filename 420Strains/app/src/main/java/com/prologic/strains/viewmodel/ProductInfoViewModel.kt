package com.prologic.strains.viewmodel


import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.prologic.strains.adapter.HomeSliderAdapter
import com.prologic.strains.adapter.IndicatorAdapter
import com.prologic.strains.model.ErrorAlert
import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.model.product.Variation
import com.prologic.strains.model.product.VariationResult
import com.prologic.strains.model.slider.SliderItem
import com.prologic.strains.model.slider.SliderResult
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.SharedPreferenceProduct
import com.prologic.strains.utils.is_cart_update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductInfoViewModel(application: Application) : AndroidViewModel(application) {
    val apiRepository = Repository()
    val sharedPreferenceProduct = SharedPreferenceProduct()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<ErrorAlert>()
    var product_quantity = MutableLiveData<Int>(0)
    val title_text = MutableLiveData<String>("Product Details")
    lateinit var productItem: ProductItem
    var variation: Variation? = null
    val variationResult = MutableLiveData<VariationResult>()
    val sliderAdapter = HomeSliderAdapter()
    val indicatorAdapter = IndicatorAdapter()


    fun init() {
        val sliderResult = SliderResult()
        productItem.images.forEach {
            sliderResult.add(SliderItem(it.src))
        }
        sliderAdapter.updateAdapter(sliderResult)
        indicatorAdapter.updateAdapter(sliderResult.size)
        startSliding()
        if (productItem.variations.size > 0)
            getProductVariations()
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
        minusQuantity(productItem, variation, product_quantity.value!!)
    }

    fun plus(view: View) {
        var price = productItem.getProductPrice()
        if (variation != null) {
            price = variation!!.getProductPrice()
        }
        if (price.isNullOrEmpty()) {
            errorMessage.value = ErrorAlert(0, "Product price not available!")
        } else {
            is_cart_update = true
            product_quantity.value = product_quantity.value?.plus(1)
            addQuantity(productItem, variation, product_quantity.value!!)
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
            sharedPreferenceProduct.getQuantity(productItem, variation)
    }

    fun getProductVariations() {
        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    apiRepository.getProductVariations(productItem.id)
                }
            }.onSuccess {
                isLoaderVisible.postValue(false)
                if (it.size > 0) {
                    variationResult.value = it
                } else {
                    errorMessage.value = ErrorAlert(1, "Product variation not available!")
                }

            }.onFailure {
                isLoaderVisible.postValue(false)
                errorMessage.value = ErrorAlert(1, errorException(it))
            }
        }
    }


}
