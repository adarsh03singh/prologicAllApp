package com.csestateconnect.db.data.homepagedata.banner.commission

data class GetHomeCommission(
    val commission_amount_total__sum: Int,
    val commission_amount_paid__sum: Int,
    val commission_amount_unpaid__sum: Int
)