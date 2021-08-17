package com.readbeard.openbrewery.app.beerlist.data.local

import android.database.sqlite.SQLiteException
import com.readbeard.openbrewery.app.beerlist.data.local.database.BreweryDao
import com.readbeard.openbrewery.app.beerlist.data.local.database.BreweryEntity
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class LocalBreweryDataStore @Inject constructor(private val breweryDao: BreweryDao) :
    BreweryDataStore {

    override suspend fun getBreweries(searchQuery: String): CustomResult<List<Brewery>> {
        try {
            val breweryList = breweryDao
                .getBreweries(searchQuery)
                .map {
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
            return CustomResult.Success(breweryList)
        } catch (e: SQLiteException) {
            Timber.d("Breweries retrieval from local db failed")
            return CustomResult.Error(e)
        }
    }

    override suspend fun addBreweries(breweriesList: List<Brewery>): Flow<CustomResult<List<Brewery>>> {
        return flow {
            emit(CustomResult.Loading)
            val addedBreweriesIds = breweryDao.addBreweries(
                breweriesList.map {
                    mapBreweryToBreweryEntity(it)
                }
            )

            if (addedBreweriesIds.isNotEmpty()) { // Sanity check
                Timber.d("Successfully saved entries to db")
                emit(CustomResult.Success(breweriesList))
            } else {
                emit(CustomResult.Error(RuntimeException("Failed saving breweries to db")))
            }
        }
    }

    private fun mapBreweryToBreweryEntity(it: Brewery): BreweryEntity {
        return BreweryEntity(
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
}
