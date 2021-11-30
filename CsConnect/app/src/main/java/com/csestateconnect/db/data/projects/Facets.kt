package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class Facets(
    @SerializedName("_filter_amenities")
    val filterAmenities: FilterAmenities?,
    @SerializedName("_filter_area")
    val filterArea: FilterArea?,
    @SerializedName("_filter_bhk")
    val filterBhk: FilterBhk?,
    @SerializedName("_filter_bsp")
    val filterBsp: FilterBsp?,
    @SerializedName("_filter_locations")
    val filterLocations: FilterLocations?,
    @SerializedName("_filter_price")
    val filterPrice: FilterPrice?,
    @SerializedName("_filter_property")
    val filterProperty: FilterProperty?,
    @SerializedName("_filter_rooms")
    val filterRooms: FilterRooms?,
    @SerializedName("_filter_status")
    val filterStatus: FilterStatus?
)