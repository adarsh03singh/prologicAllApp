package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class TowerPhase(
    val floors: Int?,
    val id: Int?,
    val name: String?,
    @SerializedName("no_of_booked_units")
    val noOfBookedUnits: Int?,
    @SerializedName("no_of_total_units")
    val noOfTotalUnits: Int?,
    @SerializedName("percentage_of_booked_units")
    val percentageOfBookedUnits: Int?,
    @SerializedName("phase_completion_status")
    val phaseCompletionStatus: PhaseCompletionStatus?,
    @SerializedName("phase_images")
    val phaseImages: List<PhaseImage?>?,
    @SerializedName("phase_launch_month")
    val phaseLaunchMonth: Int?,
    @SerializedName("phase_launch_year")
    val phaseLaunchYear: Int?,
    @SerializedName("phase_rera_number")
    val phaseReraNumber: String?,
    @SerializedName("possession_month")
    val possessionMonth: Int?,
    @SerializedName("possession_year")
    val possessionYear: Int?
)