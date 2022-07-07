package com.humeyramercan.e_commerceapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.humeyramercan.e_commerceapp.model.CRUDResponse
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.service.ProductAPIService
import com.humeyramercan.e_commerceapp.service.ProductRoomDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class ProductsRepository {
    //_saleProducts and _categories live data are defined separately because they are using on the same page
    //for get sale products from api
    private var _saleProducts = MutableLiveData<List<ProductModel>>()
    val saleProducts: MutableLiveData<List<ProductModel>>
        get() = _saleProducts

    //for categories from api
    private var _categories = MutableLiveData<List<String>>()
    val categories: MutableLiveData<List<String>>
        get() = _categories

    //for other product lists which is displayed on different pages
    private var _products = MutableLiveData<List<ProductModel>>()
    val products: MutableLiveData<List<ProductModel>>
        get() = _products


    //for a product by id from roomdb
    private var _productById = MutableLiveData<ProductModel>()
    val productById: MutableLiveData<ProductModel>
        get() = _productById

    //for add to basket and delete from basket response from api
    private var _crudResponse = MutableLiveData<CRUDResponse>()
    val crudResponse: MutableLiveData<CRUDResponse>
        get() = _crudResponse

    //for loading process for categories
    private var _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean>
        get() = _loading

    var job: Job? = null
    private val exeptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.w("COROUTINE", "Failure", throwable)
    }

    private val productAPIService = ProductAPIService()

    fun saveProductToRoomDb(product: ProductModel, context: Context) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val dao = ProductRoomDatabase(context).productDao()
            dao.insertProduct(product)
        }

    }

    fun getProductByIdFromRoom(id: Int, context: Context) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val dao = ProductRoomDatabase(context).productDao()
            val product_by_id = dao.getProductById(id)
            withContext(Dispatchers.Main + exeptionHandler) {
                if (product_by_id != null) {
                    _productById.value = product_by_id
                }
            }
        }
    }

    fun deleteProductFromRoomDb(product: ProductModel, context: Context) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val dao = ProductRoomDatabase(context).productDao()
            dao.deleteProduct(product)
            val productsFromRoom = dao.getAllProducts()
            withContext(Dispatchers.Main + exeptionHandler) {
                if (productsFromRoom != null) {
                    _products.value = productsFromRoom
                }
            }
        }
    }

    fun getAllProductsFromRoomDb(context: Context) {
        _loading.value=true
        job = CoroutineScope(Dispatchers.IO).launch {
            val dao = ProductRoomDatabase(context).productDao()
            val allProductsFromRoom = dao.getAllProducts()
            withContext(Dispatchers.Main + exeptionHandler) {
                if (allProductsFromRoom != null) {
                    _loading.value=false
                    _products.value = allProductsFromRoom
                }
            }
        }

    }

    fun addProductToBasket(
        user: String,
        title: String,
        price: Double,
        description: String,
        category: String,
        image: String,
        image_two:String,
        image_three:String,
        rate: Double,
        count: Int,
        sale_state: Int
    ) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val crud_respone = productAPIService.addToBasket(
                user,
                title,
                price,
                description,
                category,
                image,
                image_two,
                image_three,
                rate,
                count,
                sale_state
            )
            withContext(Dispatchers.Main + exeptionHandler) {
                _crudResponse.value = crud_respone
            }
        }
    }

    fun getProductsFromBasket(user: String) {
        _loading.value=true
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = productAPIService.getBagProductsByUser(user)
            withContext(Dispatchers.Main + exeptionHandler) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _products.value = it
                        _loading.value=false
                    }
                }
            }
        }
    }

    fun deleteProductFromBasket(id: Int) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val crud_response = productAPIService.deleteFromBag(id)
            withContext(Dispatchers.Main + exeptionHandler) {
                _crudResponse.value = crud_response
            }
        }
    }

    fun clearAllProductsInBasket(user: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val crud_response = productAPIService.clearBag(user)
            withContext(Dispatchers.Main + exeptionHandler) {
                _crudResponse.value = crud_response
            }
        }
    }


    fun getCategoriesByUserFromAPI() {
        _loading.value = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = productAPIService.getCategoriesByUser("humeyramercanx@gmail.com")
            withContext(Dispatchers.Main + exeptionHandler) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _categories.value = it
                        _loading.value = false
                    }
                }

            }
        }
    }

    fun getSaleProductsByUserFromAPI(){
        _loading.value=true
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = productAPIService.getSaleProductsByUser("humeyramercanx@gmail.com")
            withContext(Dispatchers.Main + exeptionHandler) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _saleProducts.value = it
                        _loading.value=false
                    }
                }
            }
        }
    }

    fun getProductsByUserAndCategoryFromAPI(category: String) {
        _loading.value = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = productAPIService.getProductsByUserAndCategory("humeyramercanx@gmail.com",category)
            withContext(Dispatchers.Main + exeptionHandler) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _products.value = it
                        _loading.value = false
                    }
                }
            }
        }
    }

}