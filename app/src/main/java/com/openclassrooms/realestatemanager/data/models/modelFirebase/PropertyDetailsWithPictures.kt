package com.openclassrooms.realestatemanager.data.models.modelFirebase

data class PropertyDetailsWithPictures(
    val property: PropertyModels,
    val pictures: List<PhotoDescription>
)
