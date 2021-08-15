package com.readbeard.openbrewery.app.beerlist.data.repository

import androidx.lifecycle.LiveData
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import kotlinx.coroutines.flow.Flow

interface BreweryDataStore {
    fun getBreweriesStream(searchQuery: String): Flow<List<Brewery>>

    fun getBreweries(searchQuery: String): LiveData<List<Brewery>>

    fun addBreweries(breweriesList: List<Brewery>): Flow<Unit>

    fun saveSearchHistory(list: List<String>): Flow<Unit>

    fun saveSearchHistory(currentSearch: String): Flow<Unit>

    fun getSearchHistory(): Flow<List<String>>
}
