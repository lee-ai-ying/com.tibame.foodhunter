package com.tibame.foodhunter.sharon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    canback:Boolean = true,
    navController: NavHostController? = null,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    ),
    hasTitleInput: Boolean = false,
){
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(stringResource(R.string.str_member))
        },
        navigationIcon = {
            if (canback) {
                IconButton(onClick = {
                    navController?.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.str_back)
                    )
                }
            }
        },
        actions = {
            if (!hasTitleInput) {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.str_notice)
                    )
                }
            } else {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = stringResource(R.string.str_notice)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.str_chat)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}
