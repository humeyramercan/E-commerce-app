package com.humeyramercan.e_commerceapp.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.humeyramercan.e_commerceapp.model.ProductModel

@Dao
interface ProductDAO {
    @Insert
    suspend fun insertProduct(product:ProductModel)

    @Delete
    suspend fun deleteProduct(product: ProductModel)

    @Query("DELETE FROM ProductModel")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM ProductModel")
    suspend fun getAllProducts():List<ProductModel>

    @Query("SELECT * FROM ProductModel WHERE id=:id")
    suspend fun getProductById(id:Int):ProductModel
}