package com.blog.prologic.imageslider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blog.prologic.imageslider.constants.ActionTypes
import com.blog.prologic.imageslider.constants.ScaleTypes
import com.blog.prologic.imageslider.interfaces.ItemChangeListener
import com.blog.prologic.imageslider.interfaces.ItemClickListener
import com.blog.prologic.imageslider.interfaces.TouchListener
import com.blog.prologic.imageslider.models.SlideModel
import com.prologic.laughinggoat.R

class SliderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slider_activity)
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider) // init imageSlider
        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel("https://bit.ly/37Rn50u", "Baby Owl", ScaleTypes.CENTER_CROP))
        imageList.add(
            SlideModel(
                "https://bit.ly/2BteuF2",
                "Elephants and tigers may become extinct."
            )
        )
        imageList.add(
            SlideModel(
                "https://bit.ly/3fLJf72",
                "The population of elephants is decreasing in the world."
            )
        )

        imageSlider.setImageList(imageList)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here.
            }
        })

        imageSlider.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                //println("Pos: " + position)
            }
        })

        imageSlider.setTouchListener(object : TouchListener {
            override fun onTouched(touched: ActionTypes) {
                if (touched == ActionTypes.DOWN) {
                    imageSlider.stopSliding()
                } else if (touched == ActionTypes.UP) {
                    imageSlider.startSliding(1000)
                }
            }
        })
    }

}
