package com.tibame.foodhunter.zoe

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
fun Home(
    navController: NavHostController,
    initTab: Int,
    postViewModel: PostViewModel = viewModel()
) {
    val selectedFilters by postViewModel.selectedFilters.collectAsState()
    var selectedTab by remember { mutableIntStateOf(initTab) }

//    val filteredPosts by postViewModel.getFilteredPosts().collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),

        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        PostTabBarComponent(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        // 根據選中的 Tab 顯示對應的頁面
        when (selectedTab) {
            0 -> RecommendedPosts(navController)
            1 -> SearchPost(navController)


        }


    }
}


//
//@Composable
//fun PostList(
//    posts: List<Post>,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(
//        modifier = modifier.fillMaxSize(),

@Composable
fun PostTabBarComponent(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabList = listOf(
        stringResource(id = R.string.str_Recommendedposts),
        stringResource(id = R.string.str_searchpost)
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





@Preview(showBackground = true)
@Composable
fun HomePreview() {
    FoodHunterTheme  {
//       Home(rememberNavController())
    }



}
