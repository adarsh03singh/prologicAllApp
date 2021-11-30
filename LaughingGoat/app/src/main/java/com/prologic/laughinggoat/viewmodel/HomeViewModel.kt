package com.prologic.laughinggoat.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.network.errorException
import com.prologic.laughinggoat.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import com.prologic.laughinggoat.adapter.*
import com.prologic.laughinggoat.model.FloralGalleryItem
import com.prologic.laughinggoat.model.FloralGalleryResult
import com.prologic.laughinggoat.model.blog.BlogItem
import com.prologic.laughinggoat.model.blog.BlogResult
import com.prologic.laughinggoat.model.category.CategoryResult
import com.prologic.laughinggoat.model.category.CategoryItem
import com.prologic.laughinggoat.model.event_vendor.EventVendorItem
import com.prologic.laughinggoat.model.event_vendor.EventVendorResult
import com.prologic.laughinggoat.model.product.ProductItem
import com.prologic.laughinggoat.model.product.ProductResult

import com.prologic.laughinggoat.model.slider.SliderResult
import com.prologic.laughinggoat.view.activity.MyWebView
import com.prologic.laughinggoat.view.fragment.PostInfo
import com.prologic.laughinggoat.view.fragment.ProductInfo
import com.prologic.laughinggoat.view.fragment.ProductList

class HomeViewModel : ViewModel(), CategoryItemListener, BlogItemListener,
    EventVendorItemListener, FloralGalleryItemListener, FeaturedProductClickListener {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var categoryResult = MutableLiveData<CategoryResult>()
    var featuredProductResult = MutableLiveData<ProductResult>()
    var blogResult = MutableLiveData<BlogResult>()

    var eventVendorResult = MutableLiveData<EventVendorResult>()
    var errorMessage = MutableLiveData<String>("")
    val categoryAdapter = CategoryAdapter(this)
    val blogAdapter = BlogAdapter(this)
    val eventVendorAdapter = EventVendorAdapter(this)
    var floralGalleryResult = MutableLiveData<FloralGalleryResult>()
    val floralGalleryAdapter = FloralGalleryAdapter(this)
    val homeProductAdapter = HomeProductAdapter(this)
    var apiCount = 0;
  
    /* fun getCategory(): CategoryResult {
         val categoryResult = CategoryResult()
         categoryResult.add(CategoryItem("19", "Dahlia Tubers"))
         categoryResult.add(CategoryItem("66", "Bulbs"))
         categoryResult.add(CategoryItem("33", "Seeds"))
         categoryResult.add(CategoryItem("36", "Features"))
         categoryResult.add(CategoryItem("58", "Retails"))
         return categoryResult
     }*/

    fun setData() {
        var json: String? = ""
        json = sharedPreference.getString("blog")
        if (json!!.isNotEmpty())
            blogResult.value = (Gson().fromJson(json, BlogResult::class.java))

        json = sharedPreference.getString("category_data")
        if (json!!.isNotEmpty())
            categoryResult.value = (Gson().fromJson(json, CategoryResult::class.java))

        json = sharedPreference.getString("featured_product")
        if (json!!.isNotEmpty())
            featuredProductResult.value = (Gson().fromJson(json, ProductResult::class.java))

        json = sharedPreference.getString("event_vendor")
        if (json!!.isNotEmpty())
            eventVendorResult.value = (Gson().fromJson(json, EventVendorResult::class.java))

        json = sharedPreference.getString("floral_gallery")
        if (json!!.isNotEmpty())
            floralGalleryResult.value = (Gson().fromJson(json, FloralGalleryResult::class.java))


        //   categoryResult.value = getCategory()

        categoryResult.observeForever {
            categoryAdapter.updateAdapter(it)
        }
        blogResult.observeForever {
            blogAdapter.updateAdapter(it)
        }
        eventVendorResult.observeForever {
            eventVendorAdapter.updateAdapter(it)
        }
        featuredProductResult.observeForever {
            homeProductAdapter.updateAdapter(it)
        }
        floralGalleryResult.observeForever {
            floralGalleryAdapter.setUpdateAdapter(it)
        }
        floralGalleryAdapter.setClickItemListener(this)
        categoryAdapter.setCategoryItemListener(this)
        blogAdapter.setClickItemListener(this)
        eventVendorAdapter.setClickItemListener(this)
        homeProductAdapter.setClickItemListener(this)

    }


    fun initialize() {
        apiCount = 0
        getEventVendor()
        getBlog()
        getFloralGallery()
        getFeaturedProduct()
        getCategory()
        isLoaderVisible.value = true
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
                }
            }.onSuccess {
                hideLoading()
            }.onFailure {
                hideLoading()
                errorMessage.postValue(errorException(it))
            }
        }
    }

    fun getFloralGallery() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    floralGalleryResult.postValue(apiRepository.getFloralGallery())
                }
            }.onSuccess {
                hideLoading()
            }.onFailure {
                hideLoading()
                errorMessage.postValue(errorException(it))
            }
        }
    }

    fun getFeaturedProduct() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    featuredProductResult.postValue(apiRepository.getFeaturedProduct())
                }
            }.onSuccess {
                hideLoading()
            }.onFailure {
                hideLoading()
                errorMessage.postValue(errorException(it))
            }
        }
    }


    fun getBlog() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    blogResult.postValue(apiRepository.getBlog())
                }
            }.onSuccess {
                hideLoading()
            }.onFailure {
                hideLoading()
                errorMessage.postValue(errorException(it))
            }
        }
    }


    fun getEventVendor() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    eventVendorResult.postValue(apiRepository.getEventVendor())
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

    override fun onClick(item: BlogItem) {
        val bundle = Bundle()
        bundle.putString("id", item.id)
        bundle.putString("title", item.title)
        bundle.putString("author_name", item.post_author_name)
        bundle.putString("post_date", item.post_date)
        bundle.putString("image_url", item.source_url)
        bundle.putString("content", item.content)
        val fragment = PostInfo()
        fragment.arguments = bundle
        addFragment(fragment)
    }

    override fun onClick(item: EventVendorItem, url: String) {
        val intent = Intent(fragmentActivity, MyWebView::class.java)
        intent.putExtra("title", item.title)
        intent.putExtra("url", url)
        fragmentActivity?.startActivity(intent)
    }

    override fun onClick(item: FloralGalleryItem) {
        val bundle = Bundle()
        bundle.putString("id", item.id)
        bundle.putString("title", item.title)
        bundle.putString("content", item.content)
        val fragment = PostInfo()
        fragment.arguments = bundle
        addFragment(fragment)
    }

    override fun onClick(item: ProductItem) {
        val bundle = Bundle()
        bundle.putSerializable(intent_data, item)
        val fragment = ProductInfo()
        fragment.arguments = bundle
        addFragment(fragment)
    }

}