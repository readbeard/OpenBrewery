package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.readbeard.openbrewery.app.beerlist.data.model.getAllFilters
import com.readbeard.openbrewery.app.beerlist.data.model.getFilter
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryIntent
import com.readbeard.openbrewery.app.beerlist.mvi.BreweryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun BreweryFilterList() {
    val viewModel: BreweryViewModel = viewModel()
    val uiState by viewModel.state
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
                    viewModel.onIntent(BreweryIntent.OnFilterSelected(item, uiState.searchTerm))
                }
            )
        }
    }
}
