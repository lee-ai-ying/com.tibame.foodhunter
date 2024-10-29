package com.tibame.foodhunter.zoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.NiaTab
import com.tibame.foodhunter.sharon.NiaTabRow
import com.tibame.foodhunter.ui.theme.FoodHunterTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPost(
    navController: NavHostController,
                postViewModel: PostViewModel = viewModel()) {
    val selectedFilters by postViewModel.selectedFilters.collectAsState()
    val selectedTabIndex by postViewModel.selectedTabIndex.collectAsState()
    val context = LocalContext.current
    val filteredPosts by postViewModel.getFilteredPosts().collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {

        SearchBar(
            query = "",
            onQueryChange = {},
            placeholder = {
                Text("")
            },
            onSearch = {},
            active = false,
            onActiveChange = {},
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
            },
            trailingIcon = {}
        ) { }

        FilterChips(
            filters = listOf("早午餐", "午餐", "晚餐"),
            selectedFilters = selectedFilters,
            onFilterChange = { updatedFilters ->
                postViewModel.updateFilters(updatedFilters)
            }
        )

        ImageList(
            posts = filteredPosts,  // 你的貼文數據
            onPostClick = { postId ->
                // 當圖片被點擊時，導航到詳情頁面
                navController.navigate("post_detail/$postId")
            }
        )
    }
}

@Composable
fun FilterChips(
    filters: List<String>,                 // 可用的標籤列表
    selectedFilters: List<String>,         // 當前選中的標籤
    onFilterChange: (List<String>) -> Unit // 選中狀態變更時的回調
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        filters.forEach { filter ->
            val isSelected = selectedFilters.contains(filter)

            // 使用 FilterChip 或 Chip 來表示篩選標籤
            FilterChip(
                selected = isSelected,
                onClick = {
                    // 當 chip 被點擊時，更新選擇列表
                    val updatedFilters = if (isSelected) {
                        selectedFilters - filter
                    } else {
                        selectedFilters + filter
                    }
                    onFilterChange(updatedFilters) // 更新回調
                },
                label = { Text(filter) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SerchpostPreview() {
    FoodHunterTheme  {
        SearchPost(rememberNavController())
    }
}
