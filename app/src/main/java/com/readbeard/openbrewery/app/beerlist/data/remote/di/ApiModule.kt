package com.readbeard.openbrewery.app.beerlist.data.remote.di

import com.readbeard.openbrewery.app.beerlist.data.local.LocalBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import com.readbeard.openbrewery.app.beerlist.data.remote.RemoteBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepository
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepositoryImpl
import com.readbeard.openbrewery.app.beerlist.data.repository.MakeBreweryApiFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideBreweryApi() = MakeBreweryApiFactory.makeBreweryApi()

    @Provides
    @Named("remote")
    fun provideRemoteBreweryDataStore(api: BreweryApi): BreweryDataStore =
        RemoteBreweryDataStore(api)

    @Provides
    fun provideBreweryRepositoryImpl(
        @Named("local") localBreweryDataStore: LocalBreweryDataStore,
        @Named("remote") remoteBreweryDataStore: RemoteBreweryDataStore
    ): BreweryRepository = BreweryRepositoryImpl(localBreweryDataStore, remoteBreweryDataStore)
}
