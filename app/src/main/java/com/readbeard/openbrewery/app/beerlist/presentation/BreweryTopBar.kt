package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@Preview
@Composable
fun BreweryTopBar(
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.primary,
        elevation = 8.dp,
        modifier = modifier,
    ) {
        Column {
            BrewerySearchField()
            BreweryFilterList()
        }
    }
}
