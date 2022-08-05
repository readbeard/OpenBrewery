package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryState
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalAnimationApi
@InternalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun BreweryScreen(
    viewModel: BreweryViewModel = viewModel()
) {
    val state by viewModel.state

    Scaffold(
        topBar = {
            BreweryTopBar()
        }
    ) { padding ->
        when (state) {
            is BreweryState.Loading -> {
                BreweryLoadingBody(toEndOfList = false)
            }
            is BreweryState.Error -> {
                BreweryErrorBody()
            }
            is BreweryState.Loaded -> {
                BreweryContentBody(viewModel, (state as BreweryState.Loaded).breweries, padding)
            }
            is BreweryState.FilterChanged -> {
                BreweryContentBody(viewModel, (state as BreweryState.FilterChanged).breweries, padding)
            }
        }
    }
}
