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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.ui.theme.FColor

@Composable
fun ReviewDetail(
    navController: NavHostController,
    reviewVM: ReviewVM = remember { ReviewVM() },
    restaurantVM: SearchScreenVM,
    reviewId: Int? = null
) {
    val reviews by reviewVM.reviewState.collectAsState()  // 收集評論列表狀態
    val restaurant by restaurantVM.choiceOneRest.collectAsState()
    val restaurantId by reviewVM.reviewState.collectAsState()

    // 確保從餐廳獲取正確的餐廳ID
    LaunchedEffect(restaurant) {
        restaurant?.restaurant_id?.let { id ->
            reviewVM.loadReviews(id)
        }
    }
    // 如果有特定評論ID，載入該評論
    LaunchedEffect(reviewId) {
        if (reviewId != null && reviewId != 0) {
            reviewVM.loadReviewById(reviewId)
        }
    }

    LaunchedEffect(reviews) {
        Log.d("RestaurantDetail", "Current reviews count: ${reviews.size}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.5.dp,
            color = FColor.Orange_1st
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            //items(1) {
            Spacer(modifier = Modifier.size(8.dp))
            //}

            //items(1) {
                ReviewInfoDetail(reviewVM = reviewVM)
            //}

            //items(1) {
                restaurant?.restaurant_id?.let { restaurantId ->
                    DetailReviewZone(
                        navController = navController,
                        viewModel = reviewVM,
                        restaurantId = restaurantId
                    )
                } ?: run {}
                // 使用假資料
                //DummyReviewList(dummyReviewList)
            //    }

            //items(1) {

                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 1.dp,
                    color = FColor.Orange_1st
                )
            //}

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),// 給右邊的叉叉留空間
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.reviewDetail),
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
fun ReviewDetailScreenPreview() {
    val navController =
        rememberNavController()  // 使用 rememberNavController() 來建立一個模擬的 NavController
    val reviewVM = remember { ReviewVM() }        // 使用模擬的 ReviewVM
    val restaurantVM = remember { SearchScreenVM() } // 使用模擬的 SearchScreenVM

    ReviewDetail(
        navController = navController,
        reviewVM = reviewVM,
        restaurantVM = restaurantVM,
    )
}