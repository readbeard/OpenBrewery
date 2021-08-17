package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow

interface BreweryDataStore {
    suspend fun getBreweries(searchQuery: String): CustomResult<List<Brewery>>

    suspend fun addBreweries(breweriesList: List<Brewery>): Flow<CustomResult<List<Brewery>>>
}
