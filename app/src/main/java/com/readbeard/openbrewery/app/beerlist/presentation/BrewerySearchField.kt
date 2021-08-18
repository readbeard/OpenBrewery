package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryIntent
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.Locale

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun BrewerySearchField() {
    val viewModel: BreweryViewModel = viewModel()
    val uiState by viewModel.state
    var searchTerm by rememberSaveable { mutableStateOf("") }

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
            onDone = { viewModel.onIntent(BreweryIntent.OnSearchChanged(uiState.selectedFilter, searchTerm)) }
        )
    )
}
