package com.tibame.foodhunter.sharon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.sharon.components.card.NoteOrGroupCard
import com.tibame.foodhunter.sharon.viewmodel.NoteVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavHostController,
    noteVM: NoteVM,

) {
    val notes by noteVM.filteredNotes.collectAsStateWithLifecycle()
    val isLoading by noteVM.isLoading.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        when {
            isLoading -> {
                Log.d("NoteScreen", "顯示載入中狀態")
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            notes.isEmpty() -> {
                Log.d("NoteScreen", "顯示空資料狀態")
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val message = if (noteVM.hasSearchQuery) {
                        "找不到符合的筆記"
                    } else {
                        "目前沒有筆記"
                    }
                    Text(message)
                }
            }
            else -> {
                Log.d("NoteScreen", "開始顯示筆記列表，數量: ${notes.size}")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = notes.size,
                        key = { index -> notes[index].noteId }
                    ) { index ->
                        val currentNote = notes[index]
                        Log.d("NoteScreen",
                            "渲染筆記項目: index=$index, " +
                                    "id=${currentNote.noteId}, " +
                                    "title=${currentNote.title}, " +
                                    "content=${currentNote.content}")
                        NoteOrGroupCard(
                            onClick = {
                                Log.d("NoteScreen", "點擊筆記: id=${currentNote.noteId}")
                                navController.navigate("note/edit/${currentNote.noteId}")
                            },
                            type = currentNote.type,
                            date = currentNote.date,
                            day = currentNote.day,
                            title = currentNote.title,
                            content = currentNote.content,
                            imageResId = currentNote.imageResId,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    val mockNavController = rememberNavController()
    NoteScreen(navController = mockNavController, noteVM = NoteVM())
}



