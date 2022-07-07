package com.humeyramercan.e_commerceapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.humeyramercan.e_commerceapp.databinding.ProductItemBinding
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.util.*
import com.humeyramercan.e_commerceapp.view.ProductsFragmentDirections
import java.math.RoundingMode
import java.text.DecimalFormat

class ProductsAdapter(val productList:ArrayList<ProductModel>): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: ProductModel){
            binding.productNameText.text=product.title
            binding.priceText.text=product.price.toString()+"$"
            binding.productCategoryText.text=product.category
            binding.productImageView.downloadFromUrl(product.image, placeHolderProgressBar(binding.productImageView.context))

            if(product.sale_state==1){
                strikeOutText(binding.priceText,product.price)
                val salePrice=product.price-(product.price*0.25)
                binding.allSalePriceText.visibility= View.VISIBLE
                binding.allSalePriceText.text= convertToTwoDigitsAfterDot(salePrice) +"$"
            }else{
                binding.allSalePriceText.visibility= View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding= ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList.get(position))
        holder.itemView.setOnClickListener {
            val action= ProductsFragmentDirections.actionAllProductsFragmentToProductDetailFragment(productList.get(position))
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