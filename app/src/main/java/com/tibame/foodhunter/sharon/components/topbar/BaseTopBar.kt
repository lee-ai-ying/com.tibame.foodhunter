package com.tibame.foodhunter.sharon.components.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.sharon.viewmodel.CalendarViewModel
import com.tibame.foodhunter.sharon.viewmodel.NoteViewModel
import com.tibame.foodhunter.ui.theme.FColor


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBarPreview(){
    val mockNavController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

//    BaseTopBar(mockNavController,scrollBehavior)
    CalendarTopBar(mockNavController,scrollBehavior, CalendarViewModel())
//    NoteTopBar(mockNavController,scrollBehavior, NoteViewModel())

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    isSearchVisible: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onToggleSearchVisibility: () -> Unit,
    showFilter: Boolean,
    onFilter: () -> Unit = {},
) {
    TopAppBar(
        title = {
            if (isSearchVisible) {
                // 當搜尋狀態時，使用文字輸入框作為標題
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    placeholder = { Text("搜尋", color = FColor.Gary, fontSize = 16.sp) },
                    active = isSearchVisible,
                    onActiveChange = { onToggleSearchVisibility() },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                // 正常情況下的標題
                Text(stringResource(R.string.str_back))
            }
        },
        navigationIcon = {
            if (isSearchVisible) {
                // 搜尋狀態下顯示的返回按鈕，用於取消搜尋
                IconButton(onClick = { onToggleSearchVisibility() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "取消搜尋"
                    )
                }
            } else {
                // 正常狀態下顯示的返回按鈕
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "返回"
                    )
                }
            }
        },
        actions = {
            if (!isSearchVisible) {
                // 非搜尋狀態、允許顯示篩選時，顯示搜尋按鈕和篩選按鈕
                IconButton(onClick = onToggleSearchVisibility) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                if(showFilter) {

                    IconButton(onClick = onFilter) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "Filter")
                    }
                }

            } else {
                // 搜尋狀態允許顯示篩選時，顯示篩選按鈕
                if (showFilter) {
                    IconButton(onClick = onFilter) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "Filter")
                    }
                }
            }
            // 如果是搜尋狀態，不顯示其他按鈕，因為搜尋框已佔據標題位置
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
    val isSearchVisible by calendarViewModel.isSearchVisible.collectAsState()
    val searchQuery by calendarViewModel.searchQuery.collectAsState()
    val isFilterChipVisible by calendarViewModel.isFilterChipVisible.collectAsState()
    val selectedFilters by calendarViewModel.selectedFilters.collectAsState()

    Column{
        BaseTopBar(
            navController = navController,
            scrollBehavior = scrollBehavior,
            // 傳遞搜尋相關參數
            isSearchVisible = isSearchVisible,
            searchQuery = searchQuery,
            onSearchQueryChange = { newQuery ->
                // 當搜尋文字改變時更新 ViewModel
                calendarViewModel.updateSearchQuery(newQuery)
            },
            onToggleSearchVisibility = {
                // 切換搜尋框顯示狀態
                calendarViewModel.toggleSearchVisibility()
            },
            showFilter = true,
            onFilter = {
                calendarViewModel.toggleFilterChipVisibility()
            }
        )
        // 新增：顯示篩選 Chip
        if (isFilterChipVisible) {
            FilterChipSection(
                selectedFilters = selectedFilters,
                onFilterSelected = { filter ->
                    calendarViewModel.updateSelectedFilter(filter)
                }
            )
        }
    }
}


@Composable
fun FilterChipSection(
    selectedFilters: List<String>,
    onFilterSelected: (String) -> Unit
) {
    // 新增：顯示包含 Chip 的圓角底圖區域
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        ),
        color = Color.White,

    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            val filters = listOf("揪團", "手札")
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilters.contains(filter),
                    onClick = { onFilterSelected(filter) },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FColor.Orange_5th,
                    )
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    noteViewModel: NoteViewModel,
) {
    // 使用 by 委派來收集狀態
    val isSearchVisible by noteViewModel.isSearchVisible.collectAsState()
    val searchQuery by noteViewModel.searchQuery.collectAsState()

    Column {
        BaseTopBar(
            navController = navController,
            scrollBehavior = scrollBehavior,
            isSearchVisible = isSearchVisible,
            searchQuery = searchQuery,
            onSearchQueryChange = { newQuery ->
                noteViewModel.updateSearchQuery(newQuery)
            },
            onToggleSearchVisibility = {
                noteViewModel.toggleSearchVisibility()
            },
            showFilter = false,
            onFilter = {
                noteViewModel.toggleFilterChipVisibility()
            }
        )

//        if (isFilterChipVisible) {
//            FilterChipSection(
//                selectedFilters = selectedFilters,
//                onFilterSelected = { filter ->
//                    noteViewModel.updateSelectedFilter(filter)
//                }
//            )
//        }
    }
}