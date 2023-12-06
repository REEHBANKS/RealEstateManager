package com.openclassrooms.realestatemanager.useCase

import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

class AddPropertyUseCase(private val propertyRepository: PropertyRepository) {

    suspend operator fun invoke(property: PropertyModels): Boolean {
        return propertyRepository.addProperty(property)
    }
}
