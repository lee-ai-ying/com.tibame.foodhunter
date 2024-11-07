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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import kotlinx.coroutines.launch

@Composable
fun FriendAddScreen(navController: NavHostController = rememberNavController(),
                    friendVM: FriendViewModel = viewModel()
                    , userVM: UserViewModel){
    val context = LocalContext.current
    var friend by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val username = userVM.username.value
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "無此帳號或已追蹤該好友") },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("確定")
                }
            }
        )
    }
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
            value = friend,
            onValueChange = { friend = it },
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
            onClick = { coroutineScope.launch {
                val add = friendVM.friendAdd(username, friend)
                if (add) {
                    navController.navigate(context.getString(R.string.str_member) + "/6")
                }else
                {showDialog=true}
            } }

        ) {
            Text(text = "新增")
        }
    }


}
}
