@file:OptIn(ExperimentalMaterial3Api::class)

package com.tibame.foodhunter.wei


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.ui.theme.FColor
import com.tibame.foodhunter.zoe.PostViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetail(
    navController: NavHostController,
    restaurantVM: SearchScreenVM,
    reviewVM: ReviewVM

) {
    val restNavController = rememberNavController()
    val context = LocalContext.current
    var mainSceneName by remember { mutableStateOf(context.getString(R.string.restaurantDetail)) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val destination = navController.currentBackStackEntryAsState().value?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()
    val postVM: PostViewModel = viewModel()
    val relatedPosts by postVM.restRelatedPosts.collectAsState()
    val restaurant by restaurantVM.choiceOneRest.collectAsState()
    LaunchedEffect(restaurant) {
        postVM.fetchRestRelatedPosts(restaurant?.restaurant_id ?: 7)
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        NavHost(
            navController = restNavController,
            startDestination = mainSceneName,
            modifier = Modifier.weight(1f)
        ) {
            composable(route = mainSceneName) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.5.dp,
                    color = FColor.Orange_1st
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(15.dp)

                ) {
                    Spacer(modifier = Modifier)

                    RestaurantInfoDetail(restaurantVM)


                    RelatedPost(relatedPosts, navController)


                    HorizontalDivider(
                        modifier = Modifier,
                        thickness = 1.5.dp,
                        color = FColor.Orange_1st
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    //評論顯示區
                    Text(
                        text = if (restaurant?.total_review == 0) "尚無評論"
                        else "總評論數(${restaurant?.total_review})",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    ReviewZone(navController = navController)
                }
            }
        }
    }
}

@Composable
fun RestaurantDetailTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp), // 給右邊的叉叉留空間
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.restaurantDetail),
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24), // 使用你的叉叉圖標
                    contentDescription = "Close",
                    tint = Color.DarkGray
                )
            }
        },
    )
}


@Preview(showBackground = true)
@Composable
fun RestaurantDetailPreview() {
    // 提供一個模擬的 NavHostController 和 SearchScreenVM
    val navController = rememberNavController()
    val restaurantVM = SearchScreenVM() // 根據需要替換成模擬或預設的 ViewModel
    val reviewVM = ReviewVM()

    RestaurantDetail(
        navController = navController,
        restaurantVM = restaurantVM,
        reviewVM = reviewVM
    )
}
