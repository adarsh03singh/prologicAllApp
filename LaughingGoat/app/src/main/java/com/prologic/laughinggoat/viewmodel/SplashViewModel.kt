package com.prologic.laughinggoat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.prologic.laughinggoat.BuildConfig

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    var version_name = MutableLiveData<String>("VERSION NAME : "+BuildConfig.VERSION_NAME+" ")

}