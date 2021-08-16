package com.readbeard.openbrewery.app.beerlist.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BreweryEntity::class], version = 1)
abstract class BreweryDB : RoomDatabase() {
    abstract fun breweryDao(): BreweryDao

    companion object {
        private var instance: BreweryDB? = null

        @JvmStatic
        @Synchronized
        fun getInstance(applicationContext: Context): BreweryDB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    applicationContext,
                    BreweryDB::class.java,
                    "brewery.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance!!
        }
    }
}
