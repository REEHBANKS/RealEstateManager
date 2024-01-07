package com.openclassrooms.realestatemanager.data.repository

import android.util.Log
import androidx.lifecycle.asFlow
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.openclassrooms.realestatemanager.data.PropertyDao
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.data.models.modelRoom.toEntity
import com.openclassrooms.realestatemanager.data.models.modelRoom.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class PropertyRepository(
    private val firestore: FirebaseFirestore,
    private val propertyDao: PropertyDao
) {


    suspend fun addProperty(property: PropertyModels): String? {
        var newPropertyId: String? = null

        try {
            // Ajout à Firebase
            val newPropertyRef = firestore.collection("properties").document()
            newPropertyId = newPropertyRef.id
            val newProperty = property.copy(id = newPropertyId)
            newPropertyRef.set(newProperty).await()

            // Conversion et stockage dans Room
            val propertyEntity = newProperty.toEntity()
            propertyDao.addProperty(propertyEntity)
        } catch (e: Exception) {
            // Gérer l'exception si nécessaire
        }

        return newPropertyId
    }

    suspend fun updateProperty(propertyId: String, updatedProperty: PropertyModels): Boolean {
        return try {
            // Mise à jour dans Firebase
            val propertyToUpdate = updatedProperty.copy(id = propertyId)
            firestore.collection("properties").document(propertyId).set(propertyToUpdate).await()

            // Mise à jour dans Room
            val propertyEntity = propertyToUpdate.toEntity()
            propertyDao.updateProperty(propertyEntity)

            true // Retourner true si la mise à jour est réussie
        } catch (e: Exception) {
            false // Retourner false en cas d'échec
        }
    }


    suspend fun getAllProperties(): List<PropertyModels> {
        Log.d("PropertyRepository", "Fetching properties...")

        // Vérifier d'abord dans Room
        val localProperties = withContext(Dispatchers.IO) {
            propertyDao.getAllProperties().asFlow().firstOrNull()
        }?.map { it.toModel() }
        if (!localProperties.isNullOrEmpty()) {
            Log.d("PropertyRepository", "Found properties in Room: ${localProperties.size}")
            return localProperties
        } else {
            Log.d("PropertyRepository", "No properties found in Room. Fetching from Firestore...")
        }

        // Si Room est vide, récupérer depuis Firebase et stocker dans Room
        val querySnapshot = firestore.collection("properties").get().await()
        val properties = querySnapshot.documents.mapNotNull { it.toObject<PropertyModels>() }

        if (properties.isNotEmpty()) {
            Log.d("PropertyRepository", "Fetched properties from Firestore: ${properties.size}")

            withContext(Dispatchers.IO) {
                propertyDao.addProperties(properties.map { it.toEntity() }) // Assurez-vous que cette méthode existe dans votre DAO
            }
        } else {
            Log.d("PropertyRepository", "No properties found in Firestore.")
        }

        return properties
    }


    suspend fun getPropertyById(id: String): PropertyModels? {
        // Vérifier d'abord dans Room
        val localProperty = propertyDao.getPropertyById(id)?.toModel()
        if (localProperty != null) {
            return localProperty
        }

        // Si non disponible localement, récupérer depuis Firebase
        val documentSnapshot = firestore.collection("properties").document(id).get().await()
        val property = documentSnapshot.toObject<PropertyModels>()

        // Stocker dans Room si la propriété est récupérée de Firebase
        property?.let { propertyDao.addProperty(it.toEntity()) }

        return property
    }


}
