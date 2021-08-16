package com.readbeard.openbrewery.app.beerlist.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breweries")
data class BreweryEntity(
    val id: Int,
    @PrimaryKey
    val obdbId: String,
    val name: String,
    val breweryType: String,
    val street: String,
    val address2: String,
    val address3: String,
    val city: String,
    val state: String,
    val countyProvince: String,
    val postalCode: String,
    val country: String,
    val longitude: String,
    val latitude: String,
    val phone: String,
    val websiteUrl: String,
    val updatedAt: String,
    val createdAt: String
)
