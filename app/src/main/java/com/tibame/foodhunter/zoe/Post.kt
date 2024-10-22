import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import com.tibame.foodhunter.zoe.CarouselItem
import com.tibame.foodhunter.zoe.ImageCarousel
import com.tibame.foodhunter.zoe.ImageList
import com.tibame.foodhunter.zoe.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Post(post: Post) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // 整個 Column 的內容置中
    ) {
        // 顯示圖片輪播
//        Row(
//            modifier = Modifier.fillMaxWidth(), // 讓 Row 撐滿寬度
//            horizontalArrangement = Arrangement.Center, // 水平置中
//            verticalAlignment = Alignment.Top,
//        ) {
//            ImageCarousel(
//                images = post.carouselItems,
//                modifier = Modifier
//                    .width(300.dp)  // 設定寬度
//                    .height(300.dp) // 設定高度，讓它保持正方形
//            )
//        }

        Spacer(modifier = Modifier.height(16.dp)) // 添加間距

        // 貼文文字輸入框
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("貼文內容") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,  // 焦點時容器背景透明
                unfocusedContainerColor = Color.Transparent, // 未焦點時容器背景透明
                disabledContainerColor = Color.Transparent,  // 禁用時容器背景透明
                errorContainerColor = Color.Transparent,     // 錯誤狀態下容器背景透明
                focusedIndicatorColor = Color.Transparent,   // 焦點框線透明
                unfocusedIndicatorColor = Color.Transparent, // 未焦點框線透明
                disabledIndicatorColor = Color.Transparent,  // 禁用狀態下框線透明
                errorIndicatorColor = Color.Transparent      // 錯誤狀態下框線透明
            )
        )


        Spacer(modifier = Modifier.height(16.dp)) // 添加間距

        Button(
            onClick = { /* 發文邏輯 */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("發文")
        }
    }
}


@Composable
fun BottomSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("這是底部彈出視窗的內容")
        Spacer(modifier = Modifier.height(16.dp))
        // 你可以放入任何內容，例如列表選擇器、開關等
        Button(onClick = { /* 點擊事件 */ }) {
            Text("確認")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PostPreview() {
    // 建立一個範例 Post 物件
    val samplePost = Post(
        publisher = "Alice",
        content = "今天的早餐好好吃！",
        visibility = 0,
        location = "Taipei",
        publisherImage = R.drawable.user2, // 使用一個圖示作為範例圖片
        postTag = "早餐",
        carouselItems = listOf(
            CarouselItem(0, R.drawable.breakfast_image_1, "早餐圖1"),
            CarouselItem(1, R.drawable.steak_image, "早餐圖2"),
            CarouselItem(2, R.drawable.steak_image, "早餐圖3")
        )
    )

    FoodHunterTheme {
        Post(post = samplePost)
    }
}

