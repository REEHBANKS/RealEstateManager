package com.openclassrooms.realestatemanager.data.models.modelRoom

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription


@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val propertyId: String,
    val uri: String,
    val description: String,
    val isMain: Boolean
)

fun PhotoDescription.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = this.id,
        propertyId = this.propertyId,
        uri = this.uri,
        description = this.description,
        isMain = this.isMain
    )
}

fun PhotoEntity.toModel(): PhotoDescription {
    return PhotoDescription(
        id = this.id,
        propertyId = this.propertyId,
        uri = this.uri,
        description = this.description,
        isMain = this.isMain
    )
}


