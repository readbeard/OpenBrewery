package com.readbeard.openbrewery.app.beerlist.data.repository

import androidx.lifecycle.LiveData
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import kotlinx.coroutines.flow.Flow

interface BreweryRepository {

    fun getBreweries(searchQuery: String): LiveData<List<Brewery>>

    fun addBreweries(breweriesList: List<Brewery>): Flow<Unit>

    fun syncBrewerySearchResult(searchQuery: String): LiveData<List<Brewery>>

    fun saveSearchResult(list: List<String>): Flow<Unit>

    fun getSearchHistory(): Flow<List<String>>
}
