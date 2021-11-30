package com.prologicwebsolution.microatm.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.FragmentActivity

class MyProgressDialog {
    companion object {
        private var dialog: ProgressDialog? = null
        fun show(context: Context) {
            dialog = ProgressDialog(context)
            dialog!!.setMessage("Please wait.")
            dialog!!.setCancelable(false)
            dialog!!.show()
        }
        fun show(activity: FragmentActivity) {
            dialog = ProgressDialog(activity)
            dialog!!.setMessage("Please wait.")
            dialog!!.setCancelable(false)
            dialog!!.show()
        }
        fun setMessage(msg: String) {
            dialog!!.setMessage(msg)
        }

        fun setCancelable(value: Boolean) {
            dialog!!.setCancelable(value)
        }

        fun dismiss() {
            if (dialog != null)
                if (dialog!!.isShowing)
                    dialog!!.dismiss()
        }
    }
}