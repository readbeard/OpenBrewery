package com.readbeard.openbrewery.app.beerlist.data.local

import com.readbeard.openbrewery.app.beerlist.data.local.database.BreweryDao
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class LocalBreweryDataStore @Inject constructor(private val breweryDao: BreweryDao) :
    BreweryDataStore {
    override fun getBreweriesStream(searchQuery: String): Flow<List<Brewery>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBreweries(searchQuery: String): Flow<CustomResult<List<Brewery>>> {
        return flow {
            emit(CustomResult.Loading)
            breweryDao
                .getBreweries(searchQuery)
                .map { breweryList ->
                    breweryList.map {
                        Brewery(
                            id = it.id,
                            obdbId = it.obdbId,
                            name = it.name,
                            breweryType = it.breweryType,
                            street = it.street,
                            address2 = it.address2,
                            address3 = it.address3,
                            city = it.city,
                            state = it.state,
                            countyProvince = it.countyProvince,
                            postalCode = it.postalCode,
                            country = it.country,
                            longitude = it.longitude,
                            latitude = it.latitude,
                            phone = it.phone,
                            websiteUrl = it.websiteUrl,
                            updatedAt = it.updatedAt,
                            createdAt = it.createdAt
                        )
                    }
                    emit(CustomResult.Success(breweryList))
                }
                .catch {
                    Timber.d("Breweries retrieval from local db failed")
                    emit(CustomResult.Error(it))
                }
        }
    }

    override fun addBreweries(breweriesList: List<Brewery>): Flow<Unit> {
        TODO("Not yet implemented")
    }
}
