package com.tibame.foodhunter.sharon.components

import Roboto
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.CalendarGroupItem
import com.tibame.foodhunter.sharon.NoteCardItem

@Preview(showBackground = true)
@Composable
fun NoteItemPreview() {
    NoteCardItem(
        navController = rememberNavController(), // 對 NavController 進行預覽時的初始化
        imageResId = R.drawable.sushi_image_1,
        title = "小巷中的咖啡廳"
    )// 預覽時顯示的圖片資源 ID
}

@Preview(showBackground = true)
@Composable
fun CalendarGroupCardPreview() {
    CalendarGroupItem(
    )
}


// 邊條顏色
@Composable
fun ColorBox(color: Color) {
    Box(
        modifier = Modifier
            .width(8.dp)
            .height(96.dp)
            .background(color = color)
    )
}
// 日期樣式
@Composable
fun DateBox(date: String, day: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 19.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = color, shape = CircleShape)
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

// 筆記資訊: 標題、內文
@Composable
fun NoteTextContent(title: String, content: String, modifier: Modifier = Modifier) {
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

// 筆記圖片
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

// 揪團資訊: 主題、人數、餐廳名稱、餐廳地址
@Composable
fun GroupTextContent(
    title: String,
    restaurantName: String,
    restaurantAddress: String,
    headcount: Int
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 14.dp)
            .offset(y=12.dp)
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 揪團主題
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Roboto,  // 使用定義的 Roboto 字體系列
                    fontWeight = FontWeight.Bold, // 使用 SemiBold (700)
                    color = Color(0xFF2A2F3D)
                )
            )
            Spacer(modifier = Modifier.size(6.dp))
            // 揪團人數
            Text(
                text = "($headcount)",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = Roboto,  // 使用定義的 Roboto 字體系列
                    fontWeight = FontWeight.Normal, // 使用 SemiBold (700)
                    color = colorResource(id = R.color.orange_1st)
                )
            )
        }
        // 餐廳名稱
        Text(
            text = restaurantName,
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontFamily = Roboto,  // 使用您定義的 Roboto 字體系列
                fontWeight = FontWeight.Normal, // 使用 Normal (400) 表示 Regular
                color = Color(0xFF4D5364)
            )
        )
        // 餐廳地址
        Text(
            text = restaurantAddress,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = Roboto,  // 使用您定義的 Roboto 字體系列
                fontWeight = FontWeight.Normal, // 使用 Normal (400) 表示 Regular
                color = Color(0xFF4D5364)
            )
        )
    }
}

// 揪團是否為公開狀態
@Composable
fun GroupVisibilityToggle(isPublic: Boolean) {
    SuggestionChip(
        modifier = Modifier
            .height(40.dp)
            .padding(top = 8.dp,end = 2.dp),
        onClick = {},
        label = {
            Text(
                text = if (isPublic) "公開" else "私人",
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (isPublic) colorResource(id = R.color.orange_1st) else colorResource(id = R.color.white), // 背景顏色
            labelColor =  if (isPublic) colorResource(id = R.color.white) else colorResource(id = R.color.orange_d1), // 文字顏色，您可以根據需要替換
        ),
        enabled = true
    )
}