package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

class AddPropertyUseCase(private val propertyRepository: PropertyRepository) {

    suspend fun addPropertyUseCase(property: PropertyModels): String? {
        return try {
            // Appel au repository pour ajouter la propriété
            val newPropertyId = propertyRepository.addProperty(property)

            newPropertyId
        } catch (e: Exception) {

            null
        }
    }
}
