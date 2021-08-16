package com.readbeard.openbrewery.app.beerlist.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import kotlinx.coroutines.flow.Flow

@Dao
interface BreweryDao {
    @Query("select * from breweries where name like '%' || :searchQuery || '%'")
    fun getBreweries(searchQuery: String): Flow<List<Brewery>>
}
