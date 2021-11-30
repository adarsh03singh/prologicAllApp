package com.csestateconnect.ui

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.csestateconnect.R
import com.csestateconnect.adapters.WalkThroughPagerAdapter
import com.csestateconnect.databinding.ActivityWalkThroughBinding
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.viewmodel.AuthViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_walk_through.*
import kotlinx.android.synthetic.main.amenities_items.*

class WalkThroughActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

   private var snackbar: Snackbar? = null
    var viewModel:AuthViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_walk_through)

        val binding: ActivityWalkThroughBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_walk_through
        )
         viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        val viewPagerAdapter = WalkThroughPagerAdapter()
        walk_through_view_pager.adapter = viewPagerAdapter

        setPagerIndicator(viewPagerAdapter.count, 0, layout_dots)
        walk_through_view_pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                setPagerIndicator(viewPagerAdapter.count, position, layout_dots)
            }

        })

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
            snackBar()
            next_btn.isEnabled = false
        } else {
            viewModel!!.getContriesWithCities()
            snackbar?.dismiss()
            next_btn.isEnabled = true
            next_btn.setOnClickListener {
                click()
                next_btn.isEnabled = false
            }
        }
    }

    private fun setPagerIndicator(
        adapterLength: Int,
        currentPage: Int,
        indicatorLayout: LinearLayout
    ) {
        val indicator = arrayOfNulls<ImageView>(adapterLength)

        indicatorLayout.removeAllViews()
        for (i in indicator.indices) {
            indicator[i] = ImageView(this)

            if (i == currentPage) {
                indicator[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.active_view_pager_indicator
                    )
                )
            } else
                indicator[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.default_view_pager_indicator
                    )
                )
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(4, 0, 4, 0)
            indicatorLayout.addView(indicator[i], layoutParams)
        }
    }

    fun click() {
        val intent = Intent(this, GenerateOtpActivity::class.java)
        startActivity(intent)
        finish()
    }
   private fun snackBar(){
        snackbar = Snackbar.make(walkThrough_constraintLayout, "You are offline", Snackbar.LENGTH_LONG)
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        var view = snackbar!!.getView()
        view.setBackgroundColor(resources.getColor(R.color.colorchery))
        snackbar?.show()
    }

}
