package com.humeyramercan.e_commerceapp.service

import com.humeyramercan.e_commerceapp.model.CRUDResponse
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.model.UserModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductAPI {
    //BASE -> https://canerture.com/api/
    //EXT -> ecommerce/get_sale_products.php

    @POST("api/ecommerce/add_to_bag.php")
    @FormUrlEncoded
    suspend fun addToBag(
        @Field("user") user:String,
        @Field("title") title:String,
        @Field("price") price:Double,
        @Field("description") description:String,
        @Field("category") category:String,
        @Field("image") image:String,
        @Field("image_two") image_two:String,
        @Field("image_three") image_three:String,
        @Field("rate") rate:Double,
        @Field("count") count:Int,
        @Field("sale_state") sale_state:Int,
    ):CRUDResponse

    @POST("api/ecommerce/get_bag_products_by_user.php")
    @FormUrlEncoded
    suspend fun getBagProductsByUser(
        @Field("user") user:String
    ):Response<List<ProductModel>>

    @POST("api/ecommerce/delete_from_bag.php")
    @FormUrlEncoded
    suspend fun deleteFromBag(
        @Field("id") id:Int
    ):CRUDResponse

    @POST("api/ecommerce/clear_bag.php")
    @FormUrlEncoded
    suspend fun clearBag(
        @Field("user") user:String
    ):CRUDResponse

    @POST("api/ecommerce/get_sale_products_by_user.php")
    @FormUrlEncoded
    suspend fun getSaleProductsByUser(
        @Field("user") user:String
    ):Response<List<ProductModel>>

    @POST("api/ecommerce/get_categories_by_user.php")
    @FormUrlEncoded
    suspend fun getCategoriesByUser(
        @Field("user") user:String
    ):Response<List<String>>

    @POST("api/ecommerce/get_products_by_user_and_category.php")
    @FormUrlEncoded
    suspend fun getProductsByUserAndCategory(
        @Field("user") user:String,
        @Field("category") category: String
    ):Response<List<ProductModel>>

}