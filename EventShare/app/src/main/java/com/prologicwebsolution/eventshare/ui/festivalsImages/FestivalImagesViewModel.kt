package com.prologicwebsolution.eventshare.ui.festivalsImages


import android.app.Application
import androidx.lifecycle.AndroidViewModel


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class FestivalImagesViewModel(application: Application) : AndroidViewModel(application) {

  /*  private val loginRepository: LoginRepository

    var userName  = MutableLiveData<String>().apply { postValue("") }
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

//        if (userName.value.isNullOrEmpty()){
//            Snackbar.make(view, "Please enter Username", Snackbar.LENGTH_LONG).show()
//        }
//        else if (password.value.isNullOrEmpty()){
//            Snackbar.make(view, "Please enter Password", Snackbar.LENGTH_LONG).show()
//        }
//        else {
//            loginClient(view)
//        }
    }


    fun loginClient(view: View){
        progressBar.value = View.VISIBLE
        buttonEabled.value = true
        var response : Response<LoginEntity>?
        Coroutines.io {

                Coroutines.main {
                    response = loginRepository.userLogin(userName.value!!, password.value)

                    if (response!!.isSuccessful) {
                        if (response?.body()?.status!!.equals("SUCCESS")) {
                            try {
                                val preferences = view.context.getSharedPreferences(
                                    "PREFERENCE_NAME",
                                    AppCompatActivity.MODE_PRIVATE
                                )
                                val editor = preferences.edit()
                                editor.putString(
                                    "user_id",
                                    response!!.body()!!.userdata.user_id.toString()
                                )
                                editor.apply()
                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                            }
                            progressBar.value = View.GONE
                            val activity = view.context as Activity
                            val intent = Intent(activity, MainActivity::class.java)
                            activity.startActivity(intent)
                            activity.finish()

//                        view.findNavController().navigate(R.id.dashboardFragment)
                            Snackbar.make(view, "Successfully Login", Snackbar.LENGTH_LONG).show()
                        } else
                            Snackbar.make(view, "${response?.body()?.msg}", Snackbar.LENGTH_LONG)
                                .show()
                        progressBar.value = View.GONE
                    }
                    else {
                            val jObjError = JSONObject(response!!.errorBody()!!.string())
                            val error = jObjError.getString("error")
                            val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                            snackbar.show()
                    }
                }
//            } else {
//                Coroutines.main {
//
//                    try {
//                        if (!response!!.errorBody()!!.equals(null)) {
////                            cancelablebutton.value = true
//                            val jObjError = JSONObject(response!!.errorBody()!!.string())
//                            val error = jObjError.getString("error")
//                            val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
//                            snackbar.show()
//
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//
//                    progressBar.value = View.GONE
//                }
//            }
        }
        buttonEabled.value = false
    }

   private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }*/

}