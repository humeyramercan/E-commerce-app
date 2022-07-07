package com.humeyramercan.e_commerceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.CRUDResponse
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.repository.ProductsRepository

class BasketViewModel:ViewModel() {
    private val productsRepository=ProductsRepository()

    private var _products= MutableLiveData<List<ProductModel>>()
    val products: MutableLiveData<List<ProductModel>>
        get()=_products

    private var _crudResponse= MutableLiveData<CRUDResponse>()
    val crudResponse: MutableLiveData<CRUDResponse>
        get()=_crudResponse

    private var _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean>
        get() = _loading

    fun getProductsFromBasket(user:String){
        productsRepository.getProductsFromBasket(user)
        _products=productsRepository.products
        _loading=productsRepository.loading
    }
    fun deleteProductFromBasket(id:Int){
        productsRepository.deleteProductFromBasket(id)
        _crudResponse=productsRepository.crudResponse
    }
    fun clearAllProductsInBasket(user:String) {
        productsRepository.clearAllProductsInBasket(user)
        _crudResponse=productsRepository.crudResponse
    }

        override fun onCleared() {
        super.onCleared()
        productsRepository.job?.cancel()
    }

}