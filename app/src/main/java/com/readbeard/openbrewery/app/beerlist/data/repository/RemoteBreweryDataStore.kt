package com.readbeard.openbrewery.app.beerlist.data.repository

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RemoteBreweryDataStore @Inject constructor(private val breweryApi: BreweryApi) :
    BreweryDataStore {

    override fun getBreweriesStream(searchQuery: String): Flow<List<Brewery>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        return flow {
            emit(CustomResult.Loading)
            val response = breweryApi.getBreweries()
            if (response.isSuccessful) {
                val items = response.body()
                if (items != null)
                    emit(CustomResult.Success(items))
                else
                    Timber.e("Retrieved a null list of breweries")
            } else {
                emit(CustomResult.Error(Exception(response.errorBody().toString())))
                Timber.e("getBreweries request failed with error ${response.errorBody()}")
            }
        }
    }

    override fun addBreweries(breweriesList: List<Brewery>): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun saveSearchHistory(list: List<String>): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun saveSearchHistory(currentSearch: String): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun getSearchHistory(): Flow<List<String>> {
        TODO("Not yet implemented")
    }
}