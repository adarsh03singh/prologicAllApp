package com.csestateconnect.ui


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.ActivityValidateOtpBinding
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.viewmodel.AuthViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_generate.*
import kotlinx.android.synthetic.main.activity_validate_otp.*
import kotlinx.android.synthetic.main.activity_walk_through.*

class ValidateOtpActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityValidateOtpBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_validate_otp
        )
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        binding.viewmodelVal = viewModel
        binding.lifecycleOwner = this
        viewModel.generateOtpButtonCount.observe(this, Observer {value ->

            if(value == 0){
                setValButtonTrueFunction()
            }else
                setValButtonFalseFunction()
        })
        viewModel.startTimer()
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun showNetworkMessage(isConnected: Boolean) {

        if (!isConnected) {
            resend_button.visibility = View.GONE
            valotp_view.isEnabled = false
            otp_verify_button.isEnabled = false
            snackBar()

        } else {
            resend_button.visibility = View.VISIBLE
            valotp_view.isEnabled = true
            if( valotp_view != null){
                otp_verify_button.isEnabled = true
            }

            snackbar?.dismiss()

        }
    }
    fun setValButtonTrueFunction(){
        otp_verify_button.isEnabled = true
        otp_verify_button.setBackgroundColor(resources.getColor(R.color.buttonColor))
        otp_verify_button.setTextColor(resources.getColor(R.color.whiteColor))
    }
    fun setValButtonFalseFunction(){
        otp_verify_button.isEnabled = false
        otp_verify_button.setBackgroundColor(resources.getColor(R.color.smokeColor))
        otp_verify_button.setTextColor(resources.getColor(R.color.smokeColor))
    }

    fun snackBar(){
        snackbar = Snackbar.make(validate_constraint, "You are offline", Snackbar.LENGTH_LONG)
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        var view = snackbar!!.getView()
        view.setBackgroundColor(resources.getColor(R.color.colorchery))
        snackbar?.show()
    }
}
