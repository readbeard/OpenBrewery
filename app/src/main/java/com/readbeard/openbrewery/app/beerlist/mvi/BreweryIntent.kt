package com.readbeard.openbrewery.app.beerlist.mvi

sealed class BreweryIntent {
    object OnStart : BreweryIntent()

    data class OnSearchChanged(
        val searchTerm: String
    ) : BreweryIntent()
}
