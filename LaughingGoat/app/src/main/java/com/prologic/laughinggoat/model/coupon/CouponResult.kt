package com.prologic.laughinggoat.model.coupon

class CouponResult : ArrayList<CouponItem>()

data class CouponItem(
    val amount: String,
    val code: String,
    val description: String,
    val discount_type: String,
    val free_shipping: Boolean,
    val id: Int,
    val minimum_amount: String

)