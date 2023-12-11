package com.openclassrooms.realestatemanager.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.activity.DetailActivity
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyFragmentItemBinding
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.data.models.PropertyWithMainPicture
import java.text.NumberFormat
import java.util.Locale


class PropertyAdapter(private val context: Context) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    private var properties: List<PropertyWithMainPicture> = listOf()

    class PropertyViewHolder(val binding: FragmentListRealEstatePropertyFragmentItemBinding) : RecyclerView.ViewHolder(binding.root)


    fun submitList(newProperties: List<PropertyWithMainPicture>) {
        properties = newProperties
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = FragmentListRealEstatePropertyFragmentItemBinding.inflate(layoutInflater, parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.binding.textViewPropertyType.text = property.property.type
        holder.binding.textViewPropertyNeighborhood.text = property.property.neighborhood

        val format = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        val priceText = format.format(property.property.price)
        holder.binding.textViewPropertyPrice.text = priceText

        // Check if there is an image URL available for the property
        if (property.mainPicture?.uri.isNullOrEmpty()) {
            // No image URL is available, so set a default image
            holder.binding.imageViewProperty.setImageResource(R.drawable.picture_intro)
        } else {
            // There is an image URL - use your preferred image loading library to load the image
            // For example, if you are using Glide:
            Glide.with(context)
                .load(property.mainPicture?.uri)
                .into(holder.binding.imageViewProperty)
        }



    }

    override fun getItemCount() = properties.size
}

