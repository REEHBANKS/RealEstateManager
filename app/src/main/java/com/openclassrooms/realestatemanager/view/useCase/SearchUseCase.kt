package com.openclassrooms.realestatemanager.view.useCase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.data.repository.PictureRepository
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val pictureRepository: PictureRepository
) {


    private val _searchResults = MutableStateFlow<List<PropertyModels>>(emptyList())
    val searchResults: StateFlow<List<PropertyModels>> = _searchResults

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun execute(
        selectedOptionType: String?,
        minSurface: Int,
        maxSurface: Int,
        minPrice: Int,
        maxPrice: Int,
        proximity: Set<String>,
        date: LocalDate?,
        filterByPhotoCount: Boolean,
        neighborhood: String?
    ): List<PropertyModels> {
        // Retrieve all properties from the repository.
        val allProperties = propertyRepository.getAllProperties()

        // Apply filtering based on the given parameters and return the results.
        val filteredProperties = allProperties.filter { property ->
            // Match property type if specified.
            val isTypeMatch = selectedOptionType == null || property.type == selectedOptionType

            // Match property area within the given range.
            val isAreaMatch = property.area in minSurface..maxSurface

            // Match property price within the given range.
            val isPriceMatch = property.price in minPrice..maxPrice

            // Match if property has all specified nearby points of interest.
            val isProximityMatch = proximity.isEmpty() || property.nearbyPointsOfInterest.containsAll(proximity)

            // Match if property was on the market before the specified date.
            val isDateMatch = date?.let {
                val cutoffInstant = it.atStartOfDay(ZoneId.systemDefault()).toInstant()
                property.marketEntryDate.toInstant().isBefore(cutoffInstant)
            } ?: true

            // Match if property is in the specified neighborhood.
            val isNeighborhoodMatch = neighborhood == null || property.neighborhood == neighborhood

            // If filtering by photo count is enabled, match properties with at least 3 photos.
            val isPhotoCountMatch = if (filterByPhotoCount) {
                val allPhotos = pictureRepository.getAllPictures()
                allPhotos.count { photo -> photo.propertyId == property.id } >= 3
            } else {
                true // Include all properties if filtering by photo count is not enabled.
            }

            // A property must match all criteria to be included.
            isTypeMatch && isAreaMatch && isPriceMatch && isProximityMatch && isDateMatch && isNeighborhoodMatch && isPhotoCountMatch

        }


        _searchResults.value = filteredProperties
        return filteredProperties

    }
}
