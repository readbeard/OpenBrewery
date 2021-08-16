package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.local.LocalBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.remote.RemoteBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import timber.log.Timber
import javax.inject.Inject

class BreweryRepositoryImpl @Inject constructor(
    private val localDataStore: LocalBreweryDataStore,
    private val remoteDataStore: RemoteBreweryDataStore
) : BreweryRepository {

    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        val localResult = localDataStore.getBreweries(searchQuery)
        val remoteResult = syncBrewerySearchResult(searchQuery)

        return merge(localResult, remoteResult)
    }

    override suspend fun syncBrewerySearchResult(searchQuery: String):
        Flow<CustomResult<List<Brewery>>> {
        val remoteFlow = remoteDataStore.getBreweries(searchQuery)
        remoteFlow.collect { breweriesListResult ->
            if (breweriesListResult is CustomResult.Success) {
                val breweryList = breweriesListResult.value
                addBreweries(breweryList)
                    .collect {
                        Timber.d("Successfully saved on local db")
                    }
            } else {
                Timber.d("No data found in remote data source")
            }
        }
        return remoteFlow
    }

    override suspend fun addBreweries(breweriesList: List<Brewery>): Flow<CustomResult<List<Brewery>>> {
        return localDataStore.addBreweries(breweriesList)
    }

    override suspend fun loadBreweriesAtPage(page: Int): CustomResult<List<Brewery>> {
        return remoteDataStore.getBreweries(page)
    }
}
