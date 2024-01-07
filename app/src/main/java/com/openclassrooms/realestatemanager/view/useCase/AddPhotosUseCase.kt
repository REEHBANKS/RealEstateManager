package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription
import com.openclassrooms.realestatemanager.data.repository.PictureRepository

class AddPhotosUseCase(private val pictureRepository: PictureRepository) {

    suspend fun addPhotos(photos: List<PhotoDescription>): Boolean {
        return try {
            // Tenter d'ajouter toutes les photos en utilisant une transaction ou un batch pour garantir l'atomicité
            pictureRepository.addPhotos(photos)
        } catch (e: Exception) {
            // En cas d'erreur, renvoyer false
            false
        }
    }
}