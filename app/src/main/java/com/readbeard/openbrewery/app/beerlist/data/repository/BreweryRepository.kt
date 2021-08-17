package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow

interface BreweryRepository {

    suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>>

    suspend fun loadBreweriesByFilter(filter: HashMap<String, Any>): Flow<CustomResult<List<Brewery>>>
}
