package com.prologicwebsolution.eventshare.ui.login


import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.eventshare.data.loginData.UserLoginDataEntity
import com.prologicwebsolution.eventshare.repo.LoginRepository
import com.prologicwebsolution.eventshare.ui.MainActivity
import com.prologicwebsolution.microatm.util.Coroutines
import org.json.JSONObject
import retrofit2.Response


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository: LoginRepository

    var userEmail  = MutableLiveData<String>().apply { postValue("") }
    var password = MutableLiveData<String>().apply { postValue("") }

    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var buttonEabled = MutableLiveData<Boolean>().apply { postValue(false) }

    init{
        loginRepository = LoginRepository(application)

    }

    fun callLoginApiAfterCheckInternet(view: View) {
        if (isNetworkConnected(view.context!!)) {
            try {
                checkfieldsForLogin(view)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(view.context, "No internet found.", Toast.LENGTH_LONG).show()
        }
    }

    fun checkfieldsForLogin(view: View){
        val activity = view.context as Activity
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()

//        if (userEmail.value.isNullOrEmpty()){
//            Toast.makeText(view.context, "Please enter Email", Toast.LENGTH_SHORT).show()
//        }
//        else if (password.value.isNullOrEmpty()){
//            Toast.makeText(view.context, "Please enter Password", Toast.LENGTH_SHORT).show()
//        }
//        else {
//            loginClient(view)
//        }
    }


    fun loginClient(view: View){
        progressBar.value = View.VISIBLE
        buttonEabled.value = true
        var response : Response<UserLoginDataEntity>?
        Coroutines.io {

                Coroutines.main {
                    response = loginRepository.userLogin(userEmail.value.toString(), password.value.toString())

                    if (response!!.isSuccessful) {
                        if (response?.body()?.status!!.equals("SUCCESS")) {
                            try {

                              val userName = response!!.body()!!.userdata.name


                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                            }
                            progressBar.value = View.GONE

                            Toast.makeText(view.context, "Successfully Login", Toast.LENGTH_LONG).show()
                            //go to MainActivity
                            goToMainActivity(view)

                        } else
                            Toast.makeText(view.context, "${response?.body()!!.msg}", Toast.LENGTH_LONG)
                                .show()
                        progressBar.value = View.GONE
                    }
                    else {
                        try {
                            val jObjError = JSONObject(response!!.errorBody()!!.string())
                            val error = jObjError.getString("error")
                            val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                            snackbar.show()
                        }catch (e: java.lang.Exception){
                            e.printStackTrace()
                        }

                    }
                }

        }
        buttonEabled.value = false
    }

    fun goToMainActivity(view: View){
        val activity = view.context as Activity
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

   private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}