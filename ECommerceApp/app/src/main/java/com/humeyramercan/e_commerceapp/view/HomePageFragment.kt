package com.humeyramercan.e_commerceapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.adapter.CategoriesAdapter
import com.humeyramercan.e_commerceapp.adapter.SaleProductsAdapter
import com.humeyramercan.e_commerceapp.databinding.FragmentHomePageBinding
import com.humeyramercan.e_commerceapp.viewmodel.HomePageViewModel
import com.humeyramercan.e_commerceapp.workmanager.SendNotificationWorker
import java.util.concurrent.TimeUnit

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var homePageViewModel: HomePageViewModel
    private var saleProductAdapter = SaleProductsAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        homePageViewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)

        with(binding) {

            saleRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            saleRecyclerView.adapter = saleProductAdapter
            saleRecyclerView.setHasFixedSize(true)

            homePageViewModel.getSaleProductsByUserFromAPI()
            observeLoading(binding.saleProductsLoading)//Progressbar should be shown until products are displayed.
            observeSaleProducts()

            seeAllTextView.setOnClickListener {
                val action = HomePageFragmentDirections.actionHomePageToProductsFragment("Sale Products")
                findNavController().navigate(action)
            }
           womanImageView.setOnClickListener {
                val action = HomePageFragmentDirections.actionHomePageToProductsFragment("Women's Clothing")
                findNavController().navigate(action)
            }
            manImageView.setOnClickListener {
                val action = HomePageFragmentDirections.actionHomePageToProductsFragment("Men's Clothing")
                findNavController().navigate(action)
            }
        }
    }


    private fun observeLoading(progressBar: ProgressBar) {
        homePageViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun observeSaleProducts() {
        homePageViewModel.saleProducts.observe(viewLifecycleOwner, Observer {
                saleProductAdapter.updateData(it)
        })
    }


}