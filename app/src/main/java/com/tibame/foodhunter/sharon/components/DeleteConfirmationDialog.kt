package com.tibame.foodhunter.sharon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//TODO 排版
@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirmed: () -> Unit ={},
    onCancel: () -> Unit ={},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 彈出框標題
            Text(
                text = "確定要刪除？",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 按鈕區域
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 取消按鈕
                TextButton(
                    onClick = { onCancel() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .background(
                            color = Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Text(
                        text = "取消",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 刪除按鈕
                TextButton(
                    onClick = { onDeleteConfirmed() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .background(
                            color = Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Text(
                        text = "刪除",
                        color = Color.Red
                    )
                }
            }
        }
    }
}
