@file:OptIn(ExperimentalMaterial3Api::class)

package com.tibame.foodhunter.wei

import com.tibame.foodhunter.R

import android.content.Context
import android.hardware.camera2.params.BlackLevelPattern
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.ui.theme.FoodHunterTheme

import com.tibame.foodhunter.global.*
import com.tibame.foodhunter.ai_ying.*
import kotlinx.coroutines.launch


//@Composable
//fun checkBottomButtonShow(destination: NavDestination?):Boolean{
//    val context = LocalContext.current
//    return !listOf(
//        context.getString(R.string.str_home),
//        context.getString(R.string.str_search),
//        context.getString(R.string.str_post),
//        context.getString(R.string.str_group),
//        context.getString(R.string.str_member)
//        ).contains(destination?.route)
//}
//@Composable
//fun checkBackButtonShow(destination: NavDestination?):Boolean{
//    val context = LocalContext.current
//    return !listOf(
//        context.getString(R.string.str_home),
//        context.getString(R.string.str_search),
//        context.getString(R.string.str_post),
//        context.getString(R.string.str_group),
//        context.getString(R.string.str_member)
//    ).contains(destination?.route)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetail(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    var mainSceneName by remember { mutableStateOf(context.getString(R.string.str_searchdetail)) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val destination = navController.currentBackStackEntryAsState().value?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .weight(1f),
            topBar = {}
        ){innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color.White)
            ){
                Column(
                    modifier = Modifier
                        .background(Color.White)
                ) {
                    NavHost(    //裡面放lazyColumn
                        navController = navController,
                        // 設定起始頁面
                        startDestination = mainSceneName,
                        modifier = Modifier.weight(1f)
                    ) {
                        composable(
                            route = mainSceneName

                        ) {
                            HorizontalDivider(
                                modifier = Modifier,
                                thickness = 3.dp,
                                Color(0xFFFE724C)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(20.dp),

                                verticalArrangement = Arrangement.spacedBy(15.dp),
                            ) {
                                Spacer(modifier = Modifier)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.mapimage),
                                        contentDescription = "餐廳照片",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .border(
                                                BorderStroke(3.dp, Color(0xFF000000)),
                                                RoundedCornerShape(12)
                                            )
                                            .clip(RoundedCornerShape(12)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.weight(0.1f))
                                    Column(
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        Text(
                                            text = "餐廳名稱",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold, // 字體樣式: 粗體
                                            color = Color.Black // 字的顏色: 藍色
                                        )

                                        Text(
                                            text = "餐廳描述1",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )

                                        Text(
                                            text = "餐廳描述2",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )


                                    }

                                    Spacer(modifier = Modifier.weight(1f)) // 使用Spacer將剩餘空間分配給它

                                    // 將兩個Icon放在Row的右側
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        //加入收藏
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    // 呼叫showSnackbar()會改變SnackbarHostState狀態並顯示Snackbar
                                                    snackbarHostState.showSnackbar(
                                                        "成功加入收藏！",
                                                        // 建議加上取消按鈕
                                                        withDismissAction = true,
                                                        // 不設定duration，預設為Short(停留短暫並自動消失)
                                                    )
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                // 設定按鈕容器顏色
                                                containerColor = Color.Transparent,
                                                // 設定按鈕內容顏色
                                                contentColor = Color.Black
                                            )
                                        ) {
                                            Icon(
                                                // 使用指定圖檔資源
                                                painter = painterResource(id = R.drawable.bookmark),
                                                contentDescription = "bookmark",
                                                modifier = Modifier.size(30.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))

                                        }

                                        //更多功能

                                        Button(
                                            onClick = {
                                                /** 還沒寫 */
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                // 設定按鈕容器顏色
                                                containerColor = Color.Transparent,
                                                // 設定按鈕內容顏色
                                                contentColor = Color.Black
                                            )
                                        ) {
                                            Icon(
                                                // 使用指定圖檔資源
                                                painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                                                contentDescription = "bookmark",
                                                modifier = Modifier.size(30.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        HorizontalDivider(
                                            modifier = Modifier,
                                            thickness = 1.5.dp,
                                            Color(0xFFFE724C)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}






//@Preview(showBackground = true)
//@Composable
//fun RestaurantDetailPreview() {
//    RestaurantDetail(navController = rememberNavController())
//}