package com.prologic.strains.viewmodel

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologic.strains.R
import com.prologic.strains.adapter.ProductAdapter
import com.prologic.strains.model.SortingMenu
import com.prologic.strains.model.category.CategoryItem
import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.model.product.ProductResult
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import com.prologic.strains.view.fragment.ProductInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListViewModel(application: Application) : AndroidViewModel(application),
    ProductItemListener {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    val productArray = ProductResult()
    var page = 1
    val selectedItem = MutableLiveData<Int>()
    val errorMessage = MutableLiveData<String>()
    val sortingName = MutableLiveData<String>()
    lateinit var categoryItem: CategoryItem
    val productAdapter = ProductAdapter(1)
    var isLoading: Boolean = false
    val progressBar = MutableLiveData<Int>(View.GONE)
    var sortingMenuArray = SortingMenu()

    init {
        val json = getAssetJsonAssets("sorting-menu.json")
        if (!json!!.isNullOrEmpty()) {
            sortingMenuArray = gson.fromJson(json, SortingMenu::class.java)
        }
        productAdapter.setOnItemListener(this)
        selectedItem.observeForever {
            sortingName.value = sortingMenuArray[it].name
            page = 1
            productArray.clear()
            getProductByCategory()
        }
    }

    fun updateAdapter() {
        productAdapter.setUpdateAdapter()
    }

    fun showPopupWindow(anchor: View) {
        val context = anchor.context
        val popupView: View = LayoutInflater.from(context).inflate(R.layout.shop_menu, null)
        val popupWindow = PopupWindow()
        popupWindow.setWindowLayoutMode(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val addViewLay = popupView.findViewById<LinearLayout>(R.id.addViewLay)

        for (i in 0 until sortingMenuArray.size) {
            val menuItem1 = sortingMenuArray[i]
            val v1 =
                LayoutInflater.from(context).inflate(R.layout.shop_menu_item, addViewLay, false)
            val addViewLay1 = v1.findViewById<LinearLayout>(R.id.addViewLay)
            val name1 = v1.findViewById<TextView>(R.id.name)
            val arrow1 = v1.findViewById<ImageView>(R.id.arrow)
            v1.id = i
            name1.text = menuItem1.name
            addViewLay1.visibility = View.GONE
            arrow1.visibility = View.GONE
            addViewLay.addView(v1)
            name1.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    popupWindow.dismiss()
                    selectedItem.value = v1.id
                }
            })
        }

        popupWindow.elevation = 20f
        popupWindow.setContentView(popupView)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(true)
        popupWindow.showAsDropDown(anchor, 0, 0, 50)
    }


    fun getProductByCategory() {
        if (productArray.size == 0)
            isLoaderVisible.postValue(true)
        else
            progressBar.value = View.VISIBLE
        val pos = selectedItem.value!!
        val orderModel = sortingMenuArray[pos]
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    apiRepository.getProducts(
                        categoryItem.id,
                        page,
                        orderModel.orderby,
                        orderModel.order
                    )
                }
            }.onSuccess {
                progressBar.value = View.GONE
                isLoading = false
                isLoaderVisible.postValue(false)
                if (it.size > 0) {
                    page++
                    productArray.addAll(it)
                    productAdapter.setUpdateAdapter(productArray)
                } else {
                    errorMessage.postValue("Oh No, there are no items in this category!")
                }

            }.onFailure {
                progressBar.value = View.GONE
                isLoaderVisible.postValue(false)
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