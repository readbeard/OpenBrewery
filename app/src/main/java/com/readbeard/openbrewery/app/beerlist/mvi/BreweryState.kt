package com.readbeard.openbrewery.app.beerlist.mvi

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.model.BreweryFilter

sealed class BreweryState(open var selectedFilter: BreweryFilter, open var page: Int, open var searchTerm: String) {
    data class Loading(
        override var selectedFilter: BreweryFilter,
        override var page: Int,
        override var searchTerm: String
    ) : BreweryState(selectedFilter, page, searchTerm)

    data class Loaded(
        val breweries: List<Brewery>,
        override var selectedFilter: BreweryFilter,
        override var page: Int,
        override var searchTerm: String
    ) : BreweryState(selectedFilter, page, searchTerm)

    data class ErrorLoadingPage(
        val breweries: List<Brewery>,
        override var selectedFilter: BreweryFilter,
        override var page: Int,
        override var searchTerm: String
    ) : BreweryState(selectedFilter, page, searchTerm)

    data class Error(
        override var selectedFilter: BreweryFilter,
        override var page: Int,
        override var searchTerm: String
    ) : BreweryState(selectedFilter, page, searchTerm)
}
