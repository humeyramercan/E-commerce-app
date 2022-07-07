package com.humeyramercan.e_commerceapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.CRUDResponse
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.repository.ProductsRepository
import com.humeyramercan.e_commerceapp.service.ProductRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel:ViewModel() {
    private val productsRepository=ProductsRepository()

    private var _productById= MutableLiveData<ProductModel>()
    val productById: MutableLiveData<ProductModel>
        get()=_productById

    private var _crudResponse= MutableLiveData<CRUDResponse>()
    val crudResponse: MutableLiveData<CRUDResponse>
        get()=_crudResponse

    fun saveProductToRoomDb(product: ProductModel, context: Context){
        productsRepository.saveProductToRoomDb(product,context)
    }
    fun deleteProductFromRoomDb(product: ProductModel, context: Context){
        productsRepository.deleteProductFromRoomDb(product,context)
    }

    fun getProductByIdFromRoom(id:Int,context:Context){
        productsRepository.getProductByIdFromRoom(id,context)
        _productById=productsRepository.productById
    }

    fun addProductToBasket(user: String,
                           title: String,
                           price: Double,
                           description: String,
                           category: String,
                           image: String,
                           image_two:String,
                           image_three:String,
                           rate: Double,
                           count: Int,
                           sale_state:Int
    ){
        productsRepository.addProductToBasket(user,title,price,description,category,image,image_two,image_three,rate,count,sale_state)
        _crudResponse=productsRepository.crudResponse
    }

    override fun onCleared() {
        super.onCleared()
        productsRepository.job?.cancel()
    }

}