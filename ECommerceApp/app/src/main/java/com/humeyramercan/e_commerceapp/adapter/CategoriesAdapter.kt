package com.humeyramercan.e_commerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.databinding.CategoriesItemBinding
import com.humeyramercan.e_commerceapp.view.CategoriesFragmentDirections


class CategoriesAdapter(val categoriesList:ArrayList<String>):RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding:CategoriesItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(categoryName:String){
            binding.categoriesNameText.text=categoryName

            when (categoryName) {
                "Women's Clothing" ->binding.backgroundContrintLayout.setBackgroundResource(R.drawable.womens_clothing)
                "Men's Clothing" -> binding.backgroundContrintLayout.setBackgroundResource(R.drawable.mens_clothing)
                "Girls' Clothing"-> binding.backgroundContrintLayout.setBackgroundResource(R.drawable.girls_clothing)
                "Boys' Clothing"-> binding.backgroundContrintLayout.setBackgroundResource(R.drawable.boys_clothing)
                "Accessory"-> binding.backgroundContrintLayout.setBackgroundResource(R.drawable.accessories)
                "Bag"-> binding.backgroundContrintLayout.setBackgroundResource(R.drawable.bags)
                "Shoes"-> binding.backgroundContrintLayout.setBackgroundResource(R.drawable.shoes)
                "Baby's Clothing"-> binding.backgroundContrintLayout.setBackgroundResource(R.drawable.babies_clothing)
                else -> {
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding=CategoriesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoriesList.get(position))
        holder.itemView.setOnClickListener {
            val action=CategoriesFragmentDirections.actionCategoriesToProductsFragment(categoriesList.get(position))
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }
    fun updateData(newCategoriesList:List<String>){
        categoriesList.clear()
        categoriesList.addAll(newCategoriesList)
        notifyDataSetChanged()
    }
}