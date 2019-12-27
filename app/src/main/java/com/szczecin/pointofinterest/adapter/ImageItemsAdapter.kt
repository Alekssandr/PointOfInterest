package com.szczecin.pointofinterest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.szczecin.pointofinterest.R
import com.szczecin.pointofinterest.databinding.ImageItemViewBinding

class ImageItemsAdapter : RecyclerView.Adapter<ImageItemsAdapter.ItemViewHolder>() {

    private var imagesList: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ImageItemViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.image_item_view, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(imagesList[position])


    fun update(items: List<String>) {
        this.imagesList = items
        notifyDataSetChanged()
    }

    class ItemViewHolder(private val binding: ImageItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            this.binding.image = imageUrl
            binding.executePendingBindings()
        }
    }
}
