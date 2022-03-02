package com.prologicwebsolution.microatm.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.prologicwebsolution.microatm.repo.HomeRepository


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val homeRepository: HomeRepository
    val mid = "442000227361448"
    val tid = "42200333"

    init{
        homeRepository = HomeRepository(application)

    }
}