package com.openclassrooms.realestatemanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.openclassrooms.realestatemanager.data.models.PhotoDescription
import kotlinx.coroutines.tasks.await

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


}
