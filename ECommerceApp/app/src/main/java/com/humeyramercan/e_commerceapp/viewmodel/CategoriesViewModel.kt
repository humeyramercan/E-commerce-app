package com.humeyramercan.e_commerceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.repository.ProductsRepository

class CategoriesViewModel:ViewModel() {
    private var _categories = MutableLiveData<List<String>>()
    val categories: MutableLiveData<List<String>>
        get() = _categories

    private var _loading=MutableLiveData<Boolean>()
    val loading:MutableLiveData<Boolean>
        get()=_loading

    private var productsRepository= ProductsRepository()

    fun getCategoriesByUserFromAPI() {
        productsRepository.getCategoriesByUserFromAPI()
        _loading=productsRepository.loading
        _categories=productsRepository.categories
    }
}