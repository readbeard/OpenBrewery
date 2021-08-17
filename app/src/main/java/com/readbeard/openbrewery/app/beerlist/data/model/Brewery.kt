package com.readbeard.openbrewery.app.beerlist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Brewery(
    @SerialName("id")
    val id: Int,
    @SerialName("obdb_id")
    val obdbId: String,
    @SerialName("name")
    val name: String? = "Test Brewery",
    @SerialName("brewery_type")
    val breweryType: String? = "Test brewery type",
    @SerialName("street")
    val street: String? = "Test brewery street",
    @SerialName("address_2")
    val address2: String? = "Test brewery address2",
    @SerialName("address_3")
    val address3: String? = "Test brewery address3",
    @SerialName("city")
    val city: String? = "Test brewery city",
    @SerialName("state")
    val state: String? = "Test brewery state",
    @SerialName("county_province")
    val countyProvince: String? = "Test brewery contry province",
    @SerialName("postal_code")
    val postalCode: String? = "Test brewery postal code",
    @SerialName("country")
    val country: String? = "Test brewery country",
    @SerialName("longitude")
    val longitude: String? = "Test brewery longitude",
    @SerialName("latitude")
    val latitude: String? = "Test brewery latitude",
    @SerialName("phone")
    val phone: String? = "Test brewery phone",
    @SerialName("website_url")
    val websiteUrl: String? = "Test brewery website",
    @SerialName("updated_at")
    val updatedAt: String? = "Test brewery updated at",
    @SerialName("created_at")
    val createdAt: String? = "Test brewery created at"
)
