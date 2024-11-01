package com.tibame.foodhunter.sharon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.card.CardContentType
import com.tibame.foodhunter.sharon.components.card.NoteOrGroupCard
import com.tibame.foodhunter.sharon.data.noteList

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    val mockNavController = rememberNavController()
    NoteScreen(navController = mockNavController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavHostController, // 創建或接收 NavController，用於控制導航

) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            NoteOrGroupCard(
                onClick = {},
                type = CardContentType.NOTE,
                date = "10/17",
                day = "星期四",
                title = "小巷中的咖啡廳",
                noteContent = "這裡有各種美味的咖啡和小吃、環境乾淨...",
                imageResId = R.drawable.sushi_image_1
            )
        }

        item {
            NoteOrGroupCard(
                onClick = {},
                type = CardContentType.NOTE,
                date = "10/17",
                day = "星期四",
                title = "小巷中的咖啡廳",
                noteContent = "這裡有各種美味的咖啡和小吃、環境乾淨...",
                imageResId = R.drawable.sushi_image_1
            )
        }

    }
}



