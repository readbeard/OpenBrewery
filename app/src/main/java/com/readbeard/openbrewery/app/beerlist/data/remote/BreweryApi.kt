package com.readbeard.openbrewery.app.beerlist.data.remote

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import retrofit2.Response
import retrofit2.http.GET

interface BreweryApi {
    @GET("/breweries")
    suspend fun getBreweries(): Response<List<Brewery>>
}
