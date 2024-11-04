package com.tibame.foodhunter.sharon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.card.NoteOrGroupCard
import com.tibame.foodhunter.sharon.data.NoteRepository
import com.tibame.foodhunter.sharon.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavHostController,
    noteViewModel: NoteViewModel = viewModel()
) {
    // 使用 collectAsStateWithLifecycle 收集筆記列表
    val notes by noteViewModel.notes.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 使用 items 遍歷筆記列表
        items(notes.size) { index ->
            val currentNote = notes[index]
            NoteOrGroupCard(
                onClick = {
                    // 點擊時導航到編輯頁面
                    navController.navigate("note_edit/${currentNote.noteId}")
                },
                type = currentNote.type,
                date = currentNote.date,
                day = currentNote.day,
                title = currentNote.title,
                noteContent = currentNote.noteContent,
                imageResId = currentNote.imageResId
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    val mockNavController = rememberNavController()
    NoteScreen(navController = mockNavController)
}



