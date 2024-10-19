package com.tibame.foodhunter.ai_ying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.Main
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme


@Composable
fun Group(
    navController: NavHostController,
    groupNavController: NavHostController = rememberNavController()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController = groupNavController,
            // 設定起始頁面
            startDestination = "group1",
            modifier = Modifier.weight(1f)
        ) {
            composable(
                route = "group1"
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(Color.Cyan)
                ) {
                    Group1(groupNavController)
                }
            }
            composable(
                route = "group2"
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(Color.Cyan)
                ) {
                    Group2(groupNavController)
                }
            }

        }
    }
}

@Composable
fun Group1(groupNavController: NavHostController = rememberNavController()) {
    Text(text = "Group1")
    Button(
        onClick = {
            groupNavController.navigate("group2")
        }
    ) {
        Text( text="button")
    }
}

@Composable
fun Group2(groupNavController: NavHostController = rememberNavController()) {

    Text(text = "Group2")

}