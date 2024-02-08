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


    suspend fun getAllProperties(isInternetAvailable: Boolean): List<PropertyModels> {
        Log.d("PropertyRepository", "Fetching properties...")

        // Vérifier d'abord la connexion réseau
        if (isInternetAvailable) {
            val querySnapshot = firestore.collection("properties").get().await()
            val propertiesFromFirebase = querySnapshot.documents.mapNotNull { it.toObject<PropertyModels>() }

            // Récupérer les propriétés locales
            val localProperties = withContext(Dispatchers.IO) {
                propertyDao.getAllProperties().asFlow().firstOrNull()
            }?.map { it.toModel() }

            if (propertiesFromFirebase.isNotEmpty()) {
                Log.d("PropertyRepository", "Fetched properties from Firestore: ${propertiesFromFirebase.size}")

                withContext(Dispatchers.IO) {
                    // Ajouter les propriétés de Firebase dans Room s'il y a des différences
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

        // Récupérer les propriétés locales si la connexion n'est pas disponible
        val localProperties = withContext(Dispatchers.IO) {
            propertyDao.getAllProperties().asFlow().firstOrNull()
        }?.map { it.toModel() }

        return localProperties ?: emptyList()
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
