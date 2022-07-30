package com.example.collapsingtoolbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.collapsingtoolbar.ui.theme.CollapsingToolbarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollapsingToolbarTheme {
                window.statusBarColor = Color(0xFF313131).toArgb()
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {

    val myHomeFeedScrollState = rememberLazyListState()
    val toolbarProgress = remember { mutableStateOf(0f) }

    val nestedScrollConnection = object : NestedScrollConnection {

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {

            if (myHomeFeedScrollState.firstVisibleItemIndex == 0) {
                toolbarProgress.value =
                    (myHomeFeedScrollState.firstVisibleItemScrollOffset / 100f).coerceIn(0f, 1f)
            } else {
                toolbarProgress.value = 1f
            }
            return Offset.Zero
        }

    }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            ProductsGrid(myHomeFeedScrollState)
        }
        Column {
            ProfileHeader(progress = toolbarProgress)
        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsGrid(lazyListState: LazyListState) {

    LazyVerticalGrid(
        cells = GridCells.Fixed(2), contentPadding = PaddingValues(top = 180.dp, bottom = 16.dp),
        state = lazyListState
    ) {

        items(30){

            Card(elevation= 4.dp,modifier=Modifier.padding(24.dp), shape= RoundedCornerShape(12.dp)) {
                ProductsCard(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

        }


    }
}
