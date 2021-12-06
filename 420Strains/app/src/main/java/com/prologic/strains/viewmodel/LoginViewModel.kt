package com.prologic.strains.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.prologic.strains.network.Repository
import com.prologic.strains.utils.SharedPreference


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()






}


