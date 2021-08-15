package com.readbeard.openbrewery.app.beerlist.mvi

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.readbeard.openbrewery.app.base.mvi.BaseViewModel
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class BreweryViewModel @Inject constructor(
    private val breweryRepositoryImpl: BreweryRepositoryImpl
) : BaseViewModel<BreweryState, BreweryIntent>(BreweryState.Loading) {

    init {
        onIntent(BreweryIntent.OnStart)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onIntent(intent: BreweryIntent) {
        when (intent) {
            BreweryIntent.OnStart -> {
                runBlocking {
                    loadBreweries()
                }
            }
            is BreweryIntent.OnSearchChanged -> {
                runBlocking {
                    loadBreweries(intent.searchTerm)
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun loadBreweries(searchTerm: String = "") {
        setState(BreweryState.Loading)
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val breweries = breweryRepositoryImpl.getBreweries(searchTerm)
                breweries.observeForever(Observer {
                    if (it != null) {
                        setState(BreweryState.Loaded(it))
                    }
                })
            } catch (exception: Exception) {
                setState(BreweryState.Error)
            }
        }
    }
}
