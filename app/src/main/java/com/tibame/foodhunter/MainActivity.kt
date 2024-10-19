package com.tibame.foodhunter

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.ui.theme.FoodHunterTheme

import com.tibame.foodhunter.global.*
import com.tibame.foodhunter.ai_ying.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodHunterTheme {
                Main()
            }
        }
    }
}

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
fun Main(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var mainSceneName by remember { mutableStateOf(context.getString(R.string.str_home)) }
    val destination = navController.currentBackStackEntryAsState().value?.destination
    var isTopBarShow by remember { mutableStateOf(false) }
    var isbottomBarShow by remember { mutableStateOf(true) }
    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .weight(1f),
            topBar = {
                TopFunctionBar(isTopBarShow,navController)
            }
        ){innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color.LightGray)
            ) {
                NavHost(
                    navController = navController,
                    // 設定起始頁面
                    startDestination = mainSceneName,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(
                        route = mainSceneName
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        ) {
                            when (mainSceneName) {
                                stringResource(R.string.str_home) -> {
                                    Text(text = destination?.route.toString())
                                }

                                stringResource(R.string.str_search) -> {
                                    Text(text = destination?.route.toString())
                                }

                                stringResource(R.string.str_post) -> {
                                    Text(text = destination?.route.toString())
                                }

                                stringResource(R.string.str_group) -> {
                                    Text(text = destination?.route.toString())
                                    Group(navController)
                                }

                                stringResource(R.string.str_member) -> {
                                    Text(text = destination?.route.toString())
                                }
                            }
                        }

                    }
                }
            }
        }
        if (isbottomBarShow) {
            BottomFunctionBar(
                onHomeClick = {
                    mainSceneName = context.getString(R.string.str_home)
                },
                onSearchClick = {
                    mainSceneName = context.getString(R.string.str_search)
                },
                onPostClick = {
                    mainSceneName = context.getString(R.string.str_post)
                },
                onGroupClick = {
                    mainSceneName = context.getString(R.string.str_group)
                },
                onMemberClick = {
                    mainSceneName = context.getString(R.string.str_member)
                },
                selectScene = mainSceneName
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodHunterPreview() {
    FoodHunterTheme {
        Main()
    }
}