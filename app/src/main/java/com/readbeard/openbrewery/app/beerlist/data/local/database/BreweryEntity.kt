package com.readbeard.openbrewery.app.beerlist.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breweries")
data class BreweryEntity(
    val id: Int,
    @PrimaryKey
    val obdbId: String,
    val name: String? = "Unknown",
    val breweryType: String? = "Unknown",
    val street: String? = "Unknown",
    val address2: String? = "Unknown",
    val address3: String? = "Unknown",
    val city: String? = "Unknown",
    val state: String? = "Unknown",
    val countyProvince: String? = "Unknown",
    val postalCode: String? = "Unknown",
    val country: String? = "Unknown",
    val longitude: String? = "Unknown",
    val latitude: String? = "Unknown",
    val phone: String? = "Unknown",
    val websiteUrl: String? = "Unknown",
    val updatedAt: String? = "Unknown",
    val createdAt: String? = "Unknown"
)
