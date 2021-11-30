package com.csestateconnect.ui.navdrawer.calenderEvents

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.csestateconnect.R
import kotlinx.android.synthetic.main.activity_notification_message.*

class NotificationMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_message)

        val bundle = intent.extras
        subject_notification_text.setText(bundle.getString("subject"))
        description_notification_text.setText(bundle.getString("descript"))
        priority_notification_text.setText(bundle.getString("priority"))
        category_notification_text.setText(bundle.getString("category"))
    }
}