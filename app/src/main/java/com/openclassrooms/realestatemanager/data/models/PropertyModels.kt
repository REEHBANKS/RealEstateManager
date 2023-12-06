package com.openclassrooms.realestatemanager.data.models

import java.util.Date


data class PropertyModels(
    var id: String = "",
    var type: String = "", // flat, loft, duplex, house
    var price: Int = 0, // in dollars
    var area: Int = 0, // in square meters
    var numberOfRooms: Int = 0,
    var numberOfBathrooms: Int = 0,
    var fullDescription: String = "",
    var address: String = "",
    var neighborhood: String = "",
    var nearbyPointsOfInterest: List<String> = emptyList(), // schools, shops, parks, etc.
    var status: Boolean = true, // true for available, false for sold
    val marketEntryDate: Date = Date(),
    var saleDate: Date? = null, // nullable, only if sold
    val agentId: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)


