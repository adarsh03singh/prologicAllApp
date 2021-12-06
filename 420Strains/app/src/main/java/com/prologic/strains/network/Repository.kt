package com.prologic.strains.network


import com.blog.prologic.model.country_state.CountryState
import com.google.gson.Gson

import com.prologic.strains.model.auth.*

import com.prologic.strains.model.category.CategoryResult
import com.prologic.strains.model.coupon.CouponResult
import com.prologic.strains.model.create_order.CreateOrder
import com.prologic.strains.model.create_order.OrderResponse
import com.prologic.strains.model.delivery.DeliveryAreaResult
import com.prologic.strains.model.delivery.DeliveryResult

import com.prologic.strains.model.order_list.OrdersResult
import com.prologic.strains.model.product.ProductResult

import com.prologic.strains.model.slider.SliderResult

import com.prologic.strains.utils.SharedPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response


class Repository {
    val sharedPreference = SharedPreference()

    suspend fun getCustomerLogin(username: String, password: String): Response<LoginResult> {
        return getClient().getCustomerLogin(username, password)
    }

    suspend fun getCustomerByEmail(email: String): Customer {
        return getClient().getCustomerByEmail(email).body()!!
    }

    suspend fun getDeliveryArea(): DeliveryResult {
        val response = getClient().getDeliveryArea().body()!!
        sharedPreference.putString("delivery_area", Gson().toJson(response))
        return response
    }

    suspend fun getDeliveryArea(id: String): DeliveryAreaResult {
        val response = getClient().getDeliveryArea(id).body()!!
        return response
    }

    suspend fun getCustomerById(id: String): UserResult {
        return getClient().getCustomerById(id).body()!!
    }

    suspend fun getProductBySearch(search: String): ProductResult {
        val response = getClient().getProductBySearch(search).body()!!
        return response
    }

    suspend fun createCustomer(
        partRequest: HashMap<String, RequestBody>,
        fileKey1: MultipartBody.Part,
        fileKey2: MultipartBody.Part?
    ): Response<RegisterResult> {
        return getClient().createCustomer(partRequest, fileKey1, fileKey2)
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




    suspend fun getSlider(): SliderResult {
        val response = getClient().getSlider().body()!!
        sharedPreference.putString("slider_data", Gson().toJson(response))
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