package com.prologic.laughinggoat.network

import com.blog.prologic.model.country_state.CountryState
import com.prologic.laughinggoat.utils.SharedPreference
import com.google.gson.Gson
import com.prologic.laughinggoat.model.FloralGalleryResult

import com.prologic.laughinggoat.model.auth.Customer
import com.prologic.laughinggoat.model.auth.LoginResult
import com.prologic.laughinggoat.model.auth.RegisterRequest
import com.prologic.laughinggoat.model.auth.UserResult
import com.prologic.laughinggoat.model.blog.BlogResult
import com.prologic.laughinggoat.model.category.CategoryResult
import com.prologic.laughinggoat.model.coupon.CouponResult
import com.prologic.laughinggoat.model.create_order.CreateOrder
import com.prologic.laughinggoat.model.create_order.OrderResponse
import com.prologic.laughinggoat.model.event_vendor.EventVendorResult
import com.prologic.laughinggoat.model.order_list.OrdersResult
import com.prologic.laughinggoat.model.product.ProductResult
import com.prologic.laughinggoat.model.shipping.ShippingResult


import com.prologic.laughinggoat.model.slider.SliderResult
import com.prologic.laughinggoat.model.squareup.SquareRequest
import com.prologic.laughinggoat.model.squareup.SquareResult
import com.prologic.laughinggoat.model.taxes.TaxesResult
import com.prologic.laughinggoat.utils.UrlValue.Companion.squareup_payment
import retrofit2.Response
import retrofit2.http.Body

class Repository {
    val sharedPreference = SharedPreference()

    suspend fun getCustomerLogin(username: String, password: String): Response<LoginResult> {
        return getClient().getCustomerLogin(username, password)
    }

    suspend fun getCustomerByEmail(email: String): Customer {
        return getClient().getCustomerByEmail(email).body()!!
    }

    suspend fun getCustomerById(id: String): UserResult {
        return getClient().getCustomerById(id).body()!!
    }

    suspend fun createCustomer(registerRequest: RegisterRequest): Response<UserResult> {
        return getClient().createCustomer(registerRequest)
    }

    suspend fun updateCustomer(id: String, registerRequest: RegisterRequest): Response<UserResult> {
        val result = getClient().updateCustomer(id, registerRequest)
        //val responsne = result.body()!!
        return result
    }

    suspend fun getCategory(): CategoryResult {
        val response = getClient().getCategory().body()!!
        sharedPreference.putString("category_data", Gson().toJson(response))
        return response
    }

    suspend fun getProductByCategory(category: String): ProductResult {
        val response = getClient().getProductByCategory(category).body()!!
        sharedPreference.putString("product_" + category, Gson().toJson(response))
        return response
    }

    suspend fun getBlog(): BlogResult {
        val response = getClient().getBlog().body()!!
        sharedPreference.putString("blog", Gson().toJson(response))
        return response
    }

    suspend fun getEventVendor(): EventVendorResult {
        val response = getClient().getEventVendor().body()!!
        sharedPreference.putString("event_vendor", Gson().toJson(response))
        return response
    }

    suspend fun getSlider(): SliderResult {
        val response = getClient().getSlider().body()!!
        sharedPreference.putString("slider_data", Gson().toJson(response))
        return response
    }

    suspend fun getFloralGallery(): FloralGalleryResult {
        val response = getClient().getFloralGallery().body()!!
        sharedPreference.putString("floral_gallery", Gson().toJson(response))
        return response
    }

    suspend fun getTaxes(): TaxesResult {
        return getClient().getTaxes().body()!!
    }

    suspend fun getShippingMethod(): ShippingResult {
        val response = getClient().getShippingMethod().body()!!
        return response
    }

    suspend fun getCoupon(coupon_code: String): CouponResult {
        val response = getClient().getCoupon(coupon_code).body()!!
        return response
    }

    suspend fun createOrder(createOrder: CreateOrder): OrderResponse {
        val response = getClient().createOrder(createOrder).body()!!
        return response
    }

    suspend fun updateSquarePayment(squareRequest: SquareRequest): Response<SquareResult> {
        val response = getClientUrl(squareup_payment).updateSquarePayment(squareRequest)
        return response
    }

    suspend fun getCountryState(): CountryState {
        val response = getClient().getCountryState().body()!!
        sharedPreference.putString("country_state", Gson().toJson(response))
        return response
    }

    suspend fun getFeaturedProduct(): ProductResult {
        val response = getClient().getFeaturedProduct(true).body()!!
        sharedPreference.putString("featured_product", Gson().toJson(response))
        return response
    }

    suspend fun getOrders(customer_id: String): OrdersResult {
        val response = getClient().getOrders(customer_id).body()!!
        sharedPreference.putString("orders_" + customer_id, Gson().toJson(response))
        return response
    }
}