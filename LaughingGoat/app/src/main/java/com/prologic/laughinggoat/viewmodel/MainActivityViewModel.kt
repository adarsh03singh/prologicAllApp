package com.prologic.laughinggoat.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.prologic.laughinggoat.databinding.MainActivityBinding
import com.prologic.laughinggoat.model.auth.UserResult

import com.prologic.laughinggoat.utils.SharedPreference
import com.prologic.laughinggoat.utils.getRoomDatabase


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val sharedPreference = SharedPreference();
    val back_visibility = MutableLiveData<Int>(View.GONE)
    val menu_visibility = MutableLiveData<Int>(View.VISIBLE)
    val header_lay = MutableLiveData<Int>(View.VISIBLE)
    val search_visibility = MutableLiveData<Int>(View.VISIBLE)
    val my_cart_visibility = MutableLiveData<Int>(View.VISIBLE)
    val title_text_visibility = MutableLiveData<Int>(View.VISIBLE)
    val login_button_visibility = MutableLiveData<Int>(View.VISIBLE)
    val user_lay_visibility = MutableLiveData<Int>(View.VISIBLE)
    val roomDatabase = getRoomDatabase().productRoomDao()
    val title_text = MutableLiveData<String>("")
    val user_name = MutableLiveData<String>("")
    val user_email = MutableLiveData<String>("")
    val cart_item_count: LiveData<Int> = roomDatabase.getCount()
    var user_data: UserResult? = null
    var binding: MainActivityBinding? = null


    fun setValue() {
        user_data = sharedPreference.getUser()
        if (user_data == null) {
            user_lay_visibility.value = View.GONE
            login_button_visibility.value = View.VISIBLE
            binding!!.ordersClick.visibility=View.GONE
            binding!!.profileClick.visibility=View.GONE
            binding!!.logoutClick.visibility=View.GONE
        } else {
            user_lay_visibility.value = View.VISIBLE
            login_button_visibility.value = View.GONE
            binding!!.ordersClick.visibility=View.VISIBLE
            binding!!.profileClick.visibility=View.VISIBLE
            binding!!.logoutClick.visibility=View.VISIBLE
            user_name.postValue(user_data!!.first_name + " " + user_data!!.last_name)
            user_email.postValue(user_data!!.email)
        }
    }


}