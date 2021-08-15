package com.readbeard.openbrewery.app.beerlist.data.repository

import androidx.lifecycle.LiveData
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

class BreweryRepositoryImpl @Inject constructor(
    private val localDataStore: LocalBreweryDataStore,
    private val remoteDataStore: RemoteBreweryDataStore
) : BreweryRepository {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getBreweries(searchQuery: String): LiveData<List<Brewery>> {
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
    override fun syncBrewerySearchResult(searchQuery: String): LiveData<List<Brewery>> {
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
