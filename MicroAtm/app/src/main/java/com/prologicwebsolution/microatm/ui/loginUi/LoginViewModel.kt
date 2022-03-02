package com.prologicwebsolution.microatm.ui.loginUi

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.data.loginData.LoginEntity
import com.prologicwebsolution.microatm.network2.CreateLoginBody
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.repo.LoginRepository
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.util.fragmentActivity
import com.prologicwebsolution.microatm.util.hideSoftKeyBoard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.lang.NullPointerException


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class LoginViewModel(application: Application) : AndroidViewModel(application) {

     val loginRepository:  LoginRepository

    private var dialog: ProgressDialog? = null
    var userName  = MutableLiveData<String>().apply { postValue("") }
    var password = MutableLiveData<String>().apply { postValue("") }
    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var buttonEabled = MutableLiveData<Boolean>().apply { postValue(false) }


    init{
        loginRepository = LoginRepository(application)
    }
    fun loginClient(view: View){

        hideSoftKeyBoard(view)
        if (userName.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter Email", Snackbar.LENGTH_LONG).show()
        }
        else if (password.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter Password", Snackbar.LENGTH_LONG).show()
        }
        else {

            val paramLoginBody = CreateLoginBody()
            paramLoginBody.username = userName.value.toString()
            paramLoginBody.password = password.value.toString()
            showProgress(view)

            var response: Response<LoginEntity>? = null
            viewModelScope.launch {
                kotlin.runCatching {
                    withContext(Dispatchers.IO) {
                        response = loginRepository.userLogin(paramLoginBody)
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        dismissProgress()
                        if (response?.isSuccessful == true) {

                            if (response?.body()?.status!!.equals("SUCCESS")) {
                                val successMsg = response?.body()!!.msg

                                try {
                                    val preferences = view.context.getSharedPreferences(
                                        "PREFERENCE_NAME", AppCompatActivity.MODE_PRIVATE)
                                    val editor = preferences.edit()
                                    editor.putString("user_id", response!!.body()!!.userdata.user_id.toString())
                                    editor.apply()

                                } catch (e: NullPointerException) {
                                    e.printStackTrace()
                                }

                                showToast(view, successMsg)
                                goToMainActivity(view)
                            } else
                                showToast(view, response?.body()!!.msg)

                        } else {
                            showToast(view, response?.body()!!.msg)
                        }
                    }

                }.onFailure {
                    withContext(Dispatchers.Main) {
                        dismissProgress()
//                error.postValue(RetrofitClient.errorException(it))
                        showToast(view, RetrofitClient.errorException(it))
                    }

                }
            }
        }

    }

    fun showToast(view: View,msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    fun showProgress(view: View) {
        val activity = view.context as Activity
        dialog = ProgressDialog(activity)
        dialog!!.setMessage("Please wait.")
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun dismissProgress() {
        if (dialog != null)
            if (dialog!!.isShowing)
                dialog!!.dismiss()
    }

    fun goToMainActivity(view: View) {
        val activity = view.context as Activity
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

}