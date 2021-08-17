package com.readbeard.openbrewery.app.beerlist.mvi

import com.readbeard.openbrewery.app.beerlist.data.model.BreweryFilter

sealed class BreweryIntent {
    object OnStart : BreweryIntent()

    data class OnSearchChanged(
        override val filter: BreweryFilter,
        override val searchTerm: String
    ) : OnFilterSelected(filter, searchTerm)

    data class OnScrolledDown(override val filter: BreweryFilter, override val searchTerm: String) :
        OnFilterSelected(filter, searchTerm)

    open class OnFilterSelected(open val filter: BreweryFilter, open val searchTerm: String) :
        BreweryIntent()
}
