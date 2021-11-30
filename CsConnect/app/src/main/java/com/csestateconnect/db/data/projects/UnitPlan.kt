package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class UnitPlan(
    @SerializedName("avg_cost")
    val avgCost: Int?,
    @SerializedName("avg_cost_view")
    val avgCostView: String?,
    @SerializedName("bhk_type")
    val bhkType: BhkType?,
    @SerializedName("built_up_area")
    val builtUpArea: Int?,
    @SerializedName("built_up_area_percentage")
    val builtUpAreaPercentage: Int?,
    @SerializedName("built_up_area_view")
    val builtUpAreaView: String?,
    @SerializedName("carpet_area")
    val carpetArea: Int?,
    @SerializedName("carpet_area_percentage")
    val carpetAreaPercentage: Int?,
    @SerializedName("carpet_area_view")
    val carpetAreaView: String?,
    @SerializedName("floor_type")
    val floorType: String?,
    @SerializedName("high_cost")
    val highCost: Int?,
    @SerializedName("high_cost_view")
    val highCostView: String?,
    @SerializedName("hybrid_high_cost")
    val hybridHighCost: Int?,
    @SerializedName("hybrid_high_cost_view")
    val hybridHighCostView: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    @SerializedName("low_cost")
    val lowCost: Int?,
    @SerializedName("low_cost_view")
    val lowCostView: String?,
    @SerializedName("property_type")
    val propertyType: PropertyType?,
    @SerializedName("super_area")
    val superArea: Int?,
    @SerializedName("super_area_percentage")
    val superAreaPercentage: Int?,
    @SerializedName("super_area_view")
    val superAreaView: String?,
    @SerializedName("total_high_cost")
    val totalHighCost: Int?,
    @SerializedName("total_high_cost_view")
    val totalHighCostView: String?,
    @SerializedName("total_low_cost")
    val totalLowCost: Int?,
    @SerializedName("total_low_cost_view")
    val totalLowCostView: String?,
    @SerializedName("unit_inside_area_components")
    val unitInsideAreaComponents: List<UnitInsideAreaComponent?>?,
    @SerializedName("unit_plan_component_prices")
    val unitPlanComponentPrices: List<UnitPlanComponentPrice?>?,
    @SerializedName("unit_plan_description")
    val unitPlanDescription: String?,
    @SerializedName("unit_plan_images")
    val unitPlanImages: List<UnitPlanImage?>?
)