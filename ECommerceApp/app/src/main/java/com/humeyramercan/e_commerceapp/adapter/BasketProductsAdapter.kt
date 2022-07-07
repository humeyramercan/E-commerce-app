package com.humeyramercan.e_commerceapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.humeyramercan.e_commerceapp.databinding.FavroitesItemBinding
import com.humeyramercan.e_commerceapp.model.ProductModel
import com.humeyramercan.e_commerceapp.util.convertToTwoDigitsAfterDot
import com.humeyramercan.e_commerceapp.util.downloadFromUrl
import com.humeyramercan.e_commerceapp.util.placeHolderProgressBar
import com.humeyramercan.e_commerceapp.util.strikeOutText
import com.humeyramercan.e_commerceapp.view.BasketFragmentDirections


class BasketProductsAdapter(val productList:ArrayList<ProductModel>):RecyclerView.Adapter<BasketProductsAdapter.ProductViewHolder>() {

   private lateinit var mListener:onItemClickListener

    interface onItemClickListener{
        fun onItemClickDelete(product:ProductModel)
        fun onItemClickPlus(product:ProductModel)
        fun onItemClickMinus(product:ProductModel)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener=listener
    }
    class ProductViewHolder(val binding:FavroitesItemBinding):RecyclerView.ViewHolder(binding.root){
        private var count=1
        fun bind(product:ProductModel,listener:onItemClickListener){
            binding.favoriteImageView.downloadFromUrl(product.image, placeHolderProgressBar(binding.favoriteImageView.context))
            binding.favoritePriceText.text=product.price.toString()+"$"
            binding.favoriteTitleText.text=product.title
            binding.favoriteCategoryText.text=product.category
            binding.favoritesRatingBar.rating=product.rate.toFloat()
            binding.favoriteDeleteButton.setOnClickListener {
                listener.onItemClickDelete(product)
            }
            binding.plusImageButton.setOnClickListener {
                count++
                if(count>10){
                    Toast.makeText(binding.favoriteImageView.context, "You can't order more than 10 products.", Toast.LENGTH_SHORT).show()
                    count=10

                }
                binding.countTextView.text = count.toString()
                listener.onItemClickPlus(product)

            }
            binding.minusImageButton.setOnClickListener {
                count--
                if(count<1) count=1
                binding.countTextView.text=count.toString()
                listener.onItemClickMinus(product)
            }
            if(product.sale_state==1){
                strikeOutText(binding.favoritePriceText,product.price)
                val salePrice=product.price-(product.price*0.25)
                binding.favoriteSalePriceText.visibility=View.VISIBLE
                binding.favoriteSalePriceText.text= convertToTwoDigitsAfterDot(salePrice) +"$"
            }else{
                binding.favoriteSalePriceText.visibility=View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding=FavroitesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList.get(position),mListener)
        holder.itemView.setOnClickListener {
           val action=BasketFragmentDirections.actionBasketToProductDetailFragment(productList.get(position))
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