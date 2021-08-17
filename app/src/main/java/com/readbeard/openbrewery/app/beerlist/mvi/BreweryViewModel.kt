package com.readbeard.openbrewery.app.beerlist.mvi

import androidx.lifecycle.viewModelScope
import com.readbeard.openbrewery.app.base.mvi.BaseViewModel
import com.readbeard.openbrewery.app.base.utils.CustomResult
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.model.BreweryFilter
import com.readbeard.openbrewery.app.beerlist.data.model.getFilter
import com.readbeard.openbrewery.app.beerlist.data.remote.BreweryApi
import com.readbeard.openbrewery.app.beerlist.data.repository.BreweryRepositoryImpl
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
) : BaseViewModel<BreweryState, BreweryIntent>(BreweryState.Loading(BreweryFilter.NAME, 1, "")) {

    private var page: Int
    private var savedBreweries: List<Brewery>
    private var selectedFilter: BreweryFilter = BreweryFilter.NONE

    init {
        onIntent(BreweryIntent.OnStart)
        page = 2
        savedBreweries = ArrayList()
        selectedFilter = BreweryFilter.NONE
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onIntent(intent: BreweryIntent) {
        when (intent) {
            BreweryIntent.OnStart -> {
                loadBreweries()
            }
            is BreweryIntent.OnSearchChanged -> {
                if (intent.filter != BreweryFilter.NONE) {
                    loadFilteredValues(intent.searchTerm)
                }
            }
            is BreweryIntent.OnScrolledDown -> {
                loadFilteredValues(intent.searchTerm, true)
            }
            is BreweryIntent.OnFilterSelected -> {
                loadFilteredValues(intent.searchTerm)
                onSelectedBreweryFilterChange(intent.filter)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun loadBreweries(searchTerm: String = "") {
        setState(BreweryState.Loading(selectedFilter, page, searchTerm))
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
                            savedBreweries = breweryList as ArrayList<Brewery>
                            setState(
                                BreweryState.Loaded(
                                    breweryList,
                                    selectedFilter,
                                    page,
                                    searchTerm
                                )
                            )
                        }
                        is CustomResult.Error -> {
                            setState(BreweryState.Error(selectedFilter, page, searchTerm))
                        }
                        is CustomResult.Loading -> {
                            setState(BreweryState.Loading(selectedFilter, page, searchTerm))
                        }
                    }
                }
        }
    }

    private fun loadFilteredValues(searchTerm: String = "", nextPage: Boolean = false) {
        viewModelScope.launch(Dispatchers.Main) {
            val filterHashMap: HashMap<String, Any> =
                hashMapOf(Pair(selectedFilter.value, searchTerm))
            if (nextPage) {
                filterHashMap[BreweryApi.page] = page
            } else {
                setState(BreweryState.Loading(selectedFilter, page, searchTerm))
            }

            // Simulate a delay, as the API is pretty fast
            delay(LOADING_DELAY)

            breweryRepositoryImpl.loadBreweriesByFilter(filterHashMap)
                .collect { result ->
                    when (result) {
                        is CustomResult.Success -> {
                            if (nextPage) {
                                page += 1
                            } else {
                                savedBreweries = ArrayList()
                            }
                            appendBreweries(result.value)
                            setState(
                                BreweryState.Loaded(
                                    savedBreweries,
                                    selectedFilter,
                                    page,
                                    searchTerm
                                )
                            )
                        }
                        is CustomResult.Error -> {
                            setState(
                                BreweryState.ErrorLoadingPage(
                                    savedBreweries,
                                    selectedFilter,
                                    page,
                                    searchTerm
                                )
                            )
                        }
                        else ->
                            return@collect
                    }
                }
        }
    }

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendBreweries(recipes: List<Brewery>) {
        val current = ArrayList(this.savedBreweries)
        current.addAll(recipes)
        this.savedBreweries = current
    }

    private fun onSelectedBreweryFilterChange(filter: BreweryFilter) {
        val newFilter = getFilter(filter.value)
        selectedFilter = if (newFilter == selectedFilter) {
            BreweryFilter.NONE
        } else {
            newFilter
        }
        page = 0
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5000L
        private const val LOADING_DELAY = 1000L
    }
}
