package com.csestateconnect

class CreateDealData(lead: Int, dealStatus: Int, project: Int, totalCommission: String,
                     payableCommission: String, paidCommission: String, unpaidCommission: String,
                     soldArea: Float, commissionRate: Float, commissionPercent: Float){

    val lead = lead
    val deal_status = dealStatus
    val project = project
    val commission_amount_total_view = totalCommission
    val commission_amount_payable_view = payableCommission
    val commission_amount_paid_view = paidCommission
    val commission_amount_unpaid_view = unpaidCommission
    val sold_area_sq_ft = soldArea
    val commission_rate_sq_ft = commissionRate
    val commission_percentage = commissionPercent
}