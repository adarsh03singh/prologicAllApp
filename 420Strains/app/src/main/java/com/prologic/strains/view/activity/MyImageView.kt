package com.prologic.strains.view.activity

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import com.prologic.strains.R
import com.prologic.strains.databinding.MyImageViewBinding
import com.prologic.strains.databinding.MyWebViewBinding
import com.prologic.strains.utils.*
import com.prologic.strains.viewmodel.MyWebViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_image_view.*


class MyImageView : AppCompatActivity() {
    lateinit var viewModel: MyWebViewModel
    lateinit var binding: MyImageViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.my_image_view)
        viewModel = ViewModelProvider(this).get(MyWebViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        var imageTitle = intent.getStringExtra("imageTitle")
        if (imageTitle == null)
            imageTitle = ""
        title_name.text = imageTitle
        back.setOnClickListener { onBackPressed() }
        loadImageOriginal(imageView, intent.getStringExtra("imageUrl")!!)
    }


}