package com.readbeard.openbrewery.app.beerlist.data.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
object MakeBreweryApiFactory {
    private const val BASE_URL = "https://api.openbrewerydb.org/"
    private const val TIMEOUT_MILLIS = 120L

    fun makeBreweryApi(): BreweryApi {
        val okHttpClient = makeOkHttpClient(
            makeLoggingInterceptor()
        )
        return makeBreweryApi(okHttpClient)
    }

    private fun makeBreweryApi(okHttpClient: OkHttpClient): BreweryApi {
        return makeRetrofit(okHttpClient).create(BreweryApi::class.java)
    }

    private fun makeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun makeOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(TIMEOUT_MILLIS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_MILLIS, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(httpLogger)
        loggingInterceptor.level =
            HttpLoggingInterceptor.Level.BODY // No need to check if debug, since using Timber
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
