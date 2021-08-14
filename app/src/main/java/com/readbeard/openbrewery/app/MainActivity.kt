package com.readbeard.openbrewery.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.readbeard.openbrewery.app.databinding.ActivityMainBinding
import com.readbeard.openbrewery.library.FactorialCalculator
import com.readbeard.openbrewery.library.android.NotificationUtil

class MainActivity : AppCompatActivity() {

    private val notificationUtil: NotificationUtil by lazy { NotificationUtil(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContent {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(text = getString(R.string.app_name))
                    },
                    actions = {
                        IconButton(onClick = {
                            Toast.makeText(
                                baseContext,
                                "Clicked on menu",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = null)
                        }
                    }
                )
            }) {
                PrepareBreweryList()
            }
        }

        binding.buttonCompute.setOnClickListener {
            val input = binding.editTextFactorial.text.toString().toInt()
            val result = FactorialCalculator.computeFactorial(input).toString()

            binding.textResult.text = result
            binding.textResult.visibility = View.VISIBLE

            notificationUtil.showNotification(
                context = this,
                title = getString(R.string.notification_title),
                message = result
            )
        }
    }

    @Preview
    @Composable
    fun PrepareBreweryList() {
        val breweryList = mutableListOf<Brewery>().apply {
            repeat(100) {
                this.add(element = Brewery("Brewery #$it", "location of #$it"))
            }
        }
        SetUpBreweryList(breweryList = breweryList)
    }

    @Composable
    fun SetUpBreweryList(breweryList: List<Brewery>) {
        val scrollState = rememberLazyListState()

        LazyColumn(state = scrollState) {
            items(breweryList) { brewery ->
                BreweryCard(brewery = brewery)
            }
        }
    }

    @Composable
    fun BreweryCard(brewery: Brewery) {
        Column(
            Modifier
                .clickable {
                    Toast
                        .makeText(
                            this,
                            "Clicked on ${brewery.name}",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
        ) {
            Text(
                text = brewery.name,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = brewery.location,
                style = MaterialTheme.typography.body2
            )
        }
    }
}
