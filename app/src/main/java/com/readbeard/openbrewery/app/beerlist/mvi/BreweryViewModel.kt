package com.readbeard.openbrewery.app.beerlist.mvi

import androidx.compose.runtime.mutableStateOf
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
) : BaseViewModel<BreweryState, BreweryIntent>(BreweryState.Loading) {

    private var page: Int
    private var savedBreweries: List<Brewery>
    var selectedFilter = mutableStateOf(BreweryFilter.NAME)

    init {
        onIntent(BreweryIntent.OnStart)
        page = 2
        savedBreweries = ArrayList()
        selectedFilter.value = BreweryFilter.NAME
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onIntent(intent: BreweryIntent) {
        when (intent) {
            BreweryIntent.OnStart -> {
                loadBreweries()
            }
            is BreweryIntent.OnSearchChanged -> {
                loadFilteredValues(intent.searchTerm)
            }
            is BreweryIntent.OnScrolledDown -> {
                nextPage()
            }
            is BreweryIntent.OnFilterSelected -> {
                onSelectedBreweryFilterChange(intent.filter)
                loadFilteredValues(intent.searchTerm)
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
                            savedBreweries = result.value
                            setState(
                                BreweryState.Loaded(
                                    savedBreweries,
                                    selectedFilter.value,
                                    page,
                                    searchTerm
                                )
                            )
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

    private fun nextPage() {
        viewModelScope.launch(Dispatchers.Main) {
            val filterHashMap: HashMap<String, Any> =
                hashMapOf(
                    Pair(state.value.selectedFilter.value, state.value.searchTerm),
                    Pair(BreweryApi.page, page + 1)
                )

            // Simulate a delay, as the API is pretty fast
            delay(LOADING_DELAY)

            breweryRepositoryImpl.loadBreweriesByFilter(filterHashMap)
                .collect { result ->
                    when (result) {
                        is CustomResult.Success -> {
                            appendBreweries(result.value)
                            setState(
                                BreweryState.Loaded(
                                    savedBreweries,
                                    selectedFilter.value,
                                    page + 1,
                                    state.value.searchTerm,
                                    result.value.isEmpty()
                                )
                            )
                        }
                        is CustomResult.Error -> {
                            setState(BreweryState.Error)
                        }
                        else ->
                            return@collect
                    }
                }
        }
    }

    private fun loadFilteredValues(searchTerm: String = state.value.searchTerm) {
        if (searchTerm.isEmpty() && selectedFilter.value != state.value.selectedFilter) return

        viewModelScope.launch(Dispatchers.Main) {
            val filterHashMap: HashMap<String, Any> =
                hashMapOf(Pair(state.value.selectedFilter.value, searchTerm))

            setState(BreweryState.Loading)
            // Simulate a delay, as the API is pretty fast
            delay(LOADING_DELAY)

            breweryRepositoryImpl.loadBreweriesByFilter(filterHashMap)
                .collect { result ->
                    when (result) {
                        is CustomResult.Success -> {
                            savedBreweries = result.value
                            setState(
                                BreweryState.Loaded(
                                    result.value,
                                    selectedFilter.value,
                                    page,
                                    searchTerm
                                )
                            )
                        }
                        is CustomResult.Error -> {
                            setState(
                                BreweryState.Error
                            )
                        }
                        is CustomResult.Loading -> {
                            setState(
                                BreweryState.Loading
                            )
                        }
                    }
                }
        }
    }

    private fun appendBreweries(newBreweries: List<Brewery>) {
        savedBreweries = listOf(savedBreweries, newBreweries).flatten()
    }

    private fun onSelectedBreweryFilterChange(filter: BreweryFilter) {
        val newFilter = getFilter(filter.value)
        selectedFilter.value = if (newFilter == selectedFilter.value) {
            BreweryFilter.NONE
        } else {
            newFilter
        }
        val newState = BreweryState.Loading
        newState.selectedFilter = newFilter
        setState(BreweryState.FilterChanged(savedBreweries, newFilter))
        page = 1
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5000L
        private const val LOADING_DELAY = 1000L
    }
}
