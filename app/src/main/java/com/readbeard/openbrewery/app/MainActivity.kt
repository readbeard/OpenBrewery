package com.readbeard.openbrewery.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.readbeard.openbrewery.app.beerlist.presentation.BreweryScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    @InternalCoroutinesApi
    @ExperimentalFoundationApi
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BreweryScreen()
        }
    }
}
