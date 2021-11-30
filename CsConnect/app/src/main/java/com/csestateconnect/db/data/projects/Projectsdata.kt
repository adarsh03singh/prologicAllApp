package com.csestateconnect.db.data.projects


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.ProjectsTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(ProjectsTypeConverters::class)
@Entity(tableName = "get_projects")
data class Projectsdata(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val facets: Facets?,
    val next: String?,
    val previous: String?,
    val results: List<Result?>?
)