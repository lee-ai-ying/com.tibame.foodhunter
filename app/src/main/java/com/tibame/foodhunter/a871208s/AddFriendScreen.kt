package com.tibame.foodhunter.a871208s

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun AddFriendScreen(navController: NavHostController = rememberNavController()){
    val context = LocalContext.current
    var id by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
    ) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(16.dp, 2.dp)
    ) {
        Text(
            text = "請輸入好友帳號",
            fontSize = 16.sp,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.size(2.dp))
        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            placeholder = { Text(text = "請輸入帳號", fontSize = 18.sp) },
            singleLine = true,
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth(),
        )
    }




    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .size(120.dp, 60.dp)
                .padding(8.dp),
            onClick = {  }

        ) {
            Text(text = "新增")
        }
    }


}
}

@Preview(showBackground = true)
@Composable
fun AddFriendScreenPreview() {
    MaterialTheme {
        AddFriendScreen()
    }

}