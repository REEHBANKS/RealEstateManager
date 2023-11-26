package com.openclassrooms.realestatemanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.activity.DetailActivity
import com.openclassrooms.realestatemanager.activity.MainActivity
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyFragmentItemBinding
import com.openclassrooms.realestatemanager.models.PropertyModels



class PropertyAdapter(private val properties: List<PropertyModels>, private val context: Context) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    class PropertyViewHolder(val binding: FragmentListRealEstatePropertyFragmentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentListRealEstatePropertyFragmentItemBinding.inflate(layoutInflater, parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.binding.textViewPropertyName.text = properties[position].name
        holder.itemView.setOnClickListener {
            // Vérifiez si vous êtes en mode tablette en cherchant le conteneur de détail dans la mise en page actuelle.
            val detailContainer = (context as Activity).findViewById<View>(R.id.detail_container)
            if (detailContainer != null) {
                // Mode tablette: chargez le DetailFragment dans le conteneur.
                (context as MainActivity).showDetailFragment(properties[position].name)
            } else {
                // Mode téléphone: démarrez une nouvelle DetailActivity.
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("EXTRA_DETAIL", properties[position].name)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = properties.size
}

