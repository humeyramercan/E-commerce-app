package com.humeyramercan.e_commerceapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.humeyramercan.e_commerceapp.databinding.SaleProductItemBinding
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.util.*
import com.humeyramercan.e_commerceapp.view.HomePageFragmentDirections
import java.math.RoundingMode
import java.text.DecimalFormat

class SaleProductsAdapter(val productList:ArrayList<ProductModel>):RecyclerView.Adapter<SaleProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding:SaleProductItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product:ProductModel){
            binding.saleProductNameText.text=product.title
            binding.saleProductImageView.downloadFromUrl(product.image, placeHolderProgressBar(binding.saleProductImageView.context))
            binding.saleCategoryText.text=product.category
            strikeOutText(binding.oldPriceText,product.price)
            val salePrice=product.price-(product.price*0.25)
            binding.salePriceText.text= convertToTwoDigitsAfterDot(salePrice) +"$"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding=SaleProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList.get(position))
        holder.itemView.setOnClickListener {
            val action=HomePageFragmentDirections.actionHomePageToProductDetailFragment(productList.get(position))
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateData(newProductList:List<ProductModel>){
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }


}