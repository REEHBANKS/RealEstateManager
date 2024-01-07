package com.openclassrooms.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.models.modelRoom.PropertyEntity


@Dao
interface PropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProperty(property: PropertyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProperties(properties: List<PropertyEntity>)

    @Query("SELECT * FROM properties")
    fun getAllProperties(): LiveData<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: String): PropertyEntity?

    @Update
    suspend fun updateProperty(property: PropertyEntity)
}
