package com.humeyramercan.e_commerceapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.repository.ProductsRepository
import com.humeyramercan.e_commerceapp.service.ProductRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel:ViewModel() {
    private val productsRepository=ProductsRepository()

    private var _products= MutableLiveData<List<ProductModel>>()
    val products: MutableLiveData<List<ProductModel>>
        get()=_products

    private var _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean>
        get() = _loading

    fun getAllProductsFromRoomDb(context: Context){
        productsRepository.getAllProductsFromRoomDb(context)
        _products=productsRepository.products
        _loading=productsRepository.loading
    }
    fun deleteProductFromRoomDb(product: ProductModel,context:Context){
        productsRepository.deleteProductFromRoomDb(product,context)
        _products=productsRepository.products
    }
    override fun onCleared() {
        super.onCleared()
        productsRepository.job?.cancel()
    }
}