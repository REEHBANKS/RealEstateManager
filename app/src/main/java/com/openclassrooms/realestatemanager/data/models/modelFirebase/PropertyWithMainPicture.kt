package com.openclassrooms.realestatemanager.data.models.modelFirebase

import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels

data class PropertyWithMainPicture(

    val property: PropertyModels,
    val mainPicture: PhotoDescription?
)
