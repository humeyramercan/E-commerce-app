package com.humeyramercan.e_commerceapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductRoomModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int=0,

    @ColumnInfo(name = "user")
    val user:String,

    @ColumnInfo(name = "title")
    val title:String,

    @ColumnInfo(name = "price")
    val price:Double,

    @ColumnInfo(name = "description")
    val description:String,

    @ColumnInfo(name = "category")
    val category:String,

    @ColumnInfo(name = "image")
    val image:String,

    @ColumnInfo(name = "rate")
    val rate:Double,

    @ColumnInfo(name = "count")
    val count:Int,
)/*{
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}*/
