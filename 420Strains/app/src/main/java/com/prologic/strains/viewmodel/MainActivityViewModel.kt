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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologic.strains.R
import com.prologic.strains.model.ErrorAlert
import com.prologic.strains.model.ShopMenu
import com.prologic.strains.model.auth.UserResult
import com.prologic.strains.model.category.CategoryItem
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import com.prologic.strains.view.fragment.ProductList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    val apiRepository = Repository()
    val sharedPreference = SharedPreference();
    val back_visibility = MutableLiveData<Int>(View.GONE)
    val menu_visibility = MutableLiveData<Int>(View.VISIBLE)
    val header_lay = MutableLiveData<Int>(View.VISIBLE)
    val search_visibility = MutableLiveData<Int>(View.VISIBLE)
    val shop_visibility = MutableLiveData<Int>(View.VISIBLE)
    val my_cart_visibility = MutableLiveData<Int>(View.VISIBLE)
    val title_text_visibility = MutableLiveData<Int>(View.VISIBLE)
    val login_button_visibility = MutableLiveData<Int>(View.VISIBLE)
    val user_lay_visibility = MutableLiveData<Int>(View.VISIBLE)
    val roomDatabase = getRoomDatabase().productRoomDao()
    val title_text = MutableLiveData<String>("")
    val user_name = MutableLiveData<String>("")
    val user_email = MutableLiveData<String>("")
    val cart_item_count: LiveData<Int> = roomDatabase.getCount()
    var userData = MutableLiveData<UserResult>(sharedPreference.getUser())
    var errorMessage = MutableLiveData<ErrorAlert>()
    var shopMenuArray = ShopMenu()

    init {

        val json = getAssetJsonAssets("shop-menu.json")
        if (!json!!.isNullOrEmpty()) {
            shopMenuArray = gson.fromJson(json, ShopMenu::class.java)
        }
    }

    fun setValue() {
        userData.value = sharedPreference.getUser()

    }

    fun getCustomerById() {
        val id = userData.value?.id
        if (id == null)
            return
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val userResultResult = apiRepository.getCustomerById(id)
                    userData.postValue(userResultResult)
                    sharedPreference.putString(user_data, gson.toJson(userResultResult))
                }
            }.onSuccess {

            }.onFailure {
                errorMessage.postValue(ErrorAlert(1, errorException(it)))
            }
        }
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
        shopMenuArray.forEach { menuItem1 ->
            val v1 =
                LayoutInflater.from(context).inflate(R.layout.shop_menu_item, addViewLay, false)
            val addViewLay1 = v1.findViewById<LinearLayout>(R.id.addViewLay)
            val name1 = v1.findViewById<TextView>(R.id.name)
            val arrow1 = v1.findViewById<ImageView>(R.id.arrow)
            name1.text = menuItem1.name
            addViewLay1.visibility = View.GONE
            if (menuItem1.data.size > 0) {
                arrow1.visibility = View.VISIBLE
                menuItem1.data.forEach { menuItem2 ->
                    val v2 = LayoutInflater.from(context)
                        .inflate(R.layout.shop_sub_menu_item, addViewLay1, false)
                    val name2 = v2.findViewById<TextView>(R.id.name)
                    name2.text = menuItem2.name
                    addViewLay1.addView(v2)
                    name2.setOnClickListener {
                        popupWindow.dismiss()
                        val bundle = Bundle()
                        bundle.putSerializable(
                            intent_data,
                            CategoryItem(menuItem2.id, menuItem2.name, "")
                        )
                        addFragment(ProductList(), bundle)
                    }
                }
            } else {
                arrow1.visibility = View.GONE
            }
            addViewLay.addView(v1)
            name1.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    if (menuItem1.data.size > 0) {
                        if (addViewLay1.visibility == View.VISIBLE)
                            addViewLay1.visibility = View.GONE
                        else
                            addViewLay1.visibility = View.VISIBLE
                    } else {
                        popupWindow.dismiss()
                        val bundle = Bundle()
                        bundle.putSerializable(intent_data, CategoryItem(menuItem1.id, menuItem1.name,""))
                        addFragment(ProductList(),bundle)
                    }
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

}