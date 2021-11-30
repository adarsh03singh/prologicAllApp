package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class UnitPlanComponentPrice(
    @SerializedName("component_price")
    val componentPrice: Float?,
    @SerializedName("component_price_view")
    val componentPriceView: String?,
    val id: Int?,
    @SerializedName("price_component_type")
    val priceComponentType: PriceComponentType?
)