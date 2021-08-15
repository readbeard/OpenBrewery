package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Response

class RemoteDataSource@Inject constructor(
    private val breweriesApi: BreweryApi
) {
    suspend fun getBreweries(): Response<List<Brewery>> {
        return breweriesApi.getBreweries()
    }
}
