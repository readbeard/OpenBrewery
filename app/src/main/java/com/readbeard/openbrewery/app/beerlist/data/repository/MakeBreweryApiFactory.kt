package com.readbeard.openbrewery.app.beerlist.data.repository

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object MakeBreweryApiFactory {
    private const val BASE_URL = "https://api.openbrewerydb.org/"
    fun makeBreweryApi(): BreweryApi {
        val okHttpClient = makeOkHttpClient(
            makeLoggingInterceptor()
        )
        return makeBreweryApi(okHttpClient, makeGson())
    }

    private fun makeBreweryApi(okHttpClient: OkHttpClient, gson: Gson): BreweryApi {
        return makeRetrofit(okHttpClient, gson).create(BreweryApi::class.java)
    }

    private fun makeRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun makeOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun makeGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create()
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(httpLogger)
        loggingInterceptor.level =
            HttpLoggingInterceptor.Level.BODY //No need to check if debug, since using Timber
        return loggingInterceptor
    }

    private val httpLogger: HttpLoggingInterceptor.Logger by lazy {
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.d("HTTP::TrendingService:: $message")
            }
        }
    }
}