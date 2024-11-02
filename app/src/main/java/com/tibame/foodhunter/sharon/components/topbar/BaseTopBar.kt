package com.tibame.foodhunter.sharon.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.sharon.viewmodel.CalendarViewModel
import com.tibame.foodhunter.sharon.viewmodel.NoteViewModel
import com.tibame.foodhunter.ui.theme.FColor


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BaseTopBarPre(){
    val mockNavController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BaseTopBar(mockNavController,scrollBehavior)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    isSearchVisible: Boolean = false,
    onSearch: () -> Unit = {},
    onFilter: () -> Unit = {},

) {
    var searchVisible by remember { mutableStateOf(isSearchVisible) } // 新增這行
    var searchQuery by remember { mutableStateOf("") }

    TopAppBar(
        title = { Text(stringResource(R.string.str_back)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back")
            }
        },
        actions = {

            if (searchVisible) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "搜尋",
                            color = FColor.Gary,
                            fontSize = 16.sp
                        )
                    },
                    active = searchVisible,
                    onActiveChange = { searchVisible = it },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                IconButton(
                    onClick = {searchVisible =!searchVisible}
                ) {
                    Icon(Icons.Default.Search, "Search")
                }
                IconButton(onClick = onFilter) {
                    Icon(Icons.Outlined.MoreVert, "Filter")
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = FColor.Dark_80,
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    calendarViewModel: CalendarViewModel,
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
    noteViewModel: NoteViewModel,
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