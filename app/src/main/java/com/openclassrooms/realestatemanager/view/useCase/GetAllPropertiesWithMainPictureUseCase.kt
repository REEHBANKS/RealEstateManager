package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.PropertyWithMainPicture
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

class GetAllPropertiesWithMainPictureUseCase(
    private val propertyRepository: PropertyRepository,
    private val pictureRepository: PictureRepository
) {
    suspend operator fun invoke(): List<PropertyWithMainPicture> {
        val properties = propertyRepository.getAllProperties()
        return properties.map { property ->
            val mainPicture = pictureRepository.getMainPictureForProperty(property.id)
            PropertyWithMainPicture(property, mainPicture)
        }
    }
}