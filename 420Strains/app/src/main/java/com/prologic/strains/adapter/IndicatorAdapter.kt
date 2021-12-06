package com.prologic.strains.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.prologic.strains.R
import com.prologic.strains.utils.TAG
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*
import java.util.concurrent.TimeUnit

interface OnChangeListener {
    fun onPageSelected(position: Int)
}

class IndicatorAdapter() :
    RecyclerView.Adapter<IndicatorAdapter.MyViewHolder>() {
    private var size = 0
    private var tempCurrent = 0

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(size: Int) {
        this.size = size
        this.tempCurrent = 0
        notifyDataSetChanged()
        ini()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setIndicatorCurrent() {

        notifyDataSetChanged()
    }

    override fun getItemCount() = size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.indicator_slider, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        fun bindHolder(position: Int) {
            if (tempCurrent == position)
                image.setImageResource(R.drawable.circle_green)
            else
                image.setImageResource(R.drawable.circle_gray)
        }

    }

    var delay: Long = 1000
    var period: Long = 1500
    private var swipeTimer = Timer()
    var viewPager: ViewPager? = null
    private var changeListener: OnChangeListener? = null
    fun setChangeListener(changeListener: OnChangeListener) {
        this.changeListener = changeListener
    }

    fun startSliding() {
        stopSliding()
        scheduleTimer()
    }

    fun stopSliding() {
        swipeTimer.cancel()
        swipeTimer.purge()
    }

    private fun scheduleTimer() {
        if (size == 0)
            return
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (tempCurrent == size)
                tempCurrent = 0
            Log.d(TAG + "-Slider", "" + tempCurrent + "/" + size)
            viewPager?.setCurrentItem(tempCurrent++, true)
        }
        swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delay, period)
    }

    private fun ini() {
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                tempCurrent = position
                setIndicatorCurrent()
                changeListener?.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

}