package com.readbeard.openbrewery.app.beerlist.data.remote

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class RemoteBreweryDataStore @Inject constructor(private val breweryApi: BreweryApi) :
    BreweryDataStore {

    override suspend fun getBreweries(searchQuery: String): CustomResult<List<Brewery>> {
        try {
            val response = breweryApi.searchBreweries()
            return if (response.isSuccessful) {
                val items = response.body()
                if (items != null) {
                    CustomResult.Success(items)
                } else {
                    Timber.e("Retrieved a null list of breweries")
                    CustomResult.Error(Exception("Retrieved a null list of breweries"))
                }
            } else {
                Timber.e("getBreweries request failed with error ${response.errorBody()}")
                CustomResult.Error(Exception("getBreweries request failed with error ${response.errorBody()}"))
            }
        } catch (e: IOException) {
            Timber.d("Remote api call failed with exception: $e")
            return CustomResult.Error(Exception("Remote api call failed with exception: $e"))
        }
    }

    override suspend fun addBreweries(breweriesList: List<Brewery>): Flow<CustomResult<List<Brewery>>> {
        TODO("Not yet implemented")
    }

    suspend fun fetchBreweriesAtPage(page: Int): CustomResult<List<Brewery>> {
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

    suspend fun fetchBreweriesByFilter(filter: HashMap<String, Any>): CustomResult<List<Brewery>> {
        return try {
            val response = breweryApi.searchBreweriesBy(filter)

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
