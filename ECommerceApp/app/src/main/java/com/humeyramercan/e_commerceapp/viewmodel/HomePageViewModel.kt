package com.humeyramercan.e_commerceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.repository.ProductsRepository

class HomePageViewModel:ViewModel() {
    private var _saleProducts = MutableLiveData<List<ProductModel>>()
    val saleProducts: MutableLiveData<List<ProductModel>>
        get() = _saleProducts

    private var _loading=MutableLiveData<Boolean>()
    val loading:MutableLiveData<Boolean>
        get()=_loading


    private var productsRepository=ProductsRepository()

    fun getSaleProductsByUserFromAPI(){
        productsRepository.getSaleProductsByUserFromAPI()
        _saleProducts=productsRepository.saleProducts
        _loading=productsRepository.loading
    }


    override fun onCleared() {
        super.onCleared()
        productsRepository.job?.cancel()
    }
}