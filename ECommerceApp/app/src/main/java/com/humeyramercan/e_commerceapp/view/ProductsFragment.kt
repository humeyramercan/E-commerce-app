package com.humeyramercan.e_commerceapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.humeyramercan.e_commerceapp.adapter.ProductsAdapter
import com.humeyramercan.e_commerceapp.databinding.FragmentProductsBinding
import com.humeyramercan.e_commerceapp.viewmodel.ProductsViewModel


class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private lateinit var productsViewModel: ProductsViewModel
    private var allProductAdapter= ProductsAdapter(arrayListOf())
    private var categoryName=""
    private val args:ProductsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProductsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryName=args.categoryName
        productsViewModel=ViewModelProvider(this).get(ProductsViewModel::class.java)

       with(binding){
           allProductsRecyclerView.layoutManager=GridLayoutManager(view.context,2)
           allProductsRecyclerView.adapter=allProductAdapter
           allProductsRecyclerView.setHasFixedSize(true)

           if(categoryName=="Sale Products"){ //If user clicks "see all" text which is for discounted products, these products must be displayed.
                 productsViewModel.getSaleProductsByUserFromAPI()
                 loadingObserve(binding.productsLoading)
                 observeProducts()
            }else { //Otherwise, the products must be displayed by categories.
                productsViewModel.getProductsByUserAndCategoryFromAPI(categoryName)
                loadingObserve(binding.productsLoading)
                observeProducts()
            }
       }
    }

    private fun loadingObserve(progressBar:ProgressBar){
        productsViewModel.loading.observe(viewLifecycleOwner, Observer {
            if(it){
                progressBar.visibility=View.VISIBLE
            }else{
                progressBar.visibility=View.GONE
            }
        })
    }

    private fun observeProducts(){
        productsViewModel.products.observe(viewLifecycleOwner, Observer {
                allProductAdapter.updateData(it)
        })
    }

}