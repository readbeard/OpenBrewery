package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LocalBreweryDataStore @Inject constructor() : BreweryDataStore {
    override fun getBreweriesStream(searchQuery: String): Flow<List<Brewery>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        TODO("Not yet implemented")
    }

    override fun addBreweries(breweriesList: List<Brewery>): Flow<Unit> {
        TODO("Not yet implemented")
    }
}
