package com.tibame.foodhunter.zoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.CalendarScreen
import com.tibame.foodhunter.sharon.FavoriteScreen
import com.tibame.foodhunter.sharon.NiaTab
import com.tibame.foodhunter.sharon.NiaTabRow
import com.tibame.foodhunter.sharon.NoteScreen
import com.tibame.foodhunter.sharon.TabBarComponent
import com.tibame.foodhunter.ui.theme.FoodHunterTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavHostController,
    postViewModel: PostViewModel = viewModel()
) {
    val selectedFilters by postViewModel.selectedFilters.collectAsState()
    val selectedTabIndex by postViewModel.selectedTabIndex.collectAsState()
    val filteredPosts by postViewModel.getFilteredPosts().collectAsState()
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) }

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
        TabBarComponent(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        // 根據選中的 Tab 顯示對應的頁面
        when (selectedTab) {
            0 -> CalendarScreen(navController) {}
            1 -> SearchPost(navController)
            2 -> FavoriteScreen(navController)
        }

        // 使用 FilterChips 函數
        FilterChips(
            filters = listOf("早午餐", "午餐", "晚餐"),
            selectedFilters = selectedFilters,
            onFilterChange = { updatedFilters -> selectedFilters = updatedFilters }
        )

        // 顯示過濾後的貼文列表
        PostList(posts = filteredPosts)

    }
}


@Composable
fun TabBarComponent(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabList = listOf(
        stringResource(id = R.string.str_calendar),
        stringResource(id = R.string.str_note),
        stringResource(id = R.string.str_favorite)
    )

    NiaTabRow(selectedTabIndex = selectedTab) {
        tabList.forEachIndexed { index, title ->
            NiaTab(
                text = { Text(text = title) },
                selected = selectedTab == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

// 示例數據
fun getSamplePosts(): List<Post> {
    // 示例 Publisher 資料
    val alice = Publisher(
        id = "1",
        name = "Alice",
        avatarImage = R.drawable.user1,
        joinDate = "2023-01-01",
        followers = emptyList(),
        following = emptyList()
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // 设置卡片背景色为白色
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            PostHeader(post = post)

            Spacer(modifier = Modifier.height(8.dp))

            ImageDisplay(imageSource = ImageSource.CarouselSource(post.carouselItems))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FavoriteIcon()

                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chat_bubble_outline_24),
                            contentDescription = "Chat Bubble",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                    contentDescription = "Bookmark",
                    modifier = Modifier.size(22.dp)
                )
            }

            PostContent(content = post.content)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
        ) {
            MessageSheet(post = post)
        }
    }
    }


@Composable
private fun PostHeader(post: Post) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = post.publisher.avatarImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
        )

        Column {
            Text(
                text = post.publisher.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = post.location,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ),



@Preview(showBackground = true)
@Composable
fun HomePreview() {
    FoodHunterTheme  {
       Home(rememberNavController())
    }
}
