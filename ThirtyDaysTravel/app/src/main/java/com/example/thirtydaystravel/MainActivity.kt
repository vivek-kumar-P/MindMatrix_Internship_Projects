package com.example.thirtydaystravel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.thirtydaystravel.model.Destination
import com.example.thirtydaystravel.model.DestinationsRepository
import com.example.thirtydaystravel.ui.theme.ThirtyDaysTravelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThirtyDaysTravelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThirtyDaysApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirtyDaysApp() {
    Scaffold(
        topBar = {
            TravelTopAppBar()
        }
    ) { paddingValues ->
        DestinationList(
            destinations = DestinationsRepository.destinations,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayMedium
            )
        },
        modifier = modifier
    )
}

@Composable
fun DestinationList(
    destinations: List<Destination>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(destinations) { destination ->
            DestinationCard(
                destination = destination,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun DestinationCard(
    destination: Destination,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier,
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.day, destination.day),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = stringResource(destination.titleRes),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                DestinationItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
            DestinationImage(
                imageRes = destination.imageRes,
                contentDescription = destination.titleRes
            )
            if (expanded) {
                DestinationDescription(
                    descriptionRes = destination.descriptionRes,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                )
            }
        }
    }
}

@Composable
fun DestinationItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expanded) {
                stringResource(R.string.show_less)
            } else {
                stringResource(R.string.show_more)
            },
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun DestinationImage(
    @DrawableRes imageRes: Int,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        painter = painterResource(id = imageRes),
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(id = contentDescription)
    )
}

@Composable
fun DestinationDescription(
    @StringRes descriptionRes: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = descriptionRes),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}
