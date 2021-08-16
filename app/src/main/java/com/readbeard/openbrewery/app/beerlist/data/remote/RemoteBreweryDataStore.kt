package com.readbeard.openbrewery.app.beerlist.data.remote

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class RemoteBreweryDataStore @Inject constructor(private val breweryApi: BreweryApi) :
    BreweryDataStore {

    override fun getBreweriesStream(searchQuery: String): Flow<List<Brewery>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        return flow {
            emit(CustomResult.Loading)
            try {
                val response = breweryApi.searchBreweries()
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        emit(CustomResult.Success(items))
                    } else {
                        Timber.e("Retrieved a null list of breweries")
                    }
                } else {
                    emit(CustomResult.Error(Exception(response.errorBody().toString())))
                    Timber.e("getBreweries request failed with error ${response.errorBody()}")
                }
            } catch (e: IOException) {
                Timber.d("Remote api call failed with exception: $e")
            }
        }
    }

    override suspend fun addBreweries(breweriesList: List<Brewery>): Flow<CustomResult<List<Brewery>>> {
        TODO("Not yet implemented")
    }

    suspend fun getBreweries(page: Int): CustomResult<List<Brewery>> {
        return try {
            val response = breweryApi.searchBreweriesAtPage(page)

            if (response.isSuccessful && response.body() != null) {
                CustomResult.Success(response.body()!!)
            } else {
                CustomResult.Error(Exception("Failed to retrieve list of breweries ${response.errorBody()}"))
            }
        } catch (e: IOException) {
            Timber.d("Paginated breweries retrieval failed with exception $e")
            CustomResult.Error(e)
        }
    }
}
