package com.readbeard.openbrewery.app.beerlist.mvi

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.readbeard.openbrewery.app.base.mvi.BaseViewModel
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepositoryImpl
import com.readbeard.openbrewery.app.beerlist.utils.CustomResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
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

    val page = mutableStateOf(1)
    var breweryListScrollPosition = 0
    var savedBreweries = mutableStateOf(ArrayList<Brewery>())

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
            is BreweryIntent.OnScrolledDown -> {
                nextPage()
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
                started = WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = CustomResult.Loading
            )
                .collect { result ->
                    when (result) {
                        is CustomResult.Success -> {
                            val breweryList = result.value
                            savedBreweries.value = breweryList as ArrayList<Brewery>
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

    fun nextPage() {
        viewModelScope.launch(Dispatchers.Main) {
            // prevent duplicate event due to recompose happening to quickly
            incrementPage()

            if (page.value > 1) {
                val result = breweryRepositoryImpl.loadBreweriesAtPage(page.value)
                appendBreweries((result as CustomResult.Success).value) // TODO: handle error case
            }

            setState(BreweryState.Loaded(savedBreweries.value))
        }
    }

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendBreweries(recipes: List<Brewery>) {
        val current = ArrayList(this.savedBreweries.value)
        current.addAll(recipes)
        this.savedBreweries.value = current
    }

    private fun incrementPage() {
        page.value = page.value + 1
    }

    fun onChangeBreweryScrollPosition(position: Int) {
        breweryListScrollPosition = position
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5000L
        const val PAGE_SIZE = 20
    }
}
