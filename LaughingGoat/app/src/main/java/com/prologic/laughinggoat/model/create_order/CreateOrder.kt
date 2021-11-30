package com.prologic.laughinggoat.model.create_order

import com.prologic.laughinggoat.model.auth.Billing
import com.prologic.laughinggoat.model.auth.Shipping


class CreateOrder {
    var customer_id: String? = null
    var payment_method: String? = null
    var payment_method_title: String? = null
    var set_paid: Boolean = true
    var billing: Billing? = null
    var shipping: Shipping? = null
    var line_items= ArrayList<ProductItem>()
    var shipping_lines= ArrayList<ShippingLine>()
    var tax_lines = ArrayList<TaxLines>()
    var coupon_lines = ArrayList<CouponLines>()

}