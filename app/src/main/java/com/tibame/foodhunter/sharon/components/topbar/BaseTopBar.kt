package com.tibame.foodhunter.sharon.components.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.viewmodel.CalendarViewModel
import com.tibame.foodhunter.sharon.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    onSearch: () -> Unit = {},
    onFilter: () -> Unit = {},
) {
    TopAppBar(
        title = {Text(stringResource(R.string.str_back))},
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft,"Back")
            }
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search,"Search")
            }
            IconButton(onClick = onFilter) {
                Icon(Icons.Outlined.MoreVert,"Filter")
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    calendarViewModel: CalendarViewModel
) {
    BaseTopBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        onSearch = {
            calendarViewModel.handleSearch("")
        },
        onFilter = {
            calendarViewModel.handleFilter(listOf())
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    noteViewModel: NoteViewModel
) {
    BaseTopBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        onSearch = {
            noteViewModel.handleSearch("")
        },
        onFilter = {
            noteViewModel.handleFilter(listOf())
        },
    )
}