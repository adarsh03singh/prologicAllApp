package com.prologicwebsolution.eventshare.ui.imageList


import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.prologicwebsolution.eventshare.repo.LoginRepository


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class ImageListViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository: LoginRepository

    var userName  = MutableLiveData<String>().apply { postValue("") }
    var password = MutableLiveData<String>().apply { postValue("") }

    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var buttonEabled = MutableLiveData<Boolean>().apply { postValue(false) }

    init{
        loginRepository = LoginRepository(application)

    }

   /* fun callLoginApiAfterCheckInternet(view: View) {
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
*/
   /* fun checkfieldsForLogin(view: View){
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


   private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }*/

}