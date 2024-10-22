package com.tibame.foodhunter.ai_ying

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun GroupChatRoom(
    navController: NavHostController = rememberNavController()
) {
    Text(navController.currentBackStackEntryAsState().value?.destination?.route.toString())
}

@Preview(showBackground = true)
@Composable
fun GroupChatRoomPreview() {
    MaterialTheme {
        GroupChatRoom()
    }
}