package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription
import com.openclassrooms.realestatemanager.data.repository.PictureRepository

class GetPicturesForPropertyUseCase(private val pictureRepository: PictureRepository) {
    suspend fun execute(propertyId: String): List<PhotoDescription> {
        return pictureRepository.getPicturesForProperty(propertyId)
    }
}
