package com.openclassrooms.realestatemanager.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.openclassrooms.realestatemanager.data.PhotoDao
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription
import com.openclassrooms.realestatemanager.data.models.modelRoom.toEntity
import com.openclassrooms.realestatemanager.data.models.modelRoom.toModel
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import java.util.UUID
class PictureRepository(
    private val firestore: FirebaseFirestore,
    private val photoDao: PhotoDao
) {

    suspend fun getMainPictureForProperty(propertyId: String): PhotoDescription? {
        // Check first in Room
        val localMainPicture = photoDao.getMainPictureForProperty(propertyId)?.toModel()
        if (localMainPicture != null) {
            return localMainPicture
        }

        // If not available locally, retrieve from Firebase
        val querySnapshot = firestore.collection("pictures")
            .whereEqualTo("propertyId", propertyId)
            .whereEqualTo("isMain", true)
            .limit(1)
            .get().await()

        val photoDescription = querySnapshot.documents.firstOrNull()?.toObject<PhotoDescription>()

        // Store in Room if photo is retrieved from Firebase
        photoDescription?.let { photoDao.addPhoto(it.toEntity()) }

        return photoDescription
    }


    suspend fun getPicturesForProperty(propertyId: String): List<PhotoDescription> {
        // Check first in Room
        val localPictures = photoDao.getPicturesForProperty(propertyId).value?.map { it.toModel() }
        if (!localPictures.isNullOrEmpty()) {
            return localPictures
        }

        // If not available locally, retrieve from Firebase
        val querySnapshot = firestore.collection("pictures")
            .whereEqualTo("propertyId", propertyId)
            .get().await()

        val photos = querySnapshot.documents.mapNotNull { it.toObject<PhotoDescription>() }

        // Store in Room if photos are retrieved from Firebase
        photoDao.addPhotos(photos.map { it.toEntity() })

        return photos
    }


    suspend fun addPhotos(photos: List<PhotoDescription>): Boolean {
        return try {
            // Add photos to Firebase
            val batch = firestore.batch()
            photos.forEach { photo ->
                val photoRef = firestore.collection("pictures").document()
                val photoMap = mapOf(
                    "description" to photo.description,
                    "id" to photo.id,
                    "propertyId" to photo.propertyId,
                    "uri" to photo.uri,
                    "isMain" to photo.isMain
                )
                batch.set(photoRef, photoMap)
            }
            batch.commit().await()

            // Add the same photos in Room
            val photoEntities = photos.map { it.toEntity() }
            photoDao.addPhotos(photoEntities)

            true
        } catch (e: Exception) {
            false
        }
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

    suspend fun getAllPictures(): List<PhotoDescription> {
        // Retrieving all photos from Room
        val localPictures = photoDao.getAllPhotos().map { it.toModel() }
        if (localPictures.isNotEmpty()) {
            return localPictures
        }

        // If no photos found locally, retrieve from Firebase
        val querySnapshot = firestore.collection("pictures").get().await()
        val photos = querySnapshot.documents.mapNotNull { it.toObject<PhotoDescription>() }

        // Store retrieved photos in Room
        if (photos.isNotEmpty()) {
            photoDao.addPhotos(photos.map { it.toEntity() })
        }

        return photos
    }
}
