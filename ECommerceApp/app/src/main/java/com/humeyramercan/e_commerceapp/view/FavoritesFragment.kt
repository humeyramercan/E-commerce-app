package com.humeyramercan.e_commerceapp.view

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
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.adapter.FavoriteProductsAdapter
import com.humeyramercan.e_commerceapp.databinding.FragmentFavoritesBinding
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.viewmodel.FavoritesViewModel


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesViewModel: FavoritesViewModel
    private var favoriteProductAdapter = FavoriteProductsAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        with(binding) {
            favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
            favoritesRecyclerView.setHasFixedSize(true)
            favoritesRecyclerView.adapter = favoriteProductAdapter

            favoritesViewModel.getAllProductsFromRoomDb(view.context)
            loadingObserve(binding.favoritesLoading)
            observeProductsFromRoom()

            favoriteProductAdapter.setOnItemClickListener(object : FavoriteProductsAdapter.onItemClickListener {
                override fun onItemClick(product: ProductModel) {
                    favoritesViewModel.deleteProductFromRoomDb(product, view.context)
                    observeDeleteProductResult()
                }
            })

            noFavoritesButton.setOnClickListener {
                val action = FavoritesFragmentDirections.actionFavoritesToHomePage()
                findNavController().navigate(action)
            }
        }
    }

    private fun loadingObserve(progressBar: ProgressBar) {
        favoritesViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun observeProductsFromRoom() {
        with(binding) {
            favoritesViewModel.products.observe(viewLifecycleOwner, Observer {

                if (it.isNotEmpty()) { //If favorites list is empty, views which is for empty list must be hidden.
                    favoriteProductAdapter.updateData(it)
                    noFavoritesConstraintLayout.visibility=View.GONE
                } else {   //If favorites list isn't empty, views which is for empty list must be displayed.
                    noFavoritesConstraintLayout.visibility=View.VISIBLE
                }
            })
        }
    }

    private fun observeDeleteProductResult() {
        favoritesViewModel.products.observe(viewLifecycleOwner, Observer {
            favoriteProductAdapter.updateData(it)
        })
    }

}