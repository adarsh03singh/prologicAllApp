package com.prologicwebsolution.microatm.ui

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologicwebsolution.microatm.database.MyDatabase
import com.prologicwebsolution.microatm.ui.dashboared.DashboardFragment
import com.prologicwebsolution.microatm.ui.loginUi.LoginActivity
import com.prologicwebsolution.microatm.util.MyProgressDialog
import com.prologicwebsolution.microatm.util.addFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val header_bar_lay = MutableLiveData<Int>(View.GONE)
    val back_button_visibility = MutableLiveData<Int>(View.GONE)
    val title_value_visibility = MutableLiveData<Int>(View.GONE)
    val title_value = MutableLiveData<String>("")


    fun setHeader(header: Boolean, isBack: Boolean, title: String) {
        title_value.value = title
        if (header) {
            header_bar_lay.value = View.VISIBLE
        } else {
            header_bar_lay.value = View.GONE
        }
        if (isBack && header) {
            title_value_visibility.value = View.VISIBLE
        } else {
            title_value_visibility.value = View.GONE
        }
        if (isBack)
            back_button_visibility.value = View.VISIBLE
        else
            back_button_visibility.value = View.GONE
    }

    fun setHideHeader() {
        title_value.value = ""
        header_bar_lay.value = View.GONE
        back_button_visibility.value = View.GONE

    }

}