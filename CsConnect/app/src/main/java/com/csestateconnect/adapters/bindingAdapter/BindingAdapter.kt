package com.csestateconnect.adapters.bindingAdapter

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.csestateconnect.R

/*@BindingAdapter(value = ["app:imageUrl" , "app:context"], requireAll = true)
fun firstImageSet(view: ImageView, url: String?, context: Context){
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_person_black_24dp)
        .into(view)
}*/
@BindingAdapter("app:imageUrl" )
fun imageSet(view: ImageView, url: String?){
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.ic_person_black_24dp)
        .into(view)
}

@BindingAdapter("app:url" )
fun topProjectImageSet(view: ImageView, url: String?){
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.gallery_9)
        .into(view)
}
