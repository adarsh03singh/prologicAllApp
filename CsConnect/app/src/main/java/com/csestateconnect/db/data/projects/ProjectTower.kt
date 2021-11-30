package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class ProjectTower(
    val floors: Int?,
    val id: Int?,
    val name: String?,
    @SerializedName("total_no_of_units")
    val totalNoOfUnits: Int?,
    @SerializedName("tower_completion_status")
    val towerCompletionStatus: TowerCompletionStatus?,
    @SerializedName("tower_images")
    val towerImages: List<TowerImage?>?,
    @SerializedName("tower_phase")
    val towerPhase: TowerPhase?,
    @SerializedName("unit_plans")
    val unitPlans: List<UnitPlan?>?
)