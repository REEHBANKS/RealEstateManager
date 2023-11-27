package com.openclassrooms.realestatemanager.models

import java.util.Date

data class PropertyModels(
    val id: String,
    var type: String, // flat, loft, duplex, house
    var price: Double, // in dollars
    var area: Double, // in square meters
    var numberOfRooms: Int,
    var numberOfBathrooms:Int,
    var fullDescription: String,
    var photos: List<PhotoDescription>, // Assuming you have a PhotoDescription class
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

data class PhotoDescription(
    var uri: String,
    var description: String
)