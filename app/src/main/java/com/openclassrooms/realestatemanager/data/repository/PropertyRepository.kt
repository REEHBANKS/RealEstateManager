package com.openclassrooms.realestatemanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import kotlinx.coroutines.tasks.await

@Suppress("DEPRECATION")
class PropertyRepository (private val firestore: FirebaseFirestore) {

    suspend fun addProperty(property: PropertyModels): Boolean {
        return try {
            // Générer un nouvel ID de document
            val newPropertyRef = firestore.collection("properties").document()
            val newPropertyId = newPropertyRef.id

            // Définir l'ID de la propriété avec l'ID généré par Firebase
            val newProperty = property.copy(id = newPropertyId)

            // Sauvegarder la nouvelle propriété avec l'ID dans Firestore
            newPropertyRef.set(newProperty).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    suspend fun getAllProperties(): List<PropertyModels> {
        val querySnapshot = firestore.collection("properties").get().await()
        return querySnapshot.documents.mapNotNull { it.toObject<PropertyModels>() }
    }


    suspend fun getPropertyById(id: String): PropertyModels? {
        val documentSnapshot = firestore.collection("properties").document(id).get().await()
        return documentSnapshot.toObject<PropertyModels>()
    }


    suspend fun updateProperty(propertyId: String, updatedProperty: PropertyModels): Boolean {
        return try {
            firestore.collection("properties").document(propertyId).set(updatedProperty).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}
