package com.humeyramercan.e_commerceapp.service

import com.humeyramercan.e_commerceapp.model.CRUDResponse
import com.humeyramercan.e_commerceapp.model.ProductModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field

class ProductAPIService {

    private val BASE_URL = "https://canerture.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL) // base url
        .addConverterFactory(GsonConverterFactory.create()) //we will use json data and convert them
        .build()
        .create(ProductAPI::class.java) //retrofit binding with api


    suspend fun addToBasket(user: String, title: String, price: Double, description: String, category: String, image: String, image_two: String, image_three: String, rate: Double, count: Int, sale_state: Int): CRUDResponse {
        return api.addToBag(user,title,price,description,category,image,image_two,image_three,rate,count,sale_state)
    }

    suspend fun getBagProductsByUser(user:String):Response<List<ProductModel>>{
        return api.getBagProductsByUser(user)
    }
    suspend fun deleteFromBag(id:Int):CRUDResponse{
        return api.deleteFromBag(id)
    }

    suspend fun clearBag(user: String):CRUDResponse{
        return api.clearBag(user)
    }
    suspend fun getSaleProductsByUser(user:String):Response<List<ProductModel>>{
        return api.getSaleProductsByUser(user)
    }
    suspend fun getCategoriesByUser(user:String):Response<List<String>>{
        return api.getCategoriesByUser(user)
    }

    suspend fun getProductsByUserAndCategory(user:String, category: String):Response<List<ProductModel>>{
        return api.getProductsByUserAndCategory(user,category)
    }

}