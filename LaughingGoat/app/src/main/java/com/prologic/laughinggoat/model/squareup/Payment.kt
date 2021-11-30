package com.prologic.laughinggoat.model.squareup

data class Payment(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val amount_money: AmountMoney,
    val status: String,
    val delay_duration: String,
    val source_type: String,
    val card_details: CardDetails,
    val location_id: String,
    val order_id: String,
    val risk_evaluation: RiskEvaluation,
    val total_money: TotalMoney,
    val approved_money: ApprovedMoney,
    val receipt_number: String,
    val receipt_url: String,
    val delay_action: String,
    val delayed_until: String,
    val application_details: ApplicationDetails,
    val version_token: String
)