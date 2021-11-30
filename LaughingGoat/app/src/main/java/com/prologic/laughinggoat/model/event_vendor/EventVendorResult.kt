package com.prologic.laughinggoat.model.event_vendor

class EventVendorResult : ArrayList<EventVendorItem>()

data class EventVendorItem(
    val content: String,
    val id: Int,
    val image_url: String,
    val title: String
)