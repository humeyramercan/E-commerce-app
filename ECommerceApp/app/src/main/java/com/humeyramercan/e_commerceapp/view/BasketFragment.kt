package com.humeyramercan.e_commerceapp.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.adapter.BasketProductsAdapter
import com.humeyramercan.e_commerceapp.databinding.FragmentBasketBinding
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.util.convertToTwoDigitsAfterDot
import com.humeyramercan.e_commerceapp.util.strikeOutText
import com.humeyramercan.e_commerceapp.viewmodel.BasketViewModel
import java.math.RoundingMode
import java.text.DecimalFormat


class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private lateinit var basketViewModel: BasketViewModel
    private var basketProductsAdapter = BasketProductsAdapter(arrayListOf())
    private var auth = Firebase.auth
    private lateinit var sharedPreferences: SharedPreferences
    private var totalAmount = 0.0
    private var saleAmount = 0.0
    private var count=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = view.context.getSharedPreferences(
            "com.humeyramercan.e_commerceapp",
            Context.MODE_PRIVATE
        )

        basketViewModel = ViewModelProvider(this).get(BasketViewModel::class.java)

        with(binding) {
            basketRecyclerView.layoutManager = LinearLayoutManager(context)
            basketRecyclerView.adapter = basketProductsAdapter
            basketRecyclerView.setHasFixedSize(true)

            auth.currentUser?.let { user ->
                basketViewModel.getProductsFromBasket(user.email.orEmpty())
                loadingObserve(binding.basketProductsLoading)
                observeProductsFromBasket()

                orderButton.setOnClickListener {
                    basketViewModel.clearAllProductsInBasket(user.email.orEmpty())
                    observeClearProductsCrudResponse()
                }

                emptyBasketButton.setOnClickListener {
                    val action = BasketFragmentDirections.actionBasketToHomePage()
                    findNavController().navigate(action)
                }

                basketProductsAdapter.setOnItemClickListener(object :
                    BasketProductsAdapter.onItemClickListener {
                    override fun onItemClickDelete(product: ProductModel) {
                        basketViewModel.deleteProductFromBasket(product.id)
                        observeDeleteProductCrudResponse(user)
                    }

                    override fun onItemClickPlus(product: ProductModel) {
                        count++
                        if(count<=10) {
                            increaseCount(product.sale_state,product.price)
                            totalPriceText.text = convertToTwoDigitsAfterDot(totalAmount) + "$"
                            val convertedSaleAmount= convertToTwoDigitsAfterDot(saleAmount).toDouble()
                            strikeOutText(saleBasketPriceText, convertedSaleAmount)
                        }else{
                            count=10
                        }
                    }

                    override fun onItemClickMinus(product: ProductModel) {
                        count--
                        if(count>=1) {
                            decreaseCount(product.sale_state,product.price)
                            totalPriceText.text = convertToTwoDigitsAfterDot(totalAmount) + "$"
                            val convertedSaleAmount= convertToTwoDigitsAfterDot(saleAmount).toDouble()
                            strikeOutText(saleBasketPriceText, convertedSaleAmount)
                        }else
                            count=1
                    }
                })
            }
        }
    }

    private fun loadingObserve(progressBar: ProgressBar) {
        basketViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun observeProductsFromBasket() {
        with(binding) {
            basketViewModel.products.observe(viewLifecycleOwner, Observer {
                basketProductsAdapter.updateData(it)

                totalAmount = 0.0
                saleAmount = 0.0

                if (it.isNotEmpty()) {
                    emptyBasketViewGone()
                    sharedPreferences.edit().putBoolean("isProductInBag", true).apply() //To send notifications with workManager in the background.
                    for (product in it) {
                        increaseCount(product.sale_state,product.price)
                    }

                } else {
                    context?.let {context-> WorkManager.getInstance(context).cancelAllWork() }
                    sharedPreferences.edit().putBoolean("isProductInBag", false).apply()
                    emptyBasketViewsVisible()
                }
                totalPriceText.text = convertToTwoDigitsAfterDot(totalAmount) +"$"

                if(saleAmount==0.0){
                    saleBasketPriceText.visibility=View.GONE
                    discountAmountText.visibility=View.GONE
                }else{
                    discountAmountText.visibility=View.VISIBLE
                    saleBasketPriceText.visibility=View.VISIBLE
                    val convertedSaleAmount= convertToTwoDigitsAfterDot(saleAmount).toDouble()
                    strikeOutText(saleBasketPriceText, convertedSaleAmount)
                }

            })
        }
    }

    private fun observeClearProductsCrudResponse() {
        basketViewModel.crudResponse.observe(viewLifecycleOwner, Observer {
            if (it.status == 1) {
                val action =
                    BasketFragmentDirections.actionBasketToSuccessOrderFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun observeDeleteProductCrudResponse(user: FirebaseUser) {
        basketViewModel.crudResponse.observe(viewLifecycleOwner, Observer {
            if (it.status == 1) {
                basketViewModel.getProductsFromBasket(user.email.orEmpty())
                observeProductsFromBasket()
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    //Views which are for empty or full basket must be set according to products in basket.
    private fun emptyBasketViewsVisible() {
        with(binding) {
            emptyBasketContraintLayout.visibility = View.VISIBLE
            totalConstraintLayout.visibility = View.GONE
        }
    }

    private fun emptyBasketViewGone() {
        with(binding) {
            emptyBasketContraintLayout.visibility = View.GONE
            totalConstraintLayout.visibility = View.VISIBLE
        }
    }

    //Increase and decrease the amount of product.
    private fun increaseCount(sale_state:Int,productPrice:Double){
        if (sale_state == 1) { //Sale and total amount calculation for discounted products.
            saleAmount += productPrice * 0.25
            totalAmount += productPrice * 0.75
        } else {
            totalAmount += productPrice //Total amount calculation for products without discount.
        }
    }

    private fun decreaseCount(sale_state:Int,productPrice:Double){
        if (sale_state == 1) {
            saleAmount -= productPrice * 0.25
            totalAmount -= productPrice * 0.75
        } else {
            totalAmount -= productPrice
        }
    }




}