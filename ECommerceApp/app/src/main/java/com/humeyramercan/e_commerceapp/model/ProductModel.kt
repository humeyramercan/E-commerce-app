package com.humeyramercan.e_commerceapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ProductModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id:Int,
    @ColumnInfo(name = "user")
    val user:String,//@SerializedName("user")
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
    @ColumnInfo(name = "sale_state")
    val sale_state:Int,
    @ColumnInfo(name = "image_two")
    val image_two:String,
    @ColumnInfo(name = "image_three")
    val image_three:String,
):Parcelable

