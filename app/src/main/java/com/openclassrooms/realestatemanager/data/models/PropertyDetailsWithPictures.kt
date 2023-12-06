package com.openclassrooms.realestatemanager.data.models

data class PropertyDetailsWithPictures(
    val property: PropertyModels,
    val pictures: List<PhotoDescription>
)
