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
import java.time.LocalDate
@Suppress("DEPRECATION")
class PropertyRepository(
    private val firestore: FirebaseFirestore,
    private val propertyDao: PropertyDao
) {


    suspend fun addProperty(property: PropertyModels): String? {
        var newPropertyId: String? = null

        try {
            // Adding to Firebase
            val newPropertyRef = firestore.collection("properties").document()
            newPropertyId = newPropertyRef.id
            val newProperty = property.copy(id = newPropertyId)
            newPropertyRef.set(newProperty).await()

            // Conversion and storage in Room
            val propertyEntity = newProperty.toEntity()
            propertyDao.addProperty(propertyEntity)
        } catch (e: Exception) {
            // Handle exception if necessary
        }

        return newPropertyId
    }

    suspend fun updateProperty(propertyId: String, updatedProperty: PropertyModels): Boolean {
        return try {
            // Updating in Firebase
            val propertyToUpdate = updatedProperty.copy(id = propertyId)
            firestore.collection("properties").document(propertyId).set(propertyToUpdate).await()

            // Updating in Room
            val propertyEntity = propertyToUpdate.toEntity()
            propertyDao.updateProperty(propertyEntity)

            true // Return true if update is successful
        } catch (e: Exception) {
            false // Return false in case of failure
        }
    }


    suspend fun getAllProperties(isInternetAvailable: Boolean): List<PropertyModels> {
        Log.d("PropertyRepository", "Fetching properties...")

        // Check network connectivity first
        if (isInternetAvailable) {
            val querySnapshot = firestore.collection("properties").get().await()
            val propertiesFromFirebase = querySnapshot.documents.mapNotNull { it.toObject<PropertyModels>() }

            // Get local properties
            val localProperties = withContext(Dispatchers.IO) {
                propertyDao.getAllProperties().asFlow().firstOrNull()
            }?.map { it.toModel() }

            if (propertiesFromFirebase.isNotEmpty()) {
                Log.d("PropertyRepository", "Fetched properties from Firestore: ${propertiesFromFirebase.size}")

                withContext(Dispatchers.IO) {
                    // Add Firebase properties to Room if there are differences
                    val propertiesToAdd = propertiesFromFirebase.filterNot { localProperties?.contains(it) == true }
                    if (propertiesToAdd.isNotEmpty()) {
                        propertyDao.addProperties(propertiesToAdd.map { it.toEntity() })
                    }
                }

                return propertiesFromFirebase
            } else if (!localProperties.isNullOrEmpty()) {
                Log.d("PropertyRepository", "No properties found in Firestore. Returning local properties.")
                return localProperties
            } else {
                Log.d("PropertyRepository", "No properties found in Firestore or Room.")
            }
        } else {
            Log.d("PropertyRepository", "Network is not available. Returning local properties.")
        }

        // Get local properties if connection is not available
        val localProperties = withContext(Dispatchers.IO) {
            propertyDao.getAllProperties().asFlow().firstOrNull()
        }?.map { it.toModel() }

        return localProperties ?: emptyList()
    }



    suspend fun getPropertyById(id: String): PropertyModels? {
        // Check first in Room
        val localProperty = propertyDao.getPropertyById(id)?.toModel()
        if (localProperty != null) {
            return localProperty
        }

        // If not available locally, retrieve from Firebase
        val documentSnapshot = firestore.collection("properties").document(id).get().await()
        val property = documentSnapshot.toObject<PropertyModels>()

        // Store in Room if property is retrieved from Firebase
        property?.let { propertyDao.addProperty(it.toEntity()) }

        return property
    }



}
