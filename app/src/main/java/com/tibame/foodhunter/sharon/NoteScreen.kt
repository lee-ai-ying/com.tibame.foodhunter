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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.sharon.components.card.NoteOrGroupCard
import com.tibame.foodhunter.sharon.viewmodel.NoteVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavHostController,
    noteVM: NoteVM = viewModel()
) {
    // 在畫面開啟時載入資料
    LaunchedEffect(Unit) {
        Log.d("NoteScreen", "LaunchedEffect 觸發載入資料")
        noteVM.loadNotes()
    }

    val notes by noteVM.filteredNotes.collectAsStateWithLifecycle(
        initialValue = emptyList()
    ).also {
        Log.d("NoteScreen", "收到筆記資料更新，數量: ${it.value.size}")
    }

    val isLoading by noteVM.isLoading.collectAsStateWithLifecycle(initialValue = false)
        .also {
            Log.d("NoteScreen", "載入狀態更新: ${it.value}")
        }

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
                // 你要的型別傳遞 Log 加在這裡
                notes.forEach { note ->
                    Log.d("NoteScreen", """
            筆記資料檢查:
            - id: ${note.noteId}
            - type: ${note.type} (${note.type.javaClass})
            - date: ${note.date} (${note.date.javaClass})
            - day: ${note.day} (${note.day.javaClass})
            - title: ${note.title} (${note.title.javaClass})
            - noteContent: ${note.noteContent} (${note.noteContent.javaClass})
            - imageResId: ${note.imageResId} (${note.imageResId?.javaClass})
        """.trimIndent())
                }
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
                    Text("目前沒有筆記")
                }
            }
            else -> {
                Log.d("NoteScreen", "開始顯示筆記列表，數量: ${notes.size}")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        count = notes.size,
                        key = { index -> notes[index].noteId }
                    ) { index ->
                        val currentNote = notes[index]
                        Log.d("NoteScreen", "渲染筆記項目: index=$index, id=${currentNote.noteId}, title=${currentNote.title}")
                        NoteOrGroupCard(
                            onClick = {
                                Log.d("NoteScreen", "點擊筆記: id=${currentNote.noteId}")
                                navController.navigate("note_edit/${currentNote.noteId}")
                            },
                            type = currentNote.type,
                            date = currentNote.date,
                            day = currentNote.day,
                            title = currentNote.title,
                            noteContent = currentNote.noteContent,
                            imageResId = currentNote.imageResId,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
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
    NoteScreen(navController = mockNavController)
}



