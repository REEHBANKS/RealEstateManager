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
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.activity.DetailActivity
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyFragmentItemBinding
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.data.models.PropertyWithMainPicture


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
        holder.binding.textViewPropertyName.text = property.property.type
    }

    override fun getItemCount() = properties.size
}


