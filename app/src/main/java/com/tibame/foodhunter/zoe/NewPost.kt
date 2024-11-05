import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.tibame.foodhunter.R
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.andysearch.ShowGoogleMap
import com.tibame.foodhunter.andysearch.ShowRestaurantLists
import com.tibame.foodhunter.sharon.components.SearchBar
import com.tibame.foodhunter.ui.theme.FColor
import com.tibame.foodhunter.ui.theme.FoodHunterTheme
import com.tibame.foodhunter.zoe.ImageDisplay
import com.tibame.foodhunter.zoe.ImageSource
import com.tibame.foodhunter.zoe.PostCreateData
import com.tibame.foodhunter.zoe.PostViewModel

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
    testVM: SearchScreenVM = viewModel()
) {
    val choiceRest by testVM.choiceOneRest.collectAsState()
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    var selectedLocation by remember { mutableStateOf("") }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var currentSheet by remember { mutableStateOf(NewPostSheetContent.NONE) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris: List<Uri> ->
            selectedImageUris = uris
        }
    )
    var inputData by remember { mutableStateOf(PostCreateData()) }

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
                    selectedTags = selectedTags,
                    onFilterChange = { newTags ->
                        selectedTags = newTags
                        inputData = inputData.copy(postTag = newTags.joinToString(","))
                    },
                    onConfirm = {
                        showBottomSheet = false // 按下確定後收起 BottomSheet
                    }
                )

                NewPostSheetContent.LOCATION -> LocationSelectionSheet(
                    onLocationSelected = { location ->
                        Log.d("location3", "NewPost: $location")
                        selectedLocation = location
                        inputData = inputData.copy(location = location)
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

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        inputData.content = newText.toString()
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
                        if (selectedLocation.isEmpty()) {
                            Text(text = stringResource(id = R.string.restaurant_location))
                        } else {
                            Text(text = selectedLocation)
                        }
                    }
                }

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
                        if (selectedTags.isEmpty()) {
                            Text(text = stringResource(id = R.string.Select_tag))
                        } else {
                            Text(text = selectedTags.joinToString(", "))
                        }
                    }
                }

                Button(
                    onClick = {
                        postViewModel.setPostCreateData(inputData)
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
    onLocationSelected: (String) -> Unit,
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

        var currentLocation by remember { mutableStateOf<LatLng?>(null) }
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
                scope.launch{testVM.updateSearchRest(searchQuery)}
            }
        )

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween){
            ShowGoogleMap(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(16.dp),
                restaurants = testRestaurant,
                restaurantVM = testVM,
                onLocationUpdate = {location -> currentLocation = location}
            )
            ShowRestaurantLists(
                restaurants = testRestaurant,
                state = false,
                currentLocation = currentLocation,
                searchTextVM = testVM,
                cardClick = {
                    onLocationSelected(it?.name ?: "")
                }
            )
        }
    }
}
@OptIn( ExperimentalLayoutApi::class)
@Composable


fun TagSelectionSheet(
    availableTags: List<String>,  // 傳入可選的標籤列表
    selectedTags: Set<String>,    // 傳入已選中的標籤
    onFilterChange: (Set<String>) -> Unit,  // 當篩選標籤發生變化時的回調
    onConfirm: () -> Unit         // 確定按鈕的回調
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
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        selectedContainerColor = colorResource(R.color.orange_3rd) // 設定選中顏色
                    ),
                    border = if (isSelected) {
                        BorderStroke(2.dp, colorResource(R.color.orange_5th)) // 設定選中時的邊界顏色
                    } else {
                        BorderStroke(2.dp, Color.LightGray) // 未選中時的邊界顏色
                    },

                    )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 確定按鈕
        Button(
            onClick = {
                onConfirm() // 按下確定時的回調
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.orange_1st)
            ),

            ) {
            Text(text = "確定", color = Color.White) // 按鈕文字
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PostPreview() {

    FoodHunterTheme {
        NewPost(rememberNavController())
    }
}

