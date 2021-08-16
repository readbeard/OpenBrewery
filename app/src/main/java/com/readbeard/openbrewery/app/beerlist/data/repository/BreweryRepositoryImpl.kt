package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.local.LocalBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.remote.RemoteBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class BreweryRepositoryImpl @Inject constructor(
    private val localDataStore: LocalBreweryDataStore,
    private val remoteDataStore: RemoteBreweryDataStore
) : BreweryRepository {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        val localResult = localDataStore.getBreweries(searchQuery)
            .catch {
                syncBrewerySearchResult(searchQuery)
            }

        val remoteResult = syncBrewerySearchResult(searchQuery)

        return merge(localResult, remoteResult)
    }

    @FlowPreview
    override suspend fun syncBrewerySearchResult(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        return remoteDataStore.getBreweries(searchQuery)
    }

    override fun addBreweries(breweriesList: List<Brewery>): Flow<Unit> {
        return localDataStore.addBreweries(breweriesList)
    }
}
