package com.readbeard.openbrewery.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.readbeard.openbrewery.app.beerlist.presentation.BreweryScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BreweryScreen()
        }
    }
}
