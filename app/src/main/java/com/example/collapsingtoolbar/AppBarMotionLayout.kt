package com.example.collapsingtoolbar

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMotionApi::class)
@Composable

/*
 * In this composable we are using constraint sets defined in motion_scene.json5 file
 * and and here we are using Modifier.layoutId() to assign the ids of view that we have defiend in
 * motion_scene file so that motion layout can identify and position them according to the constraint set
 *
 */

fun ProfileHeader(progress: MutableState<Float>) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var searchbarTxt by remember{ mutableStateOf("") }
    var selectedCategoryIndex by remember{ mutableStateOf(0) }
    val isSearchbarFoucused = remember{ mutableStateOf(false) }

    val motionString = remember {
        context.resources.openRawResource(R.raw.motion_scene)
            .readBytes()
            .decodeToString()
    }

    BackHandler(enabled = isSearchbarFoucused.value) {
        focusManager.clearFocus()
    }

    MotionLayout(
        motionScene = MotionScene(content = motionString),
        progress = progress.value,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId("box")
        )

        Text(
            text = "Smartphone_Codes",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.layoutId("username")
        )

        var hintText by remember{ mutableStateOf("") }
        LaunchedEffect(key1 = true, key2 = isSearchbarFoucused.value ){

            val hintCategories = listOf("Mens Fashion","Women's Fashion","Kid's toys","Households","Jewellery")
            CoroutineScope(Dispatchers.IO).launch {
                var currentHintIndex = 0
                while (!isSearchbarFoucused.value){
                    hintText= hintCategories[currentHintIndex]
                    if(currentHintIndex >= hintCategories.size -1) currentHintIndex = 0 else currentHintIndex++
                    Log.d("Hint_CHANGE","Looping ...")
                    delay(1200L)
                }

            }

        }

        TextField(
            value = searchbarTxt,
            onValueChange = { searchbarTxt = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            modifier= Modifier
                .padding(start = 12.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = TextFieldDefaults.MinHeight)
                .onFocusChanged {
                    isSearchbarFoucused.value = it.isFocused
                }
                .layoutId("searchbar"),
            maxLines = 1,
            placeholder = {
                Row(verticalAlignment = Alignment.CenterVertically){

                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Suggestion",
                        tint = Color.LightGray
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Search $hintText", color = Color.LightGray)
                }
            }
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("category")
        ) {
            items(8) {

                Text(
                    text = "Category $it",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selectedCategoryIndex == it) Brush.linearGradient(
                                listOf(
                                    Color.Blue,
                                    Color(0xFF8000FF)
                                )
                            )
                            else Brush.linearGradient(listOf(Color(0xFFEBEBEB), Color(0xFFEBEBEB)))
                        )
                        .clickable {
                            selectedCategoryIndex = it
                        }
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        ,
                    fontSize = 14.sp,
                    color =  if (selectedCategoryIndex == it) Color.White
                    else Color.Black
                )

            }
        }

    }

}