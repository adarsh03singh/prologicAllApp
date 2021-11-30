package com.prologic.laughinggoat.model

class FloralGalleryResult : ArrayList<FloralGalleryItem>()
data class FloralGalleryItem(
    val content: String,
    val id: String,
    val title: String
)