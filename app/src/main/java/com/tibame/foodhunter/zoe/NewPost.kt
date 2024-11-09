import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.a871208s.UserViewModel
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.ui.theme.FColor
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import com.tibame.foodhunter.zoe.ImageDisplay
import com.tibame.foodhunter.zoe.ImageSource
import com.tibame.foodhunter.zoe.PostEditorViewModel
import com.tibame.foodhunter.zoe.PostViewModel
import com.tibame.foodhunter.zoe.RestaurantList
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")



enum class NewPostSheetContent {
    NONE,
    TAGS,
    LOCATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPost(
    navController: NavHostController,
    postViewModel: PostViewModel = viewModel(),
    testVM: SearchScreenVM = viewModel(),
    PostEditorViewModel: PostEditorViewModel = viewModel(),
    userVM: UserViewModel,
    postId: Int? = null
) {

    val choiceRest by testVM.choiceOneRest.collectAsState()
    var selectedTag by remember { mutableStateOf("") }
    val selectedLocation by PostEditorViewModel.selectedLocation.collectAsState()
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var currentSheet by remember { mutableStateOf(NewPostSheetContent.NONE) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isEditing by remember { mutableStateOf(false) }
    // 收集貼文數據
    val post = if (postId != null) {
        postViewModel.getPostById(postId).collectAsState().value
    } else {
        null
    }
    val membernameState = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val user = userVM.getUserInfo(userVM.username.value)
            if (user != null) {
                membernameState.value = user.id
            }
        }
    }


    // 監聽貼文數據變化並更新 UI
    LaunchedEffect(post) {
        post?.let {
            isEditing = true
            text = TextFieldValue(it.content)
            selectedTag = it.postTag
            // 這裡可以添加圖片 URI 的處理
            PostEditorViewModel.apply {
                updateInputData(content = it.content)
                updateTags(setOf(it.postTag))
                updateLocation(0, it.location)
                // 如果有需要，這裡可以設置原有的圖片
            }
        }
    }



    // 更新圖片選擇器
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris: List<Uri> ->
            selectedImageUris = uris
            PostEditorViewModel.updatePhotos(context, uris)
        }
    )

    // 標籤列表
    val availableTags = remember {
        listOf(
            "早午餐", "午餐", "晚餐", "下午茶",
            "宵夜", "甜點", "飲料"
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                currentSheet = NewPostSheetContent.NONE
            },
            containerColor = Color.White,
            sheetMaxWidth = Dp.Unspecified,
            windowInsets = WindowInsets(0),
        ) {
            when (currentSheet) {
                NewPostSheetContent.TAGS -> TagSelectionSheet(
                    availableTags = availableTags,
                    selectedTags = selectedTag,
                    onFilterChange = { newTag ->
                        selectedTag = newTag
                        // 將單個標籤包裝為 Set 後更新
                        PostEditorViewModel.updateTags(setOf(newTag))
                    },
                    onConfirm = {
                        showBottomSheet = false
                    }
                )
                NewPostSheetContent.LOCATION -> LocationSelectionSheet(
                    onLocationSelected = { restaurantId ->
                        choiceRest?.let { restaurant ->
                            PostEditorViewModel.updateLocation(restaurantId, restaurant.name)
                        }
                        showBottomSheet = false
                    },
                    testVM = testVM
                )
                NewPostSheetContent.NONE -> {}
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // 圖片顯示區域
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
                    .padding(16.dp)
            ) {
                if (selectedImageUris.isNotEmpty()) {
                    ImageDisplay(
                        imageSource = ImageSource.UriSource(selectedImageUris),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // 輸入區域
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                // 文字輸入框
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        PostEditorViewModel.updateInputData(content = newText.text)
                    },
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
                        errorIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Gray,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.LightGray
                    )
                )

                // 圖片選擇按鈕
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(
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

                // 位置選擇按鈕
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        currentSheet = NewPostSheetContent.LOCATION
                        showBottomSheet = true
                    },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "location"
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = if (selectedLocation.isEmpty())
                            stringResource(id = R.string.restaurant_location)
                        else
                            selectedLocation
                        )
                    }
                }

                // 標籤選擇按鈕
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        currentSheet = NewPostSheetContent.TAGS
                        showBottomSheet = true
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
                        Text(text = if (selectedTag.isEmpty())
                            stringResource(id = R.string.Select_tag)
                        else
                            selectedTag
                        )
                    }
                }

                // 發布按鈕
                Button(
                    onClick = {
                        PostEditorViewModel.updatePublisher(membernameState.value)
                        PostEditorViewModel.submitPost(context)
                        navController.navigate(context.getString(R.string.str_Recommended_posts))
                    },
                    colors = ButtonDefaults.buttonColors(
                        colorResource(id = R.color.orange_2nd)
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f),
                ) {
                    Text(text = stringResource(id = R.string.str_post))
                }
            }
        }
    }
}
@Composable
fun LocationSelectionSheet(
    onLocationSelected: (Int) -> Unit,
    testVM: SearchScreenVM
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "選擇餐廳位置",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val testRestaurant by testVM.selectRestList.collectAsState()
        val scope = rememberCoroutineScope()
        var searchQuery by remember { mutableStateOf("") }
        var isActive by remember { mutableStateOf(false) }

        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = {
                Text(
                    "搜尋",
                    color = FColor.Gary,
                    fontSize = 16.sp
                )
            },
            active = isActive,
            onActiveChange = { isActive = it },
            modifier = Modifier.padding(horizontal = 16.dp),
            onSearch = {
                scope.launch { testVM.updateSearchRest(searchQuery) }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 使用現有的 RestaurantList
        RestaurantList(
            restaurants = testRestaurant,
            onRestaurantSelected = { restaurantId ->
                // 找到被選中的餐廳
                testRestaurant.find { it.restaurant_id == restaurantId }?.let { restaurant ->
                    // 更新 ViewModel 中選擇的餐廳
                    scope.launch {
                        testVM.updateChoiceOneRest(restaurant)
                    }
                }
                // 呼叫回調函數
                onLocationSelected(restaurantId)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSelectionSheet(
    availableTags: List<String>,
    selectedTags: String,    // 改為單個字串
    onFilterChange: (String) -> Unit,  // 改為回傳單個字串
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.Select_tag))
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableTags.forEach { tag ->
                val isSelected = tag == selectedTags  // 改為直接比較字串

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onFilterChange(tag)  // 直接傳遞選中的標籤
                    },
                    label = { Text(tag) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        selectedContainerColor = colorResource(R.color.orange_3rd)
                    ),
                    border = if (isSelected) {
                        BorderStroke(2.dp, colorResource(R.color.orange_5th))
                    } else {
                        BorderStroke(2.dp, Color.LightGray)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.orange_1st)
            )
        ) {
            Text(text = "確定", color = Color.White)
        }
    }
}



//
//@Preview(showBackground = true)
//@Composable
//fun PostPreview() {
//
//    FoodHunterTheme {
//        NewPost(rememberNavController())
//    }
//}