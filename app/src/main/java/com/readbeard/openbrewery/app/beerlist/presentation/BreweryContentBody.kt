package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryIntent
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryState
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@InternalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun BreweryContentBody(
    viewModel: BreweryViewModel,
    breweries: List<Brewery>,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.state
    val state = rememberLazyListState()

    LazyColumn(
        state = state,
        contentPadding = PaddingValues(
            4.dp
        ),
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        itemsIndexed(breweries) { index, brewery ->
            BreweryCard(
                brewery,
                modifier = Modifier.padding(4.dp)
            )

            val showLoadingSpinner = breweries.lastIndex == index && !(uiState as BreweryState.Loaded).reachedEnd
            AnimatedVisibility(visible = showLoadingSpinner) {
                BreweryLoadingBody(toEndOfList = true)
            }
        }
    }

    state.OnBottomReached {
        viewModel.onIntent(BreweryIntent.OnScrolledDown(uiState.selectedFilter, uiState.searchTerm))
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = shouldLoadMore()

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) {
                    loadMore()
                }
            }
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Composable
fun LazyListState.shouldLoadMore(): State<Boolean> {
    return remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            return@derivedStateOf lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }
}
