package com.tibame.foodhunter

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.tibame.foodhunter.global.*
import com.tibame.foodhunter.ai_ying.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Main()
            }
        }
    }
}

/** 將 **不顯示** TopBar的route寫進list裡 */
@Composable
fun checkTopBarNoShow(destination: NavDestination?): Boolean {
    val context = LocalContext.current
    return !listOf(
        ""
    ).contains(destination?.route)
}

/** 將 **要顯示** BackButton的route寫進list裡 */
@Composable
fun checkTopBarBackButtonShow(destination: NavDestination?): Boolean {
    val context = LocalContext.current
    return listOf(
        context.getString(R.string.str_group) + "/2",
    ).contains(destination?.route)
}

/** 將 **要顯示** BottomBar的route寫進list裡 */
@Composable
fun checkBottomButtonShow(destination: NavDestination?): Boolean {
    val context = LocalContext.current
    return listOf(
        context.getString(R.string.str_home),
        context.getString(R.string.str_search),
        context.getString(R.string.str_post),
        context.getString(R.string.str_group),
        context.getString(R.string.str_member)
    ).contains(destination?.route)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var currectScene by remember { mutableStateOf(context.getString(R.string.str_home)) }
    val destination = navController.currentBackStackEntryAsState().value?.destination

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (checkTopBarNoShow(destination)) {
                    TopFunctionBar(checkTopBarBackButtonShow(destination), navController, scrollBehavior)
                }
            },
            bottomBar = {
                if (checkBottomButtonShow(destination)) {
                    BottomFunctionBar(
                        onHomeClick = {
                            currectScene = context.getString(R.string.str_home)
                        },
                        onSearchClick = {
                            currectScene = context.getString(R.string.str_search)
                        },
                        onPostClick = {
                            currectScene = context.getString(R.string.str_post)
                        },
                        onGroupClick = {
                            currectScene = context.getString(R.string.str_group)
                        },
                        onMemberClick = {
                            currectScene = context.getString(R.string.str_member)
                        },
                        selectScene = currectScene
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.LightGray),
                navController = navController,
                startDestination = currectScene
            ) {
                composable(context.getString(R.string.str_home)) {
                    Text(text = destination?.route.toString())
                }










                composable(context.getString(R.string.str_search)) {
                    Text(text = destination?.route.toString())
                }










                composable(context.getString(R.string.str_post)) {
                    Text(text = destination?.route.toString())
                }










                composable(context.getString(R.string.str_group)) {
                    GroupMain()
                }
                composable(context.getString(R.string.str_group) + "/2") {

                }










                composable(context.getString(R.string.str_member)) {
                    Text(text = destination?.route.toString())
                }

            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun FoodHunterPreview() {
    MaterialTheme {
        Main()
    }
}