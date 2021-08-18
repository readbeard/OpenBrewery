package com.readbeard.openbrewery.app.beerlist.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.readbeard.openbrewery.app.beerlist.data.model.Brewery

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
