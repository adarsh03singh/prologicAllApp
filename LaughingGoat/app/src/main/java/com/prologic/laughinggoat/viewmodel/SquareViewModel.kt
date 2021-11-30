package com.prologic.laughinggoat.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.prologic.laughinggoat.databinding.MainActivityBinding
import com.prologic.laughinggoat.model.auth.UserResult

import com.prologic.laughinggoat.utils.SharedPreference
import com.prologic.laughinggoat.utils.getRoomDatabase


class SquareViewModel(application: Application) : AndroidViewModel(application) {

    val sharedPreference = SharedPreference();


}