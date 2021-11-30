package com.csestateconnect.db.data.favouriteProject


import androidx.room.Entity
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.FavouriteProjectsTypeConverters
import com.csestateconnect.db.data.typeconverter.ProjectsTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(FavouriteProjectsTypeConverters::class)
@Entity(tableName = "get_favourite_projects")
data class GetFavouriteProjectsEntity(
    @androidx.room.PrimaryKey(autoGenerate = false)
    val id: Int?,
    @SerializedName("favourite_projects")
    val favouriteProjects: List<FavouriteProject?>?

)
