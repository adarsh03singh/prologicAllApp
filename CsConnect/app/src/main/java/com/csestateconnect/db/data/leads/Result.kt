package com.csestateconnect.db.data.leads

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.LeadsResultTypeConverters

@TypeConverters(LeadsResultTypeConverters::class)
@Entity(tableName = "lead_result")
data class Result(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val max_budget: Int?,
    val max_budget_view: String?,
    val min_budget: Int?,
    val min_budget_view: String?,
    val budget_currency: String?,
    val phone_number: String,
    val email: String?,
    val country: Country?,
    val city: City?,
    val location: Location?,
    val quality: Float?,
    val lead_remarks: String?,
    val lead_status: LeadStatus,
    val feedback_comment: String?,
    val used: Boolean,
    val feedback_submitted: Boolean,
    val assigned: Boolean,
    val self_generated: Boolean,
    val submitted: Boolean,
    val managed_by_rm: ManagedByRm,
    val get_activities: List<CreateLeadActivity>?,
    val get_interaction_dates: List<CreateInteractionDate>?,
    val user: User,
    val created_at: String,
    val updated_at: String,
    val tags: Tags,
    val preferred_location: List<PreferredLocation?>?,
    val preferred_status: List<PreferredStatu?>?,
    val preferred_property_type: List<PreferredPropertyType?>?,
    val get_deal: Any
)