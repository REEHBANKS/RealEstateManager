package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

class UpdatePropertyUseCase(private val propertyRepository: PropertyRepository) {

    suspend operator fun invoke(propertyId: String, updatedProperty: PropertyModels): Boolean {
        return propertyRepository.updateProperty(propertyId, updatedProperty)
    }
}
