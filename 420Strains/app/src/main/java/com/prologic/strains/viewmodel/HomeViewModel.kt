package com.prologic.strains.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.prologic.strains.adapter.*


import com.prologic.strains.model.business_hour.BusinessHourResult
import com.prologic.strains.model.business_hour.Item
import com.prologic.strains.model.category.CategoryItem
import com.prologic.strains.model.category.CategoryResult
import com.prologic.strains.model.delivery.DeliveryAreaResult
import com.prologic.strains.model.delivery.DeliveryItem
import com.prologic.strains.model.delivery.DeliveryResult

import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.model.product.ProductResult
import com.prologic.strains.model.slider.SliderResult
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.MyWebView
import com.prologic.strains.view.fragment.PostInfo
import com.prologic.strains.view.fragment.ProductInfo
import com.prologic.strains.view.fragment.ProductList
import kotlinx.android.synthetic.main.home_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel(), CategoryItemListener,
    FeaturedProductClickListener {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var categoryResult = MutableLiveData<CategoryResult>()
    var deliveryResult = MutableLiveData<DeliveryResult>()
    var errorMessage = MutableLiveData<String>("")
    val categoryAdapter = CategoryAdapter(this)
    val businessHourAdapter = BusinessHourAdapter(this)
    var promotionalProductResult = MutableLiveData<ProductResult>()
    var indicaProductResult = MutableLiveData<ProductResult>()
    var sliderResult = MutableLiveData<SliderResult>()
    var hybridsProductResult = MutableLiveData<ProductResult>()
    var sativaProductResult = MutableLiveData<ProductResult>()
    var concentratesProductResult = MutableLiveData<ProductResult>()
    var businessHourResult = BusinessHourResult()

    val sliderAdapter = HomeSliderAdapter()
    val indicatorAdapter = IndicatorAdapter()
    val promotionalAdapter = HomeProductAdapter(this)
    val indicaAdapter = HomeProductAdapter(this)
    val hybridsAdapter = HomeProductAdapter(this)
    val sativaAdapter = HomeProductAdapter(this)
    val concentratesAdapter = HomeProductAdapter(this)
    val deliveryAdapter = DeliveryAdapter(this)
    var apiCount = 0

    fun setViewPager(viewPager: ViewPager) {
        indicatorAdapter.viewPager = viewPager
    }

    fun startSliding() {
        indicatorAdapter.startSliding()
    }

    fun stopSliding() {
        indicatorAdapter.stopSliding()
    }


    init {
        businessHourResult.add(Item("Monday", "11:00 AM", "07:00 PM"))
        businessHourResult.add(Item("Tuesday", "11:00 AM", "07:00 PM"))
        businessHourResult.add(Item("Wednesday", "11:00 AM", "07:00 PM"))
        businessHourResult.add(Item("Thursday", "11:00 AM", "07:00 PM"))
        businessHourResult.add(Item("Friday", "11:00 AM", "07:00 PM"))
        businessHourResult.add(Item("Saturday", "11:00 AM", "07:00 PM"))
        businessHourResult.add(Item("Sunday", "CLOSED", "CLOSED"))


        var json: String? = ""
        json = sharedPreference.getString("product_116")
        if (json!!.isNotEmpty())
            promotionalProductResult.value = (Gson().fromJson(json, ProductResult::class.java))

        json = sharedPreference.getString("product_54")
        if (json!!.isNotEmpty())
            indicaProductResult.value = (Gson().fromJson(json, ProductResult::class.java))

        json = sharedPreference.getString("product_55")
        if (json!!.isNotEmpty())
            hybridsProductResult.value = (Gson().fromJson(json, ProductResult::class.java))

        json = sharedPreference.getString("product_56")
        if (json!!.isNotEmpty())
            sativaProductResult.value = (Gson().fromJson(json, ProductResult::class.java))

        json = sharedPreference.getString("product_57")
        if (json!!.isNotEmpty())
            concentratesProductResult.value = (Gson().fromJson(json, ProductResult::class.java))

        json = sharedPreference.getString("category_data")
        if (json!!.isNotEmpty())
            categoryResult.value = (Gson().fromJson(json, CategoryResult::class.java))

        json = sharedPreference.getString("delivery_area")
        if (json!!.isNotEmpty())
            deliveryResult.value = (Gson().fromJson(json, DeliveryResult::class.java))

        json = sharedPreference.getString("slider_data")
        if (json!!.isNotEmpty())
            sliderResult.value = (Gson().fromJson(json, SliderResult::class.java))

        categoryAdapter.setCategoryItemListener(this)
        promotionalAdapter.setClickItemListener(this)
        indicaAdapter.setClickItemListener(this)
        hybridsAdapter.setClickItemListener(this)
        sativaAdapter.setClickItemListener(this)
        concentratesAdapter.setClickItemListener(this)
        deliveryResult.observeForever {
            deliveryAdapter.updateAdapter(it)
        }
        categoryResult.observeForever {
            categoryAdapter.updateAdapter(it)
        }
        sliderResult.observeForever {
            sliderAdapter.updateAdapter(it)
            indicatorAdapter.updateAdapter(it.size)
            startSliding()
        }
        promotionalProductResult.observeForever {
            promotionalAdapter.updateAdapter(it)
        }
        indicaProductResult.observeForever {
            indicaAdapter.updateAdapter(it)
        }
        hybridsProductResult.observeForever {
            hybridsAdapter.updateAdapter(it)
        }
        sativaProductResult.observeForever {
            sativaAdapter.updateAdapter(it)
        }
        concentratesProductResult.observeForever {
            concentratesAdapter.updateAdapter(it)
        }
        initialize()
    }


    fun getToday(context: Context) {
        val calendar = Calendar.getInstance()
        val date = calendar.getTime()
        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime())
        businessHourAdapter.updateAdapter(context, day, businessHourResult)
    }

    fun initialize() {
        apiCount = 0
        isLoaderVisible.value = true
        getProductByCategory(promotionalProductResult, "116")
        getProductByCategory(indicaProductResult, "54")
        getProductByCategory(hybridsProductResult, "55")
        getProductByCategory(sativaProductResult, "56")
        getProductByCategory(concentratesProductResult, "57")
        getCategory()
    }

    fun hideLoading() {
        apiCount++
        if (apiCount >= 6) {
            isLoaderVisible.value = false
        }
    }

    fun getCategory() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    categoryResult.postValue(apiRepository.getCategory())
                    sliderResult.postValue(apiRepository.getSlider())
                    deliveryResult.postValue(apiRepository.getDeliveryArea())
                }
            }.onSuccess {
                hideLoading()
            }.onFailure {
                hideLoading()
                errorMessage.postValue(errorException(it))
            }
        }
    }

    fun getProductByCategory(productResult: MutableLiveData<ProductResult>, category: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    productResult.postValue(apiRepository.getProductByCategory(category))
                }
            }.onSuccess {
                hideLoading()
            }.onFailure {
                hideLoading()
                errorMessage.postValue(errorException(it))
            }
        }
    }

    override fun onClick(item: CategoryItem) {
        val bundle = Bundle()
        bundle.putSerializable(intent_data, item)
        val fragment = ProductList()
        fragment.arguments = bundle
        addFragment(fragment)
    }


    fun getDeliveryArea(item: DeliveryItem) {
        isLoaderVisible.value = true
        var result: DeliveryAreaResult? = null
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    result = apiRepository.getDeliveryArea(item.page_id)
                }
            }.onSuccess {
                isLoaderVisible.value = false
                if (result != null) {
                    val bundle = Bundle()
                    bundle.putString("id", result!!.id)
                    bundle.putString("title", result!!.title.rendered)
                    bundle.putString("content", result!!.content.rendered)
                    val fragment = PostInfo()
                    fragment.arguments = bundle
                    addFragment(fragment)
                }
            }.onFailure {
                isLoaderVisible.value = false
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