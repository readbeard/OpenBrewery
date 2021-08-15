package com.readbeard.openbrewery.app.base.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class EnvironmentModule {

    @Provides
    fun provideEnvironment() = Environment("https://api.openbrewerydb.org/")
}