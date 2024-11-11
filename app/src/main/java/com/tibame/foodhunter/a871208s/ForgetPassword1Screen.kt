package com.tibame.foodhunter.a871208s

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.Main
import com.tibame.foodhunter.R
import kotlinx.coroutines.launch

@Composable
fun ForgetPassword1Screen(
    navController: NavHostController = rememberNavController(),
    callback: @Composable () -> Unit, userVM: UserViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "驗證碼錯誤") },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("確定")
                }
            }
        )
    }
    var showDialog2 by remember { mutableStateOf(false) }
    if (showDialog2) {
        AlertDialog(
            onDismissRequest = { showDialog2 = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "電子信箱錯誤") },
            confirmButton = {
                Button(
                    onClick = { showDialog2 = false }  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("確定")
                }
            }
        )
    }
    var showDialog3 by remember { mutableStateOf(false) }
    if (showDialog3) {
        AlertDialog(
            onDismissRequest = { showDialog3 = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "驗證碼已發送") },
            confirmButton = {
                Button(
                    onClick = { showDialog3 = false }  // 點擊確定按鈕，關閉對話框
                ) {
                    Text("確定")
                }
            }
        )
    }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "忘記密碼",
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        HorizontalDivider(
            modifier = Modifier.size(500.dp, 1.dp),
            color = Color.Blue
        )
        Spacer(modifier = Modifier.padding(4.dp))
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "請輸入信箱", fontSize = 18.sp) },
                singleLine = true,
                shape = RoundedCornerShape(32.dp),
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
                    .size(200.dp, 60.dp)
                    .padding(8.dp),
                onClick = {
                    coroutineScope.launch {
                        if (userVM.getEmailInfo(email) == null) {
                            showDialog2 = true
                        } else {
                            showDialog3 = true
                            userVM.emailFindPassword.value =email
                        }
                    }
                }
            ) {
                Text(text = "發送驗證碼")
            }
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp, 2.dp)
        ) {
            Text(
                text = "驗證碼",
                fontSize = 20.sp,
                color = Color.Blue
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "請輸入驗證碼", fontSize = 18.sp) },
                singleLine = true,
                shape = RoundedCornerShape(32.dp),
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
                onClick = {
                    if (password == "000000" && userVM.emailFindPassword.value != "") {
                        navController.navigate(context.getString(R.string.str_login) + "/4")
                    } else {
                        showDialog = true
                    }


                }
            ) {
                Text(text = "下一步")
            }
            Button(
                modifier = Modifier
                    .size(120.dp, 60.dp)
                    .padding(8.dp),
                onClick = {
                    userVM.emailFindPassword.value = ""
                    navController.navigate(context.getString(R.string.str_login))
                }
            ) {
                Text(text = "返回")
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPassword1ScreenPreview() {

    MaterialTheme {
        Main()
    }

}