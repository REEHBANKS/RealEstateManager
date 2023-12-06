package com.openclassrooms.realestatemanager.data.models

import java.util.Date

data class PropertyModels(
    var id: String,
    var type: String, // flat, loft, duplex, house
    var price: Double, // in dollars
    var area: Double, // in square meters
    var numberOfRooms: Int,
    var numberOfBathrooms:Int,
    var fullDescription: String,
    var address: String,
    var neighborhood: String,
    var nearbyPointsOfInterest: List<String>, // schools, shops, parks, etc.
    var status: Boolean, // true for available, false for sold
    val marketEntryDate: Date,
    var saleDate: Date?, // nullable, only if sold
    val agentId: Int,
    var latitude: Double,
    var longitude: Double
)

