package com.tibame.foodhunter.zoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
        NiaTabRow(
            selectedTabIndex = selectedTabIndex,
        ) {
            NiaTab(
                selected = selectedTabIndex == 0,
                onClick = {
                    postViewModel.updateTabIndex(0)
                    navController.navigate(context.getString(R.string.str_home))
                },
                text = { Text(text = stringResource(id = R.string.recommend)) }
            )
            NiaTab(
                selected = selectedTabIndex == 1,
                onClick = {
                    postViewModel.updateTabIndex(1)
                    navController.navigate(context.getString(R.string.str_searchpost))
                },
                text = { Text(text = stringResource(id = R.string.search)) }
            )
        }

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

@Preview(showBackground = true)
@Composable
fun SerchpostPreview() {
    FoodHunterTheme  {
        SearchPost(rememberNavController())
    }
}
