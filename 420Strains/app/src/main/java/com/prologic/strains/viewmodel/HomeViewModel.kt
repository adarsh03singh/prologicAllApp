package com.prologic.strains.viewmodel


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
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
import com.prologic.strains.view.fragment.PostInfo
import com.prologic.strains.view.fragment.ProductInfo
import com.prologic.strains.view.fragment.ProductList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel(), CategoryItemListener, ProductItemListener {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    val isLoaderVisible = MutableLiveData<Boolean>()

    val categoryResult = MutableLiveData<CategoryResult>()
    val deliveryResult = MutableLiveData<DeliveryResult>()
    val errorMessage = MutableLiveData<String>()

    //val categoryAdapter = CategoryAdapter(this)
    val businessHourAdapter = BusinessHourAdapter(this)
    var promotionalProductResult = MutableLiveData<ProductResult>()
    val indicaProductResult = MutableLiveData<ProductResult>()
    val sliderResult = MutableLiveData<SliderResult>()
    val hybridsProductResult = MutableLiveData<ProductResult>()
    val sativaProductResult = MutableLiveData<ProductResult>()
    val concentratesProductResult = MutableLiveData<ProductResult>()
    var businessHourResult = BusinessHourResult()

    val sliderAdapter = HomeSliderAdapter()
    val indicatorAdapter = IndicatorAdapter()
    val promotionalAdapter = ProductAdapter(2)
    val indicaAdapter = ProductAdapter(2)
    val hybridsAdapter = ProductAdapter(2)
    val sativaAdapter = ProductAdapter(2)
    val concentratesAdapter = ProductAdapter(2)
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
            promotionalProductResult.value = gson.fromJson(json, ProductResult::class.java)

        json = sharedPreference.getString("product_54")
        if (json!!.isNotEmpty())
            indicaProductResult.value = gson.fromJson(json, ProductResult::class.java)

        json = sharedPreference.getString("product_55")
        if (json!!.isNotEmpty())
            hybridsProductResult.value = gson.fromJson(json, ProductResult::class.java)

        json = sharedPreference.getString("product_56")
        if (json!!.isNotEmpty())
            sativaProductResult.value = gson.fromJson(json, ProductResult::class.java)

        json = sharedPreference.getString("product_57")
        if (json!!.isNotEmpty())
            concentratesProductResult.value = gson.fromJson(json, ProductResult::class.java)

        /*  json = sharedPreference.getString("category_data")
          if (json!!.isNotEmpty())
              categoryResult.value = gson.fromJson(json, CategoryResult::class.java)*/

        json = sharedPreference.getString("delivery_area")
        if (json!!.isNotEmpty())
            deliveryResult.value = gson.fromJson(json, DeliveryResult::class.java)

        json = sharedPreference.getString("slider_data")
        if (json!!.isNotEmpty())
            sliderResult.value = gson.fromJson(json, SliderResult::class.java)

        //categoryAdapter.setCategoryItemListener(this)

        promotionalAdapter.setOnItemListener(this)
        indicaAdapter.setOnItemListener(this)
        hybridsAdapter.setOnItemListener(this)
        sativaAdapter.setOnItemListener(this)
        concentratesAdapter.setOnItemListener(this)
        deliveryResult.observeForever {
            deliveryAdapter.updateAdapter(it)
        }
        categoryResult.observeForever {
            Log.d(TAG, it.toString())
            // categoryAdapter.updateAdapter(it)
        }
        sliderResult.observeForever {
            sliderAdapter.updateAdapter(it)
            indicatorAdapter.updateAdapter(it.size)
            startSliding()
        }
        promotionalProductResult.observeForever {
            promotionalAdapter.setUpdateAdapter(it)
        }
        indicaProductResult.observeForever {
            indicaAdapter.setUpdateAdapter(it)
        }
        hybridsProductResult.observeForever {
            hybridsAdapter.setUpdateAdapter(it)
        }
        sativaProductResult.observeForever {
            sativaAdapter.setUpdateAdapter(it)
        }
        concentratesProductResult.observeForever {
            concentratesAdapter.setUpdateAdapter(it)
        }
        initialize()
    }


    fun getToday(context: Context) {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
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
        if (apiCount >= 5) {
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
                    apiRepository.getProducts(category, 1, "id", "asc")

                }
            }.onSuccess {
                productResult.postValue(it)
                sharedPreference.putString("product_" + category, gson.toJson(it))
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