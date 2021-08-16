package com.readbeard.openbrewery.app.beerlist.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreweryDao {
    @Query("select * from breweries where name like '%' || :searchQuery || '%'")
    suspend fun getBreweries(searchQuery: String): List<BreweryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBreweries(breweryList: List<BreweryEntity>): List<Long>
}
