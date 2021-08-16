package com.readbeard.openbrewery.app.beerlist.data.local

import com.readbeard.openbrewery.app.beerlist.data.local.database.BreweryDao
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

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
                            obdb_id = it.obdb_id,
                            name = it.name,
                            brewery_type = it.brewery_type,
                            street = it.street,
                            address_2 = it.address_2,
                            address_3 = it.address_3,
                            city = it.city,
                            state = it.state,
                            county_province = it.county_province,
                            postal_code = it.postal_code,
                            country = it.country,
                            longitude = it.longitude,
                            latitude = it.latitude,
                            phone = it.phone,
                            website_url = it.website_url,
                            updated_at = it.updated_at,
                            created_at = it.created_at
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
