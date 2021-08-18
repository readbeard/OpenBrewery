package com.readbeard.openbrewery.app.beerlist.mvi

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.model.BreweryFilter

sealed class BreweryState(
    open var breweries: List<Brewery> = ArrayList(),
    open var selectedFilter: BreweryFilter = BreweryFilter.NONE,
    open var page: Int = 2,
    open val searchTerm: String = ""
) {
    open fun copy(): BreweryState { return this }

    data class Loaded(
        override var breweries: List<Brewery>,
        override var selectedFilter: BreweryFilter,
        override var page: Int,
        override var searchTerm: String,
        val reachedEnd: Boolean = false
    ) : BreweryState(breweries, selectedFilter, page, searchTerm) {
        override fun copy(): Loaded {
            return Loaded(breweries, selectedFilter, page, searchTerm)
        }
    }

    object Error : BreweryState()
    object Loading : BreweryState()
}
