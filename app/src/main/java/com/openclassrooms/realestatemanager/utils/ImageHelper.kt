package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
class ImageHelper(private val activity: Activity, private val onImageSelected: (Uri) -> Unit) {

    fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        activity.startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    fun handleGalleryResult(data: Intent?) {
        // Check if multiple images have been selected or not
        val imageUris = mutableListOf<Uri>()
        val clipData = data?.clipData
        if (clipData != null) {
            for (i in 0 until clipData.itemCount) {
                val imageUri = clipData.getItemAt(i).uri
                imageUris.add(imageUri)
            }
        } else {
            data?.data?.let { imageUri ->
                imageUris.add(imageUri)
            }
        }


        // Pass each URI to the onImageSelected function
        imageUris.forEach { imageUri ->
            onImageSelected(imageUri)
        }
    }



    companion object {
        const val IMAGE_PICK_CODE = 999
    }
}

