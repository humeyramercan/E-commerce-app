package com.humeyramercan.e_commerceapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.humeyramercan.e_commerceapp.model.ProductModel

@Database(entities = [ProductModel::class], version = 1)
abstract class ProductRoomDatabase:RoomDatabase() {
    abstract fun productDao():ProductDAO

    companion object{
        @Volatile
        private var instance:ProductRoomDatabase?=null
        private val lock=Any()

        operator fun invoke(context:Context)= instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also { instance=it }
        }

        private fun makeDatabase(context:Context)= Room.databaseBuilder(
            context.applicationContext,ProductRoomDatabase::class.java,"productdatabase"
        ).build()

    }
}