package com.readbeard.openbrewery.app.beerlist.data.remote

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BreweryApi {
    @GET("/breweries")
    suspend fun searchBreweries(): Response<List<Brewery>>

    @GET("/breweries")
    suspend fun searchBreweriesAtPage(@Query("page") page: Int): Response<List<Brewery>>
}
