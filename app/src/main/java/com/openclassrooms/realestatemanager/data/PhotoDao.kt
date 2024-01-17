package com.openclassrooms.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.models.modelRoom.PhotoEntity


@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhotos(photos: List<PhotoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhoto(photo: PhotoEntity)


    @Query("SELECT * FROM photos WHERE propertyId = :propertyId AND isMain = 1 LIMIT 1")
    suspend fun getMainPictureForProperty(propertyId: String): PhotoEntity?

    @Query("SELECT * FROM photos WHERE propertyId = :propertyId")
    fun getPicturesForProperty(propertyId: String): LiveData<List<PhotoEntity>>

    @Query("SELECT * FROM photos")
    suspend fun getAllPhotos(): List<PhotoEntity>

}
