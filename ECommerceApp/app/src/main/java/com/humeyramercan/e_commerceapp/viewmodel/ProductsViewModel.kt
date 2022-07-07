package com.humeyramercan.e_commerceapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.repository.ProductsRepository

class ProductsViewModel:ViewModel() {
    private var _products= MutableLiveData<List<ProductModel>>()
    val products: MutableLiveData<List<ProductModel>>
        get()=_products

    private var _loading=MutableLiveData<Boolean>()
    val loading:MutableLiveData<Boolean>
        get()=_loading

    private var productsRepository= ProductsRepository()

    fun getProductsByUserAndCategoryFromAPI(category: String) {
        productsRepository.getProductsByUserAndCategoryFromAPI(category)
        _products=productsRepository.products
        _loading=productsRepository.loading
    }

    fun getSaleProductsByUserFromAPI(){
        productsRepository.getSaleProductsByUserFromAPI()
        _products=productsRepository.saleProducts
        _loading=productsRepository.loading
    }
    override fun onCleared() {
        super.onCleared()
        productsRepository.job?.cancel()
    }
}