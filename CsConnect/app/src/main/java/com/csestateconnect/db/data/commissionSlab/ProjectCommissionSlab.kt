package com.csestateconnect.db.data.commissionSlab


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.AnyTypeConverters
import com.csestateconnect.db.data.typeconverter.CommissionTypeConverters
import com.csestateconnect.db.data.typeconverter.ProjectsTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(CommissionTypeConverters::class)
@Entity(tableName = "get_commission_slab")
data class ProjectCommissionSlab(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val commission: Int?,
    @SerializedName("commission_slabs")
    val commissionSlabs: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    val project: Project?,
    @SerializedName("relationship_manager")
    val relationshipManager: RelationshipManager?,
    @SerializedName("slabs_available")
    val slabsAvailable: Boolean?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val user: User?
)