package com.readbeard.openbrewery.app.beerlist.data.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Brewery(
    @SerializedName("id")
    @Expose
    val id: Int = -1,
    @SerializedName("obdb_id")
    @Expose
    val obdb_id: String = "-1",
    @SerializedName("name")
    @Expose
    val name: String = "Test Brewery",
    @SerializedName("brewery_type")
    @Expose
    val brewery_type: String = "Test brewery type",
    @SerializedName("street")
    @Expose
    val street: String = "Test brewery street",
    @SerializedName("address_2")
    @Expose
    val address_2: String = "Test brewery address2",
    @SerializedName("address_3")
    @Expose
    val address_3: String = "Test brewery address3",
    @SerializedName("city")
    @Expose
    val city: String = "Test brewery city",
    @SerializedName("state")
    @Expose
    val state: String = "Test brewery state",
    @SerializedName("county_province")
    @Expose
    val county_province: String = "Test brewery contry province",
    @SerializedName("postal_code")
    @Expose
    val postal_code: String = "Test brewery postal code",
    @SerializedName("country")
    @Expose
    val country: String = "Test brewery country",
    @SerializedName("longitude")
    @Expose
    val longitude: String = "Test brewery longitude",
    @SerializedName("latitude")
    @Expose
    val latitude: String = "Test brewery latitude",
    @SerializedName("phone")
    @Expose
    val phone: String = "Test brewery phone",
    @SerializedName("website_url")
    @Expose
    val website_url: String = "Test brewery website",
    @SerializedName("updated_at")
    @Expose
    val updated_at: String = "Test brewery updated at",
    @SerializedName("created_at")
    @Expose
    val created_at: String = "Test brewery created at"
)
