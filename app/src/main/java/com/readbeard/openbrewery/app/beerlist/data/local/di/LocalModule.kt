package com.readbeard.openbrewery.app.beerlist.data.local.di

import android.content.Context
import com.readbeard.openbrewery.app.beerlist.data.local.LocalBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.local.database.BreweryDB
import com.readbeard.openbrewery.app.beerlist.data.local.database.BreweryDao
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): BreweryDB = BreweryDB.getInstance(context)

    @Provides
    @Singleton
    fun provideBreweryDao(db: BreweryDB): BreweryDao = db.breweryDao()

    @Provides
    @Named("local")
    fun provideLocalBreweryDataStore(breweryDao: BreweryDao): BreweryDataStore =
        LocalBreweryDataStore(breweryDao)
}
