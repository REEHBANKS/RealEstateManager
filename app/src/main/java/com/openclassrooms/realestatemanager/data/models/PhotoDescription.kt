package com.openclassrooms.realestatemanager.data.models

data class PhotoDescription(

    var id: String, // ID unique de la photo
    var propertyId: String, // ID de la propriété à laquelle la photo appartient
    var uri: String, // URL de l'image
    var description: String, // Description de la photo
    var isMain: Boolean = false // Indicateur si c'est la photo principale
)

