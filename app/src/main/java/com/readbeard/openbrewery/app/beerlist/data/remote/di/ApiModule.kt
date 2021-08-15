package com.readbeard.openbrewery.app.beerlist.data.remote.di

import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepository
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepositoryImpl
import com.readbeard.openbrewery.app.beerlist.data.repository.LocalBreweryDataStore
import com.readbeard.openbrewery.app.beerlist.data.repository.MakeBreweryApiFactory
import com.readbeard.openbrewery.app.beerlist.data.repository.RemoteBreweryDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideBreweryApi() = MakeBreweryApiFactory.makeBreweryApi()

    @Provides
    @Named("local")
    fun provideLocalBreweryDataStore(): BreweryDataStore = LocalBreweryDataStore()

    @Provides
    @Named("remote")
    fun provideRemoteBreweryDataStore(api: BreweryApi): BreweryDataStore = RemoteBreweryDataStore(api)

    @Provides
    fun provideBreweryRepositoryImpl(
        @Named("local") localMovieDataStore: LocalBreweryDataStore,
        @Named("remote") remoteMovieDataStore: RemoteBreweryDataStore
    ): BreweryRepository = BreweryRepositoryImpl(localMovieDataStore, remoteMovieDataStore)
}
