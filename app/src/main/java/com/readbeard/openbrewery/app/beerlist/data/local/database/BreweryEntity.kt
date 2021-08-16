package com.readbeard.openbrewery.app.beerlist.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breweries")
data class BreweryEntity(
    val id: Int,
    @PrimaryKey
    val obdb_id: String,
    val name: String,
    val brewery_type: String,
    val street: String,
    val address_2: String,
    val address_3: String,
    val city: String,
    val state: String,
    val county_province: String,
    val postal_code: String,
    val country: String,
    val longitude: String,
    val latitude: String,
    val phone: String,
    val website_url: String,
    val updated_at: String,
    val created_at: String
)
