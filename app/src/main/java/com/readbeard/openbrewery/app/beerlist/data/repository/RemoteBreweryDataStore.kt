package com.readbeard.openbrewery.app.beerlist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RemoteBreweryDataStore @Inject constructor(private val breweryApi: BreweryApi) :
    BreweryDataStore {
    override fun getBreweriesStream(searchQuery: String): Flow<List<Brewery>> {
        TODO("Not yet implemented")
    }

    override fun getBreweries(searchQuery: String): LiveData<List<Brewery>> {
        val requestLiveData = MutableLiveData<List<Brewery>>()
        Timber.d("CALLING GET BREWERIES API")

        CoroutineScope(Dispatchers.IO).launch {
            val response = breweryApi.getBreweries()
            withContext(Dispatchers.Default) {
                if (response.isSuccessful) {
                    val items = response.body()
                    Timber.d(items!!.toString())
                    requestLiveData.postValue(items!!)
                    Timber.d(" ***** \n  ${requestLiveData.value.toString()}")
                }
            }
        }
        return requestLiveData
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
