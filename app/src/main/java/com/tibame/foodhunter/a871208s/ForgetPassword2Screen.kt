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
fun ForgetPassword2Screen(
    navController: NavHostController = rememberNavController(),
    callback: @Composable () -> Unit, userVM: UserViewModel
) {
    val context = LocalContext.current
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "密碼長度必須介於6-12") },
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
            text = { Text(text = "新密碼與再次輸入密碼不符") },
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
                text = "新密碼",
                fontSize = 20.sp,
                color = Color.Blue
            )
            OutlinedTextField(
                value = password1,
                onValueChange = { password1 = it },
                placeholder = { Text(text = "請輸入密碼", fontSize = 18.sp) },
                singleLine = true,
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(16.dp, 2.dp)
        ) {
            Text(
                text = "再次輸入密碼",
                fontSize = 20.sp,
                color = Color.Blue
            )
            OutlinedTextField(
                value = password2,
                onValueChange = { password2 = it },
                placeholder = { Text(text = "請輸入密碼", fontSize = 18.sp) },
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
                    coroutineScope.launch {
                        if (password1 == password2) {
                            val saved =
                                userVM.saveNewPassword(userVM.emailFindPassword.value, password1)
                            if (saved) {
                                navController.navigate(context.getString(R.string.str_login))
                                userVM.emailFindPassword.value = ""
                            } else {
                                showDialog = true
                            }
                        } else {
                            showDialog2 = true
                        }

                    }
                }
            ) {
                Text(text = "完成")
            }
            Button(
                modifier = Modifier
                    .size(120.dp, 60.dp)
                    .padding(8.dp),
                onClick = {
                    userVM.emailFindPassword.value = ""
                    navController.navigate(context.getString(R.string.str_login) + "/3")
                }
            ) {
                Text(text = "返回")
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPassword2ScreenPreview() {
    MaterialTheme {
        Main()
    }

}