package com.readbeard.openbrewery.app.beerlist.data.repository

import androidx.lifecycle.LiveData
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow

interface BreweryRepository {

    suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>>

    fun addBreweries(breweriesList: List<Brewery>): Flow<Unit>

    suspend fun syncBrewerySearchResult(searchQuery: String): Flow<CustomResult<List<Brewery>>>

    fun saveSearchResult(list: List<String>): Flow<Unit>

    fun getSearchHistory(): Flow<List<String>>
}
