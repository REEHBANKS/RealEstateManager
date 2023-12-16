package com.openclassrooms.realestatemanager.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.PhotoDescription
import com.openclassrooms.realestatemanager.databinding.ActivityDetailBinding
import com.openclassrooms.realestatemanager.databinding.PhotoItemBinding

class PropertyImagesAdapter (private val context: Context) : RecyclerView.Adapter<PropertyImagesAdapter.ImageViewHolder>() {

    private var images: List<PhotoDescription> = listOf()

    class ImageViewHolder (val binding: PhotoItemBinding ): RecyclerView.ViewHolder(binding.root)
    fun submitList(newImages: List<PhotoDescription>) {
        images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = PhotoItemBinding.inflate(layoutInflater,parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]

        holder.binding.photoDescriptionTextView.text = image.description

        Glide.with(context)
            .load(image.uri)
            .into(holder.binding.photoImageView)


    }

    override fun getItemCount() = images.size


}
