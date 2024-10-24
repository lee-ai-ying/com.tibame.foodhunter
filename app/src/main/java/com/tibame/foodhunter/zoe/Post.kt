import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import com.tibame.foodhunter.zoe.ImageDisplay
import com.tibame.foodhunter.zoe.ImageSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun NewPost(navController: NavHostController) {
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    var message by remember { mutableStateOf("") }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris: List<Uri> ->
            selectedImageUris = uris
        }
    )

    val availableTags = remember {
        listOf(
            "早午餐",
            "午餐",
            "晚餐",
            "下午茶",
            "宵夜",
            "甜點",
            "飲料"
        )
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            TagSelectionSheet(
                scope = scope,
                sheetState = sheetState,
                availableTags = availableTags,
                selectedTags = selectedTags,
                onFilterChange = { newTags ->
                    selectedTags = newTags
                    scope.launch { sheetState.bottomSheetState.partialExpand() }
                }
            )
        },
        sheetPeekHeight = 0.dp,  // 完全隱藏時的高度
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .padding(paddingValues)
        ) {


            Row(
                horizontalArrangement = Arrangement.Center, // 水平置中
                verticalAlignment = Alignment.CenterVertically, // 垂直置中
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                if (selectedImageUris.isNotEmpty()) {  // 只在有選擇圖片時顯示
                    ImageDisplay(
                        imageSource = ImageSource.UriSource(selectedImageUris),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp), // 項目之間的垂直間距
                horizontalAlignment = Alignment.CenterHorizontally, // 水平置中
                modifier = Modifier
                    .fillMaxSize() // 根據需要填滿可用空間
            ) {
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
                Button(

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(
                                // 設定只能挑選圖片
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.photo),
                            contentDescription = "photo",
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = stringResource(id = R.string.select_picture))
                    }
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = { /* 跳到餐廳 */ },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "location" // 添加有效的內容描述
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = stringResource(id = R.string.restaurant_location))
                    }
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        scope.launch {
                            sheetState.bottomSheetState.expand()
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tag),
                            contentDescription = "Select Tag",
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        if (selectedTags.isEmpty()) {
                            Text(text = stringResource(id = R.string.Select_tag))
                        } else {
                            Text(text = selectedTags.joinToString(", "))
                        }
                    }
                }

                Button(
                    
                    onClick = { /* 發文邏輯 */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("發文")



            }




            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable

fun TagSelectionSheet(
    scope: CoroutineScope,
    sheetState: BottomSheetScaffoldState,
    availableTags: List<String>,  // 傳入可選的標籤列表
    selectedTags: Set<String>,    // 傳入已選中的標籤
    onFilterChange: (Set<String>) -> Unit  // 當篩選標籤發生變化時的回調
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.Select_tag))  // 標籤選擇標題
        Spacer(modifier = Modifier.height(16.dp))

        // 使用 FlowRow 來自動換行顯示標籤
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),  // 增加內邊距
            horizontalArrangement = Arrangement.spacedBy(8.dp), // 主軸間距
            verticalArrangement = Arrangement.spacedBy(8.dp)    // 副軸間距
        ) {
            availableTags.forEach { tag ->
                val isSelected = selectedTags.contains(tag)

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        // 更新選中的篩選標籤
                        val updatedTags = if (isSelected) {
                            selectedTags - tag
                        } else {
                            selectedTags + tag
                        }
                        onFilterChange(updatedTags)  // 通知標籤選擇變更
                    },
                    label = { Text(tag) },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }

                )
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//
//fun ShareOptionsSheet(
//    scope: CoroutineScope,
//    sheetState: BottomSheetScaffoldState, // 直接使用 BottomSheetScaffoldState
//    onSelectOption: (String) -> Unit
//) {
//    val context = LocalContext.current
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.5f),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = stringResource(id = R.string.Share_with))
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 按鈕區塊
//        Column(
//            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                horizontalArrangement = Arrangement.Center, // 按鈕置中
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Button(
//                    onClick = {
//                        val message = context.getString(R.string.only_friend)
//                        onSelectOption(message)
//                        scope.launch { sheetState.bottomSheetState.hide() } // 收起 BottomSheet
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth() // 按鈕占滿整個 Row
//                        .height(40.dp) // 指定按鈕高度
//                ) {
//                    Text(text = stringResource(id = R.string.only_friend))
//                }
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                horizontalArrangement = Arrangement.Center, // 按鈕置中
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(
//                    onClick = {
//                        val message = context.getString(R.string.for_public)
//                        onSelectOption(message)
//                        scope.launch { sheetState.bottomSheetState.hide() } // 收起 BottomSheet
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth() // 按鈕占滿整個 Row
//                        .height(40.dp) // 指定按鈕高度
//                ) {
//                    Text(text = stringResource(id = R.string.for_public))
//                }
//            }
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun PostPreview() {

    FoodHunterTheme {
        NewPost(rememberNavController())
    }
}

