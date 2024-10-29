package com.tibame.foodhunter.wei

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewiewDetail(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    var mainSceneName by remember { mutableStateOf(context.getString(R.string.str_searchdetail)) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val destination = navController.currentBackStackEntryAsState().value?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    // 回傳CoroutineScope物件以適用於此compose環境
    val scope = rememberCoroutineScope()
    // 控制收藏狀態

    Column(modifier = Modifier.fillMaxSize()) {
//        Scaffold(
//            modifier = Modifier
//                .nestedScroll(scrollBehavior.nestedScrollConnection)
//                .weight(1f),
//            topBar = {},
//            snackbarHost = { SnackbarHost(snackbarHostState) }
//        ) { }
    }
}


@Preview(showBackground = true)
@Composable
fun RewiewDetailPreview() {
    RewiewDetail(navController = rememberNavController())
}