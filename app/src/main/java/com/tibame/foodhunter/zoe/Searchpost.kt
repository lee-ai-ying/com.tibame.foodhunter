package com.tibame.foodhunter.zoe

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPost(
    navController: NavHostController,
  postViewModel: PostViewModel = viewModel()

) {
    val selectedFilters by postViewModel.selectedFilters.collectAsState()
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
                // 这里会获取到被点击的帖子 ID
                postViewModel.setPostId(postId)
                // 使用获取到的 ID 进行导航
                navController.navigate("postDetail/$postId")
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
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(filters) { filter ->
            val isSelected = selectedFilters.contains(filter)

            // 使用 FilterChip 或 Chip 來表示篩選標籤
            FilterChip(
                selected = isSelected,
                onClick = {
                    val updatedFilters = if (isSelected) {
                        selectedFilters - filter
                    } else {
                        selectedFilters + filter
                    }
                    onFilterChange(updatedFilters)
                },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    selectedContainerColor = (colorResource(R.color.orange_5th)),
                                    )
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
