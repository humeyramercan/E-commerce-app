package com.humeyramercan.e_commerceapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.adapter.ViewPagerAdapter
import com.humeyramercan.e_commerceapp.databinding.FragmentProductDetailBinding
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.util.*
import com.humeyramercan.e_commerceapp.viewmodel.ProductDetailViewModel


class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var productModel: ProductModel
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private  var viewPagerAdapter=ViewPagerAdapter(arrayListOf())
    private val auth = Firebase.auth
    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = view.context.getSharedPreferences(
            "com.humeyramercan.e_commerceapp",
            Context.MODE_PRIVATE
        )
        productModel = args.productModel
        productDetailViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)

        with(binding) {
            productCategoryText.text = productModel.category
            productDescriptionText.text = productModel.description

            productDetailViewPager.adapter=viewPagerAdapter
            viewPagerAdapter.updateData(arrayListOf(productModel.image,productModel.image_two,productModel.image_three))
            circleIndicator.setViewPager(productDetailViewPager)

            productTitleText.text = productModel.title
            productPriceText.text = "${productModel.price}$"
            ratingBar.rating=productModel.rate.toFloat()

            if (productModel.sale_state == 1) { //If product is discounted, productSaleText must be displayed.
                val salePrice = productModel.price - (productModel.price * 0.25)
                productSaleText.visibility = View.VISIBLE
                strikeOutText(productPriceText, productModel.price)
                productSaleText.text= convertToTwoDigitsAfterDot(salePrice) +"$"
            } else {
                productSaleText.visibility = View.GONE
            }

            println(productModel.id)
            productDetailViewModel.getProductByIdFromRoom(productModel.id, view.context)
            //If product exists in roomdb, favorite button must be red and its isSelected attribute must be true.
            observeProductByIdFromRoom()

            favoriteButton.setOnClickListener {

                if (favoriteButton.isSelected) { //If favorite button is selected, it means product is saved in roomdb. We should delete it.
                    favoriteButton.isSelected = false
                    productDetailViewModel.deleteProductFromRoomDb(productModel, view.context)
                    favoriteButton.backgroundTintList =
                        resources.getColorStateList(R.color.light_red)
                } else { //If favorite button is not selected, it means product is not saved in roomdb. We should save it.
                    favoriteButton.isSelected = true
                    productDetailViewModel.saveProductToRoomDb(productModel, view.context)
                    favoriteButton.backgroundTintList = resources.getColorStateList(R.color.red)
                }
            }

            auth.currentUser?.let { user ->
                addToBasketButton.setOnClickListener {
                    productDetailViewModel.addProductToBasket(
                        user.email.orEmpty(),
                        productModel.title,
                        productModel.price,
                        productModel.description,
                        productModel.category,
                        productModel.image,
                        productModel.image_two,
                        productModel.image_three,
                        productModel.rate,
                        productModel.count,
                        productModel.sale_state
                    )
                    observeAddProductCrudResponse()
                }
            }
        }
    }

    private fun observeProductByIdFromRoom() {
        with(binding) {
            productDetailViewModel.productById.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    favoriteButton.backgroundTintList = resources.getColorStateList(R.color.red)
                    favoriteButton.isSelected = true
                } else {
                    favoriteButton.backgroundTintList =
                        resources.getColorStateList(R.color.light_red)
                    favoriteButton.isSelected = false
                }
            })
        }
    }

    private fun observeAddProductCrudResponse() {
        productDetailViewModel.crudResponse.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            if(it.status==1){
                sharedPreferences.edit().putBoolean("isProductInBag", true).apply()
            }
        })
    }



}