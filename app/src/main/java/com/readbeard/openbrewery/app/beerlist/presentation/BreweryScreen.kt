package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readbeard.openbrewery.app.base.presentation.BreweryFilterChip
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.data.model.getAllFilters
import com.readbeard.openbrewery.app.beerlist.data.model.getFilter
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryIntent
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryState
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.Locale

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
            BreweryTopBar {
                viewModel.onIntent(BreweryIntent.OnSearchChanged(state.selectedFilter, it))
            }
        }
    ) {
        when (state) {
            is BreweryState.Loading -> {
                BreweryLoadingBody()
            }
            is BreweryState.Error -> {
                BreweryErrorBody()
            }
            is BreweryState.Loaded -> {
                BreweryContentBody(viewModel, (state as BreweryState.Loaded).breweries)
            }
        }
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
@Preview
@Composable
fun BreweryTopBar(
    modifier: Modifier = Modifier,
    onSearchTextChanged: (searchText: String) -> Unit = {},
) {
    val viewModel: BreweryViewModel = viewModel()
    val uiState by viewModel.state

    var searchTerm by rememberSaveable { mutableStateOf("") }
    Surface(
        color = MaterialTheme.colors.primary,
        elevation = 8.dp,
        modifier = modifier,
    ) {
        Column {
            TextField(
                value = searchTerm,
                placeholder = { Text("Search by ${uiState.selectedFilter.name.lowercase(Locale.getDefault())}") },
                singleLine = true,
                modifier = Modifier
                    .testTag("searchBar")
                    .padding(8.dp)
                    .fillMaxWidth(),
                onValueChange = {
                    searchTerm = it
                },
                keyboardActions = KeyboardActions(
                    onDone = { onSearchTextChanged(searchTerm) }
                )
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                itemsIndexed(getAllFilters()) { _, item ->
                    BreweryFilterChip(
                        filter = item,
                        isSelected = uiState.selectedFilter == getFilter(item.value),
                        onSelectedFilterChanged = {
                            viewModel.onIntent(BreweryIntent.OnFilterSelected(item, searchTerm))
                        },
                        onExecuteSearch = { Timber.d("Clicked on ${getFilter(item.value).value}") }
                    )
                }
            }
        }
    }
}

@Composable
fun BreweryLoadingBody(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.testTag("spinner")
        )
    }
}

@Composable
fun BreweryErrorBody(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Error downloading breweries")
    }
}

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
                BrewerySpinner(modifier = Modifier.padding(4.dp))
            }
        }
    }

    state.OnBottomReached {
        viewModel.onIntent(BreweryIntent.OnScrolledDown(uiState.selectedFilter, uiState.searchTerm))
    }
}

@Composable
fun BreweryCard(brewery: Brewery, modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                brewery.name!!,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
            )
            Text(
                brewery.city!!,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun BrewerySpinner(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        CircularProgressIndicator(
            modifier = Modifier.testTag("spinner")
        )
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
