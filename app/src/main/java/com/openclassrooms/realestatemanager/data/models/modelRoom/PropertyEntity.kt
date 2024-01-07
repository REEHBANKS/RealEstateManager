package com.openclassrooms.realestatemanager.data.models.modelRoom
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import java.util.Date

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey val id: String,
    val type: String,
    val price: Int,
    val area: Int,
    val numberOfRooms: Int,
    val numberOfBathrooms: Int,
    val fullDescription: String,
    val address: String,
    val neighborhood: String,
    val nearbyPointsOfInterest: String, // Converti en JSON
    val status: Boolean,
    val marketEntryDate: Long, // Date convertie en timestamp
    val saleDate: Long?, // Nullable, convertie en timestamp
    val agentId: String,
    val latitude: Double,
    val longitude: Double
)

fun PropertyModels.toEntity(): PropertyEntity {
    return PropertyEntity(
        id = this.id,
        type = this.type,
        price = this.price,
        area = this.area,
        numberOfRooms = this.numberOfRooms,
        numberOfBathrooms = this.numberOfBathrooms,
        fullDescription = this.fullDescription,
        address = this.address,
        neighborhood = this.neighborhood,
        nearbyPointsOfInterest = Gson().toJson(this.nearbyPointsOfInterest),
        status = this.status,
        marketEntryDate = this.marketEntryDate.time,
        saleDate = this.saleDate?.time,
        agentId = this.agentId,
        latitude = this.latitude,
        longitude = this.longitude
    )

}

fun PropertyEntity.toModel(): PropertyModels {
    return PropertyModels(
        id = this.id,
        type = this.type,
        price = this.price,
        area = this.area,
        numberOfRooms = this.numberOfRooms,
        numberOfBathrooms = this.numberOfBathrooms,
        fullDescription = this.fullDescription,
        address = this.address,
        neighborhood = this.neighborhood,
        nearbyPointsOfInterest = Gson().fromJson(this.nearbyPointsOfInterest, object : TypeToken<List<String>>() {}.type),
        status = this.status,
        marketEntryDate = Date(this.marketEntryDate),
        saleDate = this.saleDate?.let { Date(it) },
        agentId = this.agentId,
        latitude = this.latitude,
        longitude = this.longitude
    )
}
