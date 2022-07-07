package com.humeyramercan.e_commerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humeyramercan.e_commerceapp.databinding.ViewPagerItemBinding
import com.humeyramercan.e_commerceapp.util.downloadFromUrl
import com.humeyramercan.e_commerceapp.util.placeHolderProgressBar

class ViewPagerAdapter(val productImageList:ArrayList<String>): RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(val binding: ViewPagerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(imageUrl:String){
           binding.productDetailImageView.downloadFromUrl(imageUrl, placeHolderProgressBar(binding.productDetailImageView.context))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding= ViewPagerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(productImageList.get(position))
    }

    override fun getItemCount(): Int {
        return productImageList.size
    }
    fun updateData(newCategoriesList:List<String>){
        productImageList.clear()
        productImageList.addAll(newCategoriesList)
        notifyDataSetChanged()
    }
}