package com.prologic.laughinggoat.network

import com.blog.prologic.model.country_state.CountryState
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
import com.prologic.laughinggoat.utils.UrlValue.Companion.squareup_token
import com.prologic.laughinggoat.utils.UrlValue.Companion.squareup_version
import retrofit2.Response
import retrofit2.http.*

interface RetrofitUrl {

    @POST("wp-json/wc/v1/customers?")
    suspend fun createCustomer(@Body registerRequest: RegisterRequest): Response<UserResult>

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

    @GET("wp-json/wc/v3/customers/{id}")
    suspend fun getCustomerById(@Path("id") id: String): Response<UserResult>

    @GET("wp-json/wc/v3/products/categories?")
    suspend fun getCategory(): Response<CategoryResult>

    @GET("wp-json/wc/v1/products?")
    suspend fun getProductByCategory(@Query("category") categories: String): Response<ProductResult>

    @GET("wp-json/wc/v1/products?")
    suspend fun getFeaturedProduct(@Query("featured") featured: Boolean): Response<ProductResult>

    @GET("wp-json/v1/get-blog-posts")
    suspend fun getBlog(): Response<BlogResult>


    @GET("wp-json/v1/get-events-vendors")
    suspend fun getEventVendor(): Response<EventVendorResult>

    @GET("wp-content/themes/taqwa/slider.json")
    suspend fun getSlider(): Response<SliderResult>

    @GET("wp-json/v1/get-gallery-images")
    suspend fun getFloralGallery(): Response<FloralGalleryResult>

    @GET("wp-json/wc/v3/taxes/?")
    suspend fun getTaxes(): Response<TaxesResult>

    @GET("wp-json/wc/v3/shipping/zones/6/methods/13")
    suspend fun getShippingMethod(): Response<ShippingResult>

    @GET("wp-json/wc/v3/coupons?")
    suspend fun getCoupon(@Query("code") code: String): Response<CouponResult>

    @POST("wp-json/wc/v3/orders")
    suspend fun createOrder(@Body createOrder: CreateOrder): Response<OrderResponse>


    @Headers(
        squareup_version,
        squareup_token,
        "Content-Type: application/json"
    )
    @POST("payments")
    suspend fun updateSquarePayment(@Body squareRequest: SquareRequest): Response<SquareResult>

    @GET("ramon/country_state.json")
    suspend fun getCountryState(): Response<CountryState>

    @GET("wp-json/wc/v3/orders?")
    suspend fun getOrders(@Query("customer") customer_id: String): Response<OrdersResult>
}