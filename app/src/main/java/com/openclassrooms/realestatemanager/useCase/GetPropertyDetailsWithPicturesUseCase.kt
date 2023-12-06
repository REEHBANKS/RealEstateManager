package com.openclassrooms.realestatemanager.useCase

import com.openclassrooms.realestatemanager.data.models.PropertyDetailsWithPictures
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

class GetPropertyDetailsWithPictures(
    private val propertyRepository: PropertyRepository,
    private val pictureRepository: PictureRepository
) {
    suspend operator fun invoke(propertyId: String): PropertyDetailsWithPictures? {
        val property = propertyRepository.getPropertyById(propertyId)
        return property?.let {
            val pictures = pictureRepository.getPicturesForProperty(propertyId)
            PropertyDetailsWithPictures(property, pictures)
        }
    }
}

