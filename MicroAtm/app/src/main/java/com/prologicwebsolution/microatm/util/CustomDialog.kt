package com.prologicwebsolution.microatm.util


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.customview.LoadingSpinner


class CustomDialog( context: Context?) : Dialog(context!!) {
    private var txtDescription: TextView? = null
    var imgSpinner: LoadingSpinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState?: Bundle())
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_end_cash_drawer)
        initialize()
    }

    private fun initialize() {
        imgSpinner = findViewById(R.id.imgSpinner)
        txtDescription = findViewById(R.id.txtDescription)
        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            setCancelable(false)
        }
    }

    fun setMessage(msg: String?) {
        txtDescription!!.text = msg
    }
}