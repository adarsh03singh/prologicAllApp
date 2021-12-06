package com.prologic.strains.model.create_order

import com.prologic.strains.model.auth.Billing
import com.prologic.strains.model.auth.Shipping


class CreateOrder {
    var customer_id: String? = null
    var payment_method: String? = null
    var payment_method_title: String? = null
    var set_paid: Boolean = true
    var billing: Billing? = null
    var shipping: Shipping? = null
    var line_items= ArrayList<ProductItem>()
    var coupon_lines = ArrayList<CouponLines>()

}