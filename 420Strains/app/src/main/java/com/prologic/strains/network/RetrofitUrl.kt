package com.prologic.strains.network

import com.blog.prologic.model.country_state.CountryState

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

import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Response
import retrofit2.http.*

interface RetrofitUrl {
    @Multipart
    @POST("wp-json/wp/v2/users/register")
    suspend fun createCustomer(
        @PartMap partRequest: HashMap<String, RequestBody>,
        @Part fileKey1: MultipartBody.Part,
        @Part fileKey2: MultipartBody.Part?
    ): Response<RegisterResult>

    @POST("wp-json/wc/v3/customers/{id}")
    suspend fun updateCustomer(
        @Path("id") id: String,
        @Body registerRequest: RegisterRequest
    ): Response<UserResult>

    @FormUrlEncoded
    @POST("wp-json/jwt-auth/v1/token?")
    suspend fun getCustomerLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResult>

    @GET("wc-api/v3/customers/email/{email}")
    suspend fun getCustomerByEmail(@Path("email") email: String): Response<Customer>

    @GET("wp-json/wp/v2/footer/menu")
    suspend fun getDeliveryArea(): Response<DeliveryResult>

    @GET("wp-json/wp/v2/pages/{id}")
    suspend fun getDeliveryArea(@Path("id") id: String): Response<DeliveryAreaResult>

    @GET("wp-json/wc/v3/customers/{id}")
    suspend fun getCustomerById(@Path("id") id: String): Response<UserResult>

    @GET("wp-json/wc/v3/products/categories?")
    suspend fun getCategory(): Response<CategoryResult>

    @GET("wp-json/wc/v1/products?")
    suspend fun getProductByCategory(@Query("category") categories: String): Response<ProductResult>

    @GET("wp-json/wc/v3/products?")
    suspend fun getFeaturedProduct(@Query("featured") featured: Boolean): Response<ProductResult>


    @GET("wp-json/wp/v2/home/slider")
    suspend fun getSlider(): Response<SliderResult>





    @GET("wp-json/wc/v3/coupons?")
    suspend fun getCoupon(@Query("code") code: String): Response<CouponResult>

    @POST("wp-json/wc/v3/orders")
    suspend fun createOrder(@Body createOrder: CreateOrder): Response<OrderResponse>

    @GET("wp-json/wc/v1/products?")
    suspend fun getProductBySearch(@Query("search") search: String): Response<ProductResult>


    @GET("ramon/country_state.json")
    suspend fun getCountryState(): Response<CountryState>

    @GET("wp-json/wc/v3/orders?")
    suspend fun getOrders(@Query("customer") customer_id: String): Response<OrdersResult>
}