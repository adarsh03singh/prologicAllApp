package com.prologic.laughinggoat.model.squareup

data class Card(
    val card_brand: String,
    val last_4: String,
    val exp_month: Int,
    val exp_year: Int,
    val fingerprint: String,
    val card_type: String,
    val prepaid_type: String,
    val bin: String
)