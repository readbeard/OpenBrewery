package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class BreweryRepositoryImpl @Inject constructor(
    private val localDataStore: LocalBreweryDataStore,
    private val remoteDataStore: RemoteBreweryDataStore
) : BreweryRepository {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        /*val localResult = localDataStore.getBreweries(searchQuery)
            .map {
                if (it.isNullOrEmpty()) {
                    // TODO: create custom exception
                    throw RuntimeException("Movies matching the query isn't in db")
                } else {
                    it
                }
            }
            .catch {
                syncBrewerySearchResult(searchQuery)
            }

        val remoteResult = syncBrewerySearchResult(searchQuery)

        return merge(localResult, remoteResult)*/
        return syncBrewerySearchResult(searchQuery)
    }

    @FlowPreview
    override suspend fun syncBrewerySearchResult(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        return remoteDataStore.getBreweries(searchQuery)
    }

    override fun addBreweries(breweriesList: List<Brewery>): Flow<Unit> {
        return localDataStore.addBreweries(breweriesList)
    }

    override fun saveSearchResult(list: List<String>): Flow<Unit> {
        return localDataStore.saveSearchHistory(list)
    }

    override fun getSearchHistory(): Flow<List<String>> {
        return localDataStore.getSearchHistory()
    }
}
