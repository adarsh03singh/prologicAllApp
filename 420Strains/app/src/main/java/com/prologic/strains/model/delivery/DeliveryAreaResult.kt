package com.prologic.strains.model.delivery

data class DeliveryAreaResult(
    val content: Content,
    val date: String,
    val date_gmt: String,
    val excerpt: Excerpt,
    val id: String,
    val modified: String,
    val title: Title
)

data class Excerpt(
    val rendered: String
)

data class Title(
    val rendered: String
)

data class Content(
    val rendered: String
)