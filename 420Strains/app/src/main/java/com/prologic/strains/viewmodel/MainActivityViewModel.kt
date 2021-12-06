package com.prologic.strains.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.prologic.strains.databinding.ActivityMainBinding
import com.prologic.strains.model.ErrorAlert
import com.prologic.strains.model.auth.UserResult
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.SharedPreference
import com.prologic.strains.utils.getRoomDatabase
import com.prologic.strains.utils.user_data
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
                    sharedPreference.putString(user_data, Gson().toJson(userResultResult))
                }
            }.onSuccess {

            }.onFailure {
                errorMessage.postValue(ErrorAlert(1, errorException(it)))
            }
        }
    }

}