package com.csestateconnect.db.data.assignedProjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.AssignedProjectsTypeConverters

@TypeConverters(AssignedProjectsTypeConverters::class)
@Entity(tableName = "assigned_projects")
data class GetAssignedProjects(
    @PrimaryKey(autoGenerate = false)
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>?
)