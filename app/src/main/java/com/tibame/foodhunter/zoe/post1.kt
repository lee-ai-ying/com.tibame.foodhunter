import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.tibame.foodhunter.Main
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import com.tibame.foodhunter.zoe.CarouselItem
import com.tibame.foodhunter.zoe.ImageCarousel
import com.tibame.foodhunter.zoe.Post
import com.tibame.foodhunter.zoe.PostItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Post1(post: Post) {
    val scope = rememberCoroutineScope()

    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        ) {
            ImageCarousel(
                images = post.carouselItems,
                modifier = Modifier
                    .width(394.dp)
                    .height(319.dp)
            )
        }


        // 貼文文字輸入框
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("貼文內容") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

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
fun post1Preview() {
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
        Post1(post = samplePost)
    }
}

