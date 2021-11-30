package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class ProjectDeveloper(
    @SerializedName("contact_no")
    val contactNo: String?,
    @SerializedName("developer_info")
    val developerInfo: String?,
    @SerializedName("head_office_address")
    val headOfficeAddress: String?,
    @SerializedName("head_office_city")
    val headOfficeCity: HeadOfficeCity?,
    @SerializedName("head_office_country")
    val headOfficeCountry: HeadOfficeCountry?,
    @SerializedName("head_office_location")
    val headOfficeLocation: HeadOfficeLocation?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val management: String?,
    val name: String?,
    @SerializedName("no_of_projects")
    val noOfProjects: Int?,
    @SerializedName("rera_no")
    val reraNo: String?
)