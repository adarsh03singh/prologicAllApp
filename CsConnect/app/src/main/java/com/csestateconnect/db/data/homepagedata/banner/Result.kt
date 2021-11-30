package com.csestateconnect.db.data.homepagedata.banner

import com.csestateconnect.db.data.homepagedata.banner.City
import com.csestateconnect.db.data.homepagedata.banner.Country
import com.csestateconnect.db.data.homepagedata.banner.Project

data class Result(
    val id: Int,
    val country: Country,
    val city: City,
    val project: Project,
    val priority: Int,
    val banner_image: String,
    val low_cost_view: String?,
    val high_cost_view: String?
)