import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import com.tibame.foodhunter.zoe.CarouselItem
import com.tibame.foodhunter.zoe.ImageCarousel
import com.tibame.foodhunter.zoe.ImageList
import com.tibame.foodhunter.zoe.Post
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun Post(post: Post) {
    var message by remember { mutableStateOf("") }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val scaffoldState = rememberBottomSheetScaffoldState() // 使用 BottomSheetScaffold 狀態
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .padding(16.dp) // 調整 padding 來給內容留出邊距
    ) {
        Modifier
            .width(414.dp)
            .height(587.dp)
            .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp)
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                ShareOptionsSheet(
                    scope = scope,
                    sheetState = scaffoldState, // 使用 scaffoldState
                    onSelectOption = { selectedMessage ->
                        message = selectedMessage
                        // 選擇後隱藏 BottomSheet
                        scope.launch { scaffoldState.bottomSheetState.hide() }
                    }
                )
            },
            sheetPeekHeight = 0.dp // 設定初始高度
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f), // 主內容區塊調整為占 80% 高度
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 顯示圖片輪播
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top,
                ) {
                    ImageCarousel(
                        images = post.carouselItems,
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 貼文文字輸入框
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("貼文內容") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 按鈕，點擊後彈出 BottomSheet
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand() // 展開 BottomSheet
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.Share_with))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 發文按鈕
                Button(
                    onClick = { /* 發文邏輯 */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.str_post))
                }
            }
        }
    }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareOptionsSheet(
    scope: CoroutineScope,
    sheetState: BottomSheetScaffoldState, // 直接使用 BottomSheetScaffoldState
    onSelectOption: (String) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.Share_with))
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,) {
            Modifier
                .width(370.dp)
                .height(124.dp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
//                .width(313.dp)
//                .height(40.dp)
//                .padding(start = 24.dp, top = 10.dp, end = 24.dp, bottom = 10.dp)

                Button(onClick = {


                    val message = context.getString(R.string.only_friend)
                    onSelectOption(message)
                    scope.launch { sheetState.bottomSheetState.hide()  } // 收起 BottomSheet
                }) {
                    Text(text = stringResource(id = R.string.only_friend))
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val message = context.getString(R.string.for_public)
                onSelectOption(message)
                scope.launch { sheetState.bottomSheetState.hide() } // 收起 BottomSheet
            }) {
                Text(text = stringResource(id = R.string.for_public))
            }
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

