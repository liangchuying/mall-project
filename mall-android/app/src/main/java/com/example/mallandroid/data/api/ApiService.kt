package com.example.mallandroid.data.api

import com.example.mallandroid.data.model.*
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<Void>>

    @GET("api/category/tree")
    suspend fun getCategoryTree(): Response<ApiResponse<List<Category>>>

    @GET("api/product/search")
    suspend fun searchProducts(
        @Query("keyword") keyword: String? = null,
        @Query("categoryId") categoryId: Long? = null,
        @Query("current") current: Int = 1,
        @Query("size") size: Int = 10
    ): Response<ApiResponse<ProductSearchResponse>>

    @GET("api/product/search/suggestions")
    suspend fun getSearchSuggestions(
        @Query("keyword") keyword: String? = null
    ): Response<ApiResponse<List<String>>>

    @GET("api/cart")
    suspend fun getCartList(): Response<ApiResponse<List<CartItem>>>

    @POST("api/cart")
    suspend fun addToCart(
        @Body request: CartAddRequest
    ): Response<ApiResponse<Long>>

    @PUT("api/cart")
    suspend fun updateCart(
        @Body request: CartUpdateRequest
    ): Response<ApiResponse<Void>>

    @DELETE("api/cart/{cartId}")
    suspend fun deleteCart(@Path("cartId") cartId: Long): Response<ApiResponse<Void>>

    @GET("api/order/my")
    suspend fun getMyOrders(
        @Query("status") status: Int? = null,
        @Query("current") current: Int = 1,
        @Query("size") size: Int = 10
    ): Response<ApiResponse<OrderListResponse>>

    @GET("api/order/{orderNo}")
    suspend fun getOrderDetail(@Path("orderNo") orderNo: String): Response<ApiResponse<Order>>
}

data class ProductSearchResponse(
    @SerializedName("records")
    val records: List<Product>,
    @SerializedName("total")
    val total: Long,
    @SerializedName("current")
    val current: Long,
    @SerializedName("size")
    val size: Long
)

data class CartAddRequest(
    @SerializedName("skuId")
    val skuId: Long,
    @SerializedName("quantity")
    val quantity: Int
)

data class CartUpdateRequest(
    @SerializedName("id")
    val id: Long,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("selected")
    val selected: Boolean?
)

data class OrderListResponse(
    @SerializedName("records")
    val records: List<Order>,
    @SerializedName("total")
    val total: Long
)