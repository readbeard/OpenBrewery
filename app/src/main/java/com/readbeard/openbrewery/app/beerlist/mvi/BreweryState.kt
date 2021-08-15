package com.readbeard.openbrewery.app.beerlist.mvi

import com.readbeard.openbrewery.app.beerlist.data.model.Brewery

sealed class BreweryState {
    object Loading : BreweryState()

    data class Loaded(
        val movies: List<Brewery>
    ) : BreweryState()

    object Error : BreweryState()
}
