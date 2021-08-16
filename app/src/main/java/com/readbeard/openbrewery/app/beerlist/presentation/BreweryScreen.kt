package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryIntent
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryState
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

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
                viewModel.onIntent(BreweryIntent.OnSearchChanged(it))
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
                BreweryContentBody((state as BreweryState.Loaded).movies)
            }
        }
    }
}

@Preview
@Composable
fun BreweryTopBar(
    modifier: Modifier = Modifier,
    onSearchTextChanged: (searchText: String) -> Unit = {},
) {
    var searchTerm by rememberSaveable { mutableStateOf("") }

    Surface(
        color = MaterialTheme.colors.primary,
        elevation = 8.dp,
        modifier = modifier,
    ) {
        TextField(
            value = searchTerm,
            placeholder = { Text("Search by name") },
            singleLine = true,
            modifier = Modifier
                .testTag("searchBar")
                .padding(8.dp)
                .fillMaxWidth(),
            onValueChange = {
                searchTerm = it
                onSearchTextChanged(it)
            }
        )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreweryContentBody(
    breweries: List<Brewery>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(
            120.dp
        ),
        contentPadding = PaddingValues(
            4.dp
        ),
        modifier = modifier
    ) {
        items(breweries) { movie ->
            BreweryCard(
                movie,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun BreweryCard(brewery: Brewery, modifier: Modifier) {
    Card(
        modifier = modifier
    ) {
        Column {
            Text(
                brewery.name,
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
                brewery.city,
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