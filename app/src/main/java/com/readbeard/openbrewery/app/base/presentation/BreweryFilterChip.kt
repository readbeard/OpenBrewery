package com.readbeard.openbrewery.app.base.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.readbeard.openbrewery.app.beerlist.data.model.BreweryFilter

@Composable
fun BreweryFilterChip(
    filter: BreweryFilter,
    isSelected: Boolean = false,
    onSelectedFilterChanged: (BreweryFilter) -> Unit,
    onExecuteSearch: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color.LightGray else MaterialTheme.colors.secondary
    ) {
        Row(
            modifier = Modifier.toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectedFilterChanged(filter)
                    onExecuteSearch()
                }
            )
        ) {
            Text(
                text = filter.name.lowercase(),
                style = MaterialTheme.typography.body2,
                color = Color.Black,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
