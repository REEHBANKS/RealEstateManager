package com.openclassrooms.realestatemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.data.models.modelRoom.PhotoEntity
import com.openclassrooms.realestatemanager.data.models.modelRoom.PropertyEntity

@Database(entities = [PropertyEntity::class, PhotoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun photoDao(): PhotoDao


}
