package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.base.utils.CustomResult
import com.readbeard.openbrewery.app.beerlist.data.local.LocalBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.remote.RemoteBreweryDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BreweryRepositoryImpl @Inject constructor(
    private val localDataStore: LocalBreweryDataStore,
    private val remoteDataStore: RemoteBreweryDataStore
) : BreweryRepository {

    @ExperimentalCoroutinesApi
    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        val localResult = localDataStore.getBreweries(searchQuery)
        val remoteResult = remoteDataStore.getBreweries(searchQuery)

        val emptyResponseFromDb =
            (localResult is CustomResult.Success && localResult.value.isEmpty()) ||
                localResult is CustomResult.Error

        if (remoteResult is CustomResult.Error) {
            return if (emptyResponseFromDb) {
                flow { emit(CustomResult.Error(Exception("Both data sources failed retrieving data"))) }
            } else {
                flow { emit(localResult) }
            }
        }

        return localDataStore.addBreweries((remoteResult as CustomResult.Success).value)
    }

    override suspend fun loadBreweriesByFilter(filter: HashMap<String, Any>): Flow<CustomResult<List<Brewery>>> {
        val fetchedBreweriesResult = remoteDataStore.fetchBreweriesByFilter(filter)
        if (fetchedBreweriesResult is CustomResult.Success) {
            localDataStore.addBreweries(fetchedBreweriesResult.value).collect()
        }
        return flow { emit(fetchedBreweriesResult) }
    }
}
