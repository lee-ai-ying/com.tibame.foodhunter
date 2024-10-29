package com.tibame.foodhunter.sharon

import Roboto
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R

@Preview(showBackground = true)
@Composable
fun NoteItemPreview() {
    NoteItem(
        navController = rememberNavController(), // 對 NavController 進行預覽時的初始化
        cardColor = Color.White, // 預覽使用白色背景
        borderColor = Color(0xFFEBEBEB), // 預覽時的邊框顏色
        boxColor = Color(0xFFFFC529), // 預覽時的左側顏色條
        imageResId = R.drawable.sushi_image_1 )// 預覽時顯示的圖片資源 ID

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    navController: NavHostController = rememberNavController(),
    cardColor: Color = Color.White,
    borderColor: Color = Color(0xFFEBEBEB),
    boxColor: Color = Color(0xFFFFC529),
    imageResId: Int
) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .width(380.dp)
            .height(88.dp)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // 黃色:筆記、橘色:揪團
            ColorBox(color = boxColor)
            // 日期 跟 底圖顏色
            DateBox(
                date = "10/17",
                day = "星期四",
                color = Color(0xFFFFEFC3),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            // 標題、內文。傳遞 .weight(1f) 保持填補Row空間
            TextContent(
                title = "小巷中的咖啡廳",
                content = "這裡有各種美味的咖啡和小吃、環境乾淨...",
                modifier = Modifier.weight(1f)
            )
            NoteImage(imageResId = imageResId)
        }
    }
}

@Composable
fun ColorBox(color: Color) {
    Box(
        modifier = Modifier
            .width(8.dp)
            .height(96.dp)
            .background(color = color)
    )
}

@Composable
fun DateBox(date: String, day: String, color: Color ,modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 19.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = Color, shape = CircleShape)
        )
        Column(
            modifier = Modifier.padding(top = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date,
                style = TextStyle(fontSize = 16.sp, color = Color(0xFF2A2F3D), fontWeight = FontWeight.Medium)
            )
            Text(
                modifier = Modifier.offset(y = (-3).dp),
                text = day,
                style = TextStyle(fontSize = 16.sp, color = Color(0xFF2A2F3D), fontWeight = FontWeight.Medium)
            )
        }
    }
}

// 接收 Modifier 參數的 TextContent
@Composable
fun TextContent(title: String, content: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 14.dp)
            .offset(y=14.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 16.sp, color = Color(0xFF2A2F3D), fontWeight = FontWeight.Bold)
        )
        Text(
            text = content,
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF4D5364), lineHeight = 16.sp)
        )
    }
}

@Composable
fun NoteImage(imageResId: Int) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = "image description",
        modifier = Modifier
            .size(width = 100.dp, height = 88.dp)
            .clip(CardDefaults.shape),
        contentScale = ContentScale.Crop
    )
}



