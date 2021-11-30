package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class Result(
    val address: String?,
    val city: City?,
    val country: Country?,
    @SerializedName("created_at")
    val createdAt: String?,
    val distances: List<Distance?>?,
    val documents: List<Document?>?,
    @SerializedName("high_commission")
    val highCommission: Boolean?,
    @SerializedName("high_cost")
    val highCost: Int?,
    @SerializedName("high_cost_view")
    val highCostView: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val location: Location?,
    @SerializedName("low_cost")
    val lowCost: Int?,
    @SerializedName("low_cost_view")
    val lowCostView: String?,
    @SerializedName("maximum_size")
    val maximumSize: Int?,
    @SerializedName("maximum_size_view")
    val maximumSizeView: String?,
    @SerializedName("minimum_size")
    val minimumSize: Int?,
    @SerializedName("minimum_size_view")
    val minimumSizeView: String?,
    val name: String?,
    @SerializedName("possession_month")
    val possessionMonth: Int?,
    @SerializedName("possession_year")
    val possessionYear: Int?,
    @SerializedName("project_amenities")
    val projectAmenities: List<ProjectAmenity?>?,
    @SerializedName("project_brochure")
    val projectBrochure: String?,
    @SerializedName("project_bsp_cost")
    val projectBspCost: Int?,
    @SerializedName("project_bsp_cost_view")
    val projectBspCostView: String?,
    @SerializedName("project_class_name")
    val projectClassName: String?,
    @SerializedName("project_completion_status")
    val projectCompletionStatus: ProjectCompletionStatus?,
    @SerializedName("project_developer")
    val projectDeveloper: ProjectDeveloper?,
    @SerializedName("project_display_priority")
    val projectDisplayPriority: Int?,
    @SerializedName("project_images")
    val projectImages: List<ProjectImage?>?,
    @SerializedName("project_info")
    val projectInfo: String?,
    @SerializedName("project_landmarks")
    val projectLandmarks: List<ProjectLandmark?>?,
    @SerializedName("project_launch_month")
    val projectLaunchMonth: Int?,
    @SerializedName("project_launch_year")
    val projectLaunchYear: Int?,
    @SerializedName("project_open_area")
    val projectOpenArea: Int?,
    @SerializedName("project_open_area_percentage")
    val projectOpenAreaPercentage: Int?,
    @SerializedName("project_open_area_view")
    val projectOpenAreaView: String?,
    @SerializedName("project_rera_number")
    val projectReraNumber: String?,
    @SerializedName("project_specifications_and_others")
    val projectSpecificationsAndOthers: List<ProjectSpecificationsAndOther?>?,
    @SerializedName("project_total_area")
    val projectTotalArea: Int?,
    @SerializedName("project_total_area_view")
    val projectTotalAreaView: String?,
    @SerializedName("project_towers")
    val projectTowers: List<ProjectTower?>?,
    @SerializedName("project_wow_factors")
    val projectWowFactors: List<ProjectWowFactor?>?,
    val quarter: Int?,
    @SerializedName("tower_count")
    val towerCount: Int?,
    @SerializedName("updated_at")
    val updatedAt: String?
)