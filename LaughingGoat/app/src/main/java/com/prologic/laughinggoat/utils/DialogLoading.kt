package com.prologic.laughinggoat.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.Html
import android.util.DisplayMetrics
import android.view.View

import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.prologic.laughinggoat.R

class DialogLoading {
    companion object {
        private var dialogLoading: Dialog? = null

        fun show(activity: Activity, message: String) {
            initilize(activity, message)
        }

        fun show(activity: Activity) {
            initilize(activity, "Please wait . . .")
        }

        fun hide() {
            if (dialogLoading != null)
                dialogLoading!!.dismiss()
        }

        private fun initilize(thisActivity: Activity, message: String) {
            if (dialogLoading != null)
                if (dialogLoading!!.isShowing)
                    dialogLoading!!.dismiss()
            dialogLoading = Dialog(thisActivity)
            dialogLoading!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            thisActivity.window.setBackgroundDrawableResource(R.color.trans)
            dialogLoading!!.window!!.setBackgroundDrawableResource(R.color.trans)
            val metrics = DisplayMetrics()
            val windowManager =
                thisActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(metrics)
            dialogLoading!!.setContentView(R.layout.loading)
            dialogLoading!!.setCancelable(false)
            val layout = dialogLoading!!.findViewById<LinearLayout>(R.id.layout)
            val params = layout.layoutParams
            params.width = metrics.widthPixels - metrics.widthPixels * 10 / 100
            layout.layoutParams = params
            val msg = dialogLoading!!.findViewById<TextView>(R.id.msg)
            msg.text = message
            dialogLoading!!.show()
        }

    }
}



