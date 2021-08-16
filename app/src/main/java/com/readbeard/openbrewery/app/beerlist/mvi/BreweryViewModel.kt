package com.readbeard.openbrewery.app.beerlist.mvi

import androidx.lifecycle.viewModelScope
import com.readbeard.openbrewery.app.base.mvi.BaseViewModel
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepositoryImpl
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                loadBreweries()
            }
            is BreweryIntent.OnSearchChanged -> {
                loadBreweries(intent.searchTerm)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun loadBreweries(searchTerm: String = "") {
        setState(BreweryState.Loading)
        viewModelScope.launch(Dispatchers.Main) {
            val breweries = breweryRepositoryImpl.getBreweries(searchTerm)
            breweries.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000),
                initialValue = CustomResult.Loading
            )
                .collect { result ->
                    when (result) {
                        is CustomResult.Success -> {
                            val breweryList = result.value
                            setState(BreweryState.Loaded(breweryList))
                        }
                        is CustomResult.Error -> {
                            setState(BreweryState.Error)
                        }
                        is CustomResult.Loading -> {
                            setState(BreweryState.Loading)
                        }
                    }
                }
        }
    }
}
