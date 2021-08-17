package com.readbeard.openbrewery.app.beerlist.data.remote

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface BreweryApi {
    @GET("/breweries")
    suspend fun searchBreweries(): Response<List<Brewery>>

    @GET("/breweries")
    suspend fun searchBreweriesAtPage(@Query("page") page: Int = 1): Response<List<Brewery>>

    @GET("/breweries")
    suspend fun searchBreweriesBy(@QueryMap filter: Map<String, @JvmSuppressWildcards Any>): Response<List<Brewery>>

    companion object {
        const val name = "by_name"
        const val city = "by_city"
        const val postalCode = "by_postal"
        const val state = "by_state"
        const val distance = "by_dist"
        const val type = "by_type"
        const val sort = "sort"
        const val page = "page"
    }
}
