package com.tibame.foodhunter.a871208s

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R

@Composable
fun MemberInformationScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris: List<Uri> ->
            selectedImageUris = uris
        }
    )
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },  // 點擊對話框以外區域，關閉對話框
            text = { Text(text = "確定要刪除帳號?") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(context.getString(R.string.str_member) + "/4")

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
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "會員資料",
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
        Text(
            text = "個人頭像",
            modifier = Modifier.padding(16.dp, 8.dp),
            fontSize = 16.sp,
            color = Color.Blue
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                // 設定內容物對齊方式為置中對齊
                contentAlignment = Alignment.Center,
                //modifier = Modifier.fillMaxWidth()

            ) {


                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image",
                    modifier = Modifier
                        .size(120.dp)
                        // 建立圓形邊框
                        .border(BorderStroke(1.dp, Color(0xFFFF9800)), CircleShape)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                TextButton(
                    modifier = Modifier
                        .size(120.dp),
                    shape = RoundedCornerShape(360.dp),
                    onClick = {
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(
                                // 設定只能挑選圖片
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                ) {}
            }

        }



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
                text = "aaaaaaaaa",
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
                text = "暱稱",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = "aaaaaaaaa",
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
                text = "電子郵件",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = "zxc@abc",
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
                text = "手機",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = "0912345678",
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
                text = "生日",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = "2014/10/18",
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
                text = "性別",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = "男",
                fontSize = 14.sp,
                color = Color.Black
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
                onClick = { navController.navigate(context.getString(R.string.str_member) + "/3") }

            ) {
                Text(text = "修改資料")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .size(140.dp, 60.dp)
                    .padding(8.dp),
                onClick = { showDialog = true }

            ) {
                Text(text = "刪除帳號")
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MemberInformationScreenPreview() {
    MaterialTheme {
        MemberInformationScreen()
    }

}
