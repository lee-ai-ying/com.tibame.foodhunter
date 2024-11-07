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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import kotlinx.coroutines.launch

@Composable
fun ModifyInformationScreen(navController: NavHostController = rememberNavController(),userVM: UserViewModel) {
    val context = LocalContext.current

    val passwordState = remember { mutableStateOf("") }
    val nicknameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val phoneState = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val user = userVM.getUserInfo(userVM.username.value) // 替換為實際用戶 ID
            if (user != null) {
                passwordState.value = user.password // 獲取用戶名
                nicknameState.value = user.nickname
                emailState.value = user.email
                phoneState.value =user.phone

            }
        }
    }

    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "確定要修改資料?") },
            confirmButton = {
                Button(

                    onClick = {
                        coroutineScope.launch {
                            if (password == "") {
                                password = passwordState.value
                            }
                            if (nickname == "") {
                                nickname = nicknameState.value
                            }
                            if (email == "") {
                                email = emailState.value
                            }
                            if (phone == "") {
                                phone = phoneState.value
                            }
                            val saved =
                                userVM.save(userVM.username.value, password, nickname, email, phone)
                            if (saved) {
                                navController.navigate(context.getString(R.string.str_member) + "/2")
                            } else {
                                showDialog2 = true
                                showDialog = false
                            }
                        }
                        }  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("確定")
                }
                Button(
                    onClick = { showDialog = false }  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("取消")
                }
            }
        )
    }

    if (showDialog2) {
        AlertDialog(
            onDismissRequest = { showDialog2 = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "修改格式錯誤") },
            confirmButton = {
                Button(
                    onClick = { showDialog2 = false }  // 點擊確定按鈕，關閉對話框
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
        Text(
            text = "修改資料",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        HorizontalDivider(
            modifier = Modifier.size(500.dp, 1.dp),
            color = Color.Blue
        )
        //Spacer(modifier = Modifier.padding(2.dp))


        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp, 2.dp)
        ) {
            Text(
                text = "帳號",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = userVM.username.value,
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp, 2.dp)
        ) {
            Text(
                text = "密碼",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = passwordState.value, fontSize = 18.sp) },
                singleLine = true,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }


        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp, 2.dp)
        ) {
            Text(
                text = "暱稱",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                placeholder = { Text(text = nicknameState.value, fontSize = 18.sp) },
                singleLine = true,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp, 2.dp)
        ) {
            Text(
                text = "電子信箱",
                fontSize = 20.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = emailState.value, fontSize = 18.sp) },
                singleLine = true,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp, 2.dp)
        ) {
            Text(
                text = "手機",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text(text = phoneState.value, fontSize = 18.sp) },
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
                    .size(220.dp, 60.dp)
                    .padding(8.dp),
                onClick = { showDialog = true }

            ) {
                Text(text = "完成修改")
            }
        }


    }
}




