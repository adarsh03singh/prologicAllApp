package com.prologic.strains.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics

import android.view.Window
import android.view.WindowManager

import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.prologic.strains.R


object SuccessAlert {


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
        onDialogCloseListener: OnDialogCloseListener
    ) {
        initialize(thisActivity, message, false, onDialogCloseListener)
    }

    fun showHtml(
        thisActivity: Activity,
        message: String,
        onDialogCloseListener: OnDialogCloseListener
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
        dialog.setContentView(R.layout.success_alert)
        dialog.setCancelable(false)
        val layout = dialog.findViewById<FrameLayout>(R.id.layout)
        val params = layout.layoutParams
        params.width = metrics.widthPixels - metrics.widthPixels * 10 / 100
        layout.layoutParams = params
        val topView: ImageView = dialog.findViewById(R.id.topView)
        val msg: TextView = dialog.findViewById(R.id.msg)
        val ok: Button = dialog.findViewById(R.id.ok)

        ok.setOnClickListener {
            dialog.dismiss()
            onDialogCloseListener?.onClick()
        }
        if (htmlMsg) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                msg.text = Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY)
            } else {
                msg.text = Html.fromHtml(message)
            }
        } else {
            msg.text = message
        }
        dialog.show()
        mDialog = dialog
    }


}



