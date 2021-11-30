package com.prologic.laughinggoat.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.Html
import android.util.DisplayMetrics
import android.view.View

import android.view.Window
import android.view.WindowManager

import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.prologic.laughinggoat.R

class AlertError {
    companion object {
        private var mDialog: Dialog? = null

        fun show(
            thisActivity: Activity,
            message: String
        ) {
            initialize(thisActivity, message, false, null)
        }

        fun showHtml(
            thisActivity: Activity,
            message: String
        ) {
            initialize(thisActivity, message, true, null)
        }

        fun show(
            thisActivity: Activity,
            message: String,
            onDialogCloseListener: OnDialogCloseListener?
        ) {
            initialize(thisActivity, message, false, onDialogCloseListener)
        }

        fun showHtml(
            thisActivity: Activity,
            message: String,
            onDialogCloseListener: OnDialogCloseListener?
        ) {
            initialize(thisActivity, message, true, onDialogCloseListener)
        }

        private fun initialize(
            thisActivity: Activity,
            message: String,
            htmlMsg: Boolean,
            onDialogCloseListener: OnDialogCloseListener?
        ) {
            var dialog = mDialog
            if (dialog != null)
                if (dialog.isShowing)
                    dialog.dismiss()
            dialog = Dialog(thisActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            thisActivity.window.setBackgroundDrawableResource(R.color.trans)
            dialog.window!!.setBackgroundDrawableResource(R.color.trans)
            val metrics = DisplayMetrics()
            val windowManager =
                thisActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(metrics)
            dialog.setContentView(R.layout.alert_error_dialog)
            dialog.setCancelable(false)
            val layout = dialog.findViewById<LinearLayout>(R.id.alerterror)
            val params = layout.layoutParams
            params.width = metrics.widthPixels - metrics.widthPixels * 10 / 100
            layout.layoutParams = params
            val topViewTv = dialog.findViewById<TextView>(R.id.topViewTv)
            val msg = dialog.findViewById<TextView>(R.id.msg)
            val cancel = dialog.findViewById<Button>(R.id.cancel)

            cancel.setOnClickListener {
                dialog.dismiss()
                onDialogCloseListener?.onClick()
            }
            val shake = AnimationUtils.loadAnimation(thisActivity, R.anim.shake)
            topViewTv.startAnimation(shake)
            if (htmlMsg) msg.text = Html.fromHtml(message) else msg.text = message
            dialog.show()
            mDialog =dialog
        }
    }
}





