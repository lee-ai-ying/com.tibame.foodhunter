package com.tibame.foodhunter.sharon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tibame.foodhunter.ui.theme.FColor
import com.tibame.foodhunter.ui.theme.FTypography

@Preview
@Composable
fun DeleteConfirmationDialogP(){
    DeleteConfirmationDialog()
}


//TODO 排版
@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirmed: () -> Unit ={},
    onCancel: () -> Unit ={},
) {
    Box(
        modifier = Modifier
            .width(288.dp)
            .height(144.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            // 彈出框標題
            Text(
                text = "確定要刪除？",
                style = FTypography.Title1,
                color = FColor.Dark_80,
                modifier = Modifier.padding(start = 10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 按鈕區域
            Row(
                modifier = Modifier.width(200.dp).padding(start = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // 取消按鈕
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { onCancel() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = FColor.Dark_80
                    ),
                ) {
                    Text(text = "取消",style = FTypography.Label1)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 刪除按鈕
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { onDeleteConfirmed() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = FColor.Orange_1st
                    ),

                ) {
                    Text(text = "刪除",style = FTypography.Label1)
                }
            }

        }
    }
}
