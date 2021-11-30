package com.prologic.laughinggoat.model.squareup

data class SquareRequest(
    val idempotency_key: String,
    val source_id: String,
    val amount_money: AmountMoney
)

