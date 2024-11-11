package com.tibame.foodhunter.sharon.components.topbar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.DeleteConfirmationDialog
import com.tibame.foodhunter.sharon.viewmodel.NoteEditEvent
import com.tibame.foodhunter.sharon.viewmodel.NoteEditVM
import com.tibame.foodhunter.ui.theme.FColor
import perfetto.protos.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditTopBar(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    ),
    noteEditVM: NoteEditVM,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val uiState by noteEditVM.uiState.collectAsState()

    Log.d("NoteEditTopBar", "TopBar UI狀態: $uiState")


    TopAppBar(
        title = {},
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (uiState.hasTitle) {
                IconButton(onClick = {
                    noteEditVM.saveAndNavigateBack(navController)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.str_back)
                    )
                }
            }
        },
        actions = {
            when {
                uiState.isFirstEntry && !uiState.hasTitle ->
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(FColor.Orange_1st),
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.str_note_cancel_add)
                        )
                    }
                // 不是首次進入、有既有筆記
                uiState.hasTitle || uiState.isExistingNote ->
                    IconButton(
                        onClick = {
                            showDeleteDialog = true
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
            }
        }
    )
    // 如果顯示刪除對話框
    if (showDeleteDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)) // 半透明黑色背景
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center // 確保對話框在中間
            ) {
                DeleteConfirmationDialog(
                    onDeleteConfirmed = {
                        showDeleteDialog = false
                        noteEditVM.deleteNote(navController)
                        navController.popBackStack()
                    },
                    onCancel = {
                        showDeleteDialog = false
                    }
                )
            }
        }
    }
}
