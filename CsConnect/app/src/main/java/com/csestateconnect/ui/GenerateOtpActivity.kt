package com.csestateconnect.ui

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.ActivityGenerateBinding
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.viewmodel.AuthViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_generate.*
import kotlinx.android.synthetic.main.activity_walk_through.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class GenerateOtpActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var snackbar: Snackbar? = null
    var  viewModel: AuthViewModel? = null
    internal var country = arrayOf("INDIA +91")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityGenerateBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_generate
        )
            spinnerSelected()

         viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel!!.generateOtpButtonCount.observe(this, Observer {value ->

            if(value == 0){
               setButtonTrueFunction()
            }else
                setButtonFalseFunction()
        })

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        viewModel!!.numberText.observe(this, Observer { newScore ->
        })

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }
    private fun showNetworkMessage(isConnected: Boolean) {

        if (!isConnected) {
            generateOtp_button.isEnabled = false
            generate_check_box.isEnabled = false
            snackBar()

        } else {

            generate_check_box.isEnabled = true
            snackbar?.dismiss()
            if( generate_check_box.isChecked){
                generateOtp_button.isEnabled = true
            }

        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    fun spinnerSelected()
    {

        val spinnerArrayAdapter = ArrayAdapter<String>(
            applicationContext,
            R.layout.custom_spinner_dropdown_item,country
        )
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner_country_code.adapter = spinnerArrayAdapter

    }

    fun setButtonTrueFunction(){
        generateOtp_button.isEnabled = true
        generateOtp_button.setBackgroundColor(resources.getColor(R.color.buttonColor))
        generateOtp_button.setTextColor(resources.getColor(R.color.whiteColor))
    }
    fun setButtonFalseFunction(){
        generateOtp_button.isEnabled = false
        generateOtp_button.setBackgroundColor(resources.getColor(R.color.smokeColor))
        generateOtp_button.setTextColor(resources.getColor(R.color.smokeColor))
    }
    private fun snackBar(){
        snackbar = Snackbar.make(generate_constraint, "You are offline", Snackbar.LENGTH_LONG)
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        var view = snackbar!!.getView()
        view.setBackgroundColor(resources.getColor(R.color.colorchery))
        snackbar?.show()
    }
}
