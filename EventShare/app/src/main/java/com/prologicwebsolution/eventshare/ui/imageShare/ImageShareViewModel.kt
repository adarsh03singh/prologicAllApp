package com.prologicwebsolution.eventshare.ui.imageShare


import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.prologicwebsolution.eventshare.repo.LoginRepository
import com.prologicwebsolution.microatm.util.Coroutines


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class ImageShareViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository: LoginRepository

    var userName = MutableLiveData<String>().apply { postValue("") }
    var userEmail = MutableLiveData<String>().apply { postValue("") }
    var userNumber = MutableLiveData<String>().apply { postValue("") }
    var name: String? = null
    var email: String? = null
    var number: String? = null

    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var buttonEabled = MutableLiveData<Boolean>().apply { postValue(false) }

    init {
        loginRepository = LoginRepository(application)
        getuserDataFromDatabase()
    }

    fun getuserDataFromDatabase() {
        Coroutines.io {
            loginRepository.getallUserDataDatabase().forEach {
                name = it.userdata.name
                email = it.userdata.email
                number = it.userdata.phone
            }
            Coroutines.main {
                userName.value = name
                userEmail.value = email
                userNumber.value = number
            }
        }

    }

}