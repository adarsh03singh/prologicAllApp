package com.prologic.laughinggoat.model.squareup

data class CardDetails(
    val status: String,
    val card: Card,
    val entry_method: String,
    val cvv_status: String,
    val avs_status: String,
    val statement_description: String,
    val card_payment_timeline: CardPaymentTimeline
)