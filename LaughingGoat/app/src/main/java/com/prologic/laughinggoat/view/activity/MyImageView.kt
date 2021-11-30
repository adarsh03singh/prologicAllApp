package com.prologic.laughinggoat.view.activity

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
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.MyImageViewBinding
import com.prologic.laughinggoat.databinding.MyWebViewBinding
import com.prologic.laughinggoat.utils.AlertError
import com.prologic.laughinggoat.utils.OnDialogCloseListener
import com.prologic.laughinggoat.utils.isNetworkAvailable
import com.prologic.laughinggoat.utils.showToast
import com.prologic.laughinggoat.viewmodel.MyWebViewModel
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
        loadImage(imageView, intent.getStringExtra("imageUrl")!!)
    }

    fun loadImage(imageView: ImageView, image_url: String) {
        Picasso.get()
            .load(image_url)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(imageView)
    }
}