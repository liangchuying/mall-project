package com.example.mallandroid.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
) {
    fun isSuccess(): Boolean = code == 200
}

data class Category(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("parentId")
    val parentId: Long?,
    @SerializedName("sort")
    val sort: Int?,
    @SerializedName("children")
    val children: List<Category>? = null
)

data class Product(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("originalPrice")
    val originalPrice: Double?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("brandId")
    val brandId: Long?,
    @SerializedName("brandName")
    val brandName: String?,
    @SerializedName("categoryId")
    val categoryId: Long?,
    @SerializedName("status")
    val status: Int,
    @SerializedName("sales")
    val sales: Int,
    @SerializedName("sort")
    val sort: Int?
)

data class CartItem(
    @SerializedName("id")
    val id: Long,
    @SerializedName("skuId")
    val skuId: Long,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productImage")
    val productImage: String?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("selected")
    var selected: Boolean
)

data class Order(
    @SerializedName("orderNo")
    val orderNo: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("totalAmount")
    val totalAmount: Double,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("items")
    val items: List<OrderItem>?
)

data class OrderItem(
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productImage")
    val productImage: String?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    val quantity: Int
)

data class LoginRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String
)

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("phone")
    val phone: String?
)