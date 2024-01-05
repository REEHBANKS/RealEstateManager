package com.openclassrooms.realestatemanager.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.openclassrooms.realestatemanager.data.models.PhotoDescription
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import java.util.UUID

class PictureRepository(private val firestore: FirebaseFirestore) {

    suspend fun getMainPictureForProperty(propertyId: String): PhotoDescription? {
        val querySnapshot = firestore.collection("pictures")
            .whereEqualTo("propertyId", propertyId)
            .whereEqualTo("isMain", true)
            .limit(1)
            .get().await()

        return querySnapshot.documents.firstOrNull()?.toObject<PhotoDescription>()
    }


    suspend fun getPicturesForProperty(propertyId: String): List<PhotoDescription> {
        val querySnapshot = firestore.collection("pictures")
            .whereEqualTo("propertyId", propertyId)
            .get().await()

        return querySnapshot.documents.mapNotNull { it.toObject<PhotoDescription>() }
    }

    suspend fun addPhotos(photos: List<PhotoDescription>): Boolean {
        // Créer un batch pour effectuer les opérations d'écriture
        val batch = firestore.batch()

        // Pour chaque photo, créer une référence de document dans la collection "pictures"
        // et l'ajouter au batch
        photos.forEach { photo ->
            val photoRef = firestore.collection("pictures").document() // Créer une référence avec un ID unique
            val photoMap = mapOf(
                "description" to photo.description,
                "id" to photo.id,
                "propertyId" to photo.propertyId,
                "uri" to photo.uri,
                "isMain" to photo.isMain // Utilisez 'isMain' ici au lieu de 'main'
            )
            batch.set(photoRef, photoMap) // Utilisez la carte pour définir la photo
        }

        // Exécuter le batch
        batch.commit().await()
        return true // Retourner true si le batch est exécuté sans erreurs
    }



    suspend fun uploadImageAndGetDownloadUrl(inputStream: InputStream): Uri? {
        val storageReference =
            FirebaseStorage.getInstance().getReference("uploads/${UUID.randomUUID()}.jpg")

        return try {
            val uploadTaskSnapshot = storageReference.putStream(inputStream).await()
            uploadTaskSnapshot.metadata?.reference?.downloadUrl?.await()
        } catch (e: Exception) {
            null
        }
    }


}
