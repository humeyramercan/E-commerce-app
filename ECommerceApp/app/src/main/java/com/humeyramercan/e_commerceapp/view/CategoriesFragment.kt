package com.humeyramercan.e_commerceapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.adapter.CategoriesAdapter
import com.humeyramercan.e_commerceapp.databinding.FragmentCategoriesBinding
import com.humeyramercan.e_commerceapp.viewmodel.CategoriesViewModel
import com.humeyramercan.e_commerceapp.viewmodel.HomePageViewModel


class CategoriesFragment : Fragment() {

    private lateinit var binding:FragmentCategoriesBinding
    private var categoriesAdapter = CategoriesAdapter(arrayListOf())
    private lateinit var categoriesViewModel: CategoriesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        with(binding){
           categoriesRecyclerView.layoutManager = LinearLayoutManager(context)
            categoriesRecyclerView.adapter = categoriesAdapter
            categoriesRecyclerView.setHasFixedSize(true)

            categoriesViewModel.getCategoriesByUserFromAPI()
            observeLoading(categoriesProgressBar)
            observeCategories()

      }

    }

    private fun observeLoading(progressBar: ProgressBar) {
        categoriesViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }
     private fun observeCategories() {
         categoriesViewModel.categories.observe(viewLifecycleOwner, Observer {
                categoriesAdapter.updateData(it)
        })
    }

}