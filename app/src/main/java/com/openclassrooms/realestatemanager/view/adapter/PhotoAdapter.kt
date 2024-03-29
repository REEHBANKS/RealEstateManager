package com.openclassrooms.realestatemanager.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription
import com.openclassrooms.realestatemanager.databinding.PhotoDetailItemBinding

class PhotoAdapter(private var photos: MutableList<PhotoDescription>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    // Utilisez le ViewBinding pour le ViewHolder
    class PhotoViewHolder(val binding: PhotoDetailItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {

        Log.d("RENEL", "onCreateViewHolder called")

        val binding = PhotoDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        Log.d("RENEL", "onBindViewHolder called for position $position")
        val photoDescription = photos[position]

        Glide.with(holder.binding.imageViewPhoto.context)
            .load(photoDescription.uri)
            .into(holder.binding.imageViewPhoto)
        holder.binding.textViewDescription.text = photoDescription.description
    //   holder.binding.imageViewPhoto.setImageResource(R.drawable.picture_intro)

    }

    override fun getItemCount(): Int {
        return photos.size
    }


    fun updatePhotos(newPhotos: List<PhotoDescription>) {
        newPhotos.forEach { newPhoto ->
            if (!photos.any { existingPhoto -> existingPhoto.uri == newPhoto.uri }) {
                photos.add(newPhoto)
            }
        }
        notifyDataSetChanged()
    }


}
