package com.prologic.strains.model.delivery


class DeliveryResult : ArrayList<DeliveryItem>()

data class DeliveryItem(
    val title: String,
    val page_id: String

)