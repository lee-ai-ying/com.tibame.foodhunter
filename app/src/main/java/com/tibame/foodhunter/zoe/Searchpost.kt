package com.tibame.foodhunter.zoe

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.Main
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.NiaTab
import com.tibame.foodhunter.sharon.NiaTabRow
import com.tibame.foodhunter.ui.theme.FoodHunterTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPost(navController: NavHostController = rememberNavController()) {
    val samplePosts: List<Post> = getSamplePosts()
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }
    var selectedTabIndex by remember { mutableStateOf(1) }
    val context = LocalContext.current

    // 根據選擇的篩選標籤過濾貼文
    val filteredPosts = if (selectedFilters.isEmpty()) {
        samplePosts
    } else {
        samplePosts.filter { post -> selectedFilters.contains(post.postTag) }
    }

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
                    selectedTabIndex = 0
                    navController.navigate(context.getString(R.string.str_home))
                },
                text = { Text(text = stringResource(id = R.string.recommend)) }
            )
            NiaTab(
                selected = selectedTabIndex == 1,
                onClick = {
                    selectedTabIndex = 1
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
            onFilterChange = { updatedFilters -> selectedFilters = updatedFilters }
        )

        ImageList(posts = filteredPosts)
    }
}

@Preview(showBackground = true)
@Composable
fun SerchpostPreview() {
    FoodHunterTheme  {
        SearchPost()
    }
}
