package com.tibame.foodhunter.andysearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RandomFood(
    navController: NavHostController,
    searchTextVM: SearchScreenVM,
    randomFoodVM: RandomFoodVM = viewModel()
) {
    var rotated by remember { mutableStateOf(false) }
    val rotateAngle by remember { mutableStateOf(0f) }
    val currentRotation by remember { mutableStateOf(0f) }
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "InfiniteTransition")
    val animatedRotationAngle by infiniteTransition.animateFloat(
        initialValue = currentRotation, // 從當前的角度開始
        targetValue = if (rotated) 360f else rotateAngle,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 650,
                easing = LinearEasing
            )
        ), label = "infiniteTransition.animateFloat"
    )


    val options by randomFoodVM.settingRandomFood.collectAsState()
    val colors = listOf(FColor.Banana_Yellow, FColor.Orange_5th)
    val optionsSize = options.size

    var showDialog by remember { mutableStateOf(false) }
    var showSettingDialog by remember { mutableStateOf(false) }
    var showCitySettingDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    val randomScope = rememberCoroutineScope()
    val onRandomFood: (rotated: Boolean) -> Unit = {
        if (rotated) {
            randomScope.launch {
                delay(2500)
                selectedOption = options.random()
                rotated = false
                showDialog = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Button(
            modifier = Modifier
                .size(200.dp, 100.dp)
                .padding(16.dp),
            onClick = {showCitySettingDialog = !showCitySettingDialog},
            colors = ButtonDefaults.buttonColors(FColor.Orange_3rd)
        ) {
            Text(text = "台北市", color = FColor.Dark)
            Text(text = "大安區", color = FColor.Dark)
        }


        Box(
            modifier = Modifier.size(350.dp),
            contentAlignment = Alignment.Center
        ) {
            var optionNumber = 0f
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(animatedRotationAngle) // 旋轉動畫
            ) {
                options.forEachIndexed { index, content ->
                    // 繪製弧形
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = optionNumber, // 0代表3點鐘方向
                        sweepAngle = 360f / optionsSize, // 順時針畫出280度
                        useCenter = true,
                        size = size
                    )

                    val radians = Math.toRadians(optionNumber.toDouble()) // 轉換為弧度

                    // 設置 start 和 end 位置
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = size.width / 2

                    // 根據中間角度計算 end 位置
                    val endX = centerX + radius * cos(radians).toFloat()
                    val endY = centerY + radius * sin(radians).toFloat()

                    // 繪製直線，從圓心到外圍
                    drawLine(
                        color = FColor.Dark,
                        start = Offset(centerX, centerY),  // 圓心作為開始點
                        end = Offset(endX, endY),          // 計算的終點
                        strokeWidth = 5f
                    )

                    // 繪製文字
                    val textRadius = radius * 0.6f  // 文字的位置，圓心的 60% 處
                    val textAngleRadians =
                        Math.toRadians((optionNumber + 360f / optionsSize / 2).toDouble())  // 計算文字的中間角度

                    val textX = centerX + textRadius * cos(textAngleRadians).toFloat()
                    val textY = centerY + textRadius * sin(textAngleRadians).toFloat()

                    drawIntoCanvas { canvas ->
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 40f  // 設定文字大小
                        }
                        canvas.nativeCanvas.drawText(
                            content,
                            textX,
                            textY,
                            paint
                        )
                    }
                    optionNumber += 360f / optionsSize
                }

            }

            // 按鈕
            Button(
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .border(2.dp, Color.Black, shape = CircleShape),
                colors = ButtonDefaults.buttonColors(FColor.Orange_3rd),
                onClick = {
                    rotated = true
                    onRandomFood(rotated)
                }
            ) {
                Text(text = "開始", color = FColor.Dark)
            }
            if (showSettingDialog) {
                SettingDialog(onDismiss = { showSettingDialog = false }, randomFoodVM)
            }
            if (showCitySettingDialog){
                CityDialog(onDismiss = { showSettingDialog = false })
            }
        }

        Button(
            modifier = Modifier
                .size(200.dp, 100.dp)
                .padding(16.dp),
            onClick = { showSettingDialog = true },
            colors = ButtonDefaults.buttonColors(FColor.Orange_3rd)
        ) {
            Text(text = "設定轉盤", color = FColor.Dark)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },  // 點擊對話框以外區域，關閉對話框
                text = { Text(text = "最終選擇: $selectedOption") },
                confirmButton = {
                    Button(
                        onClick = {
                            searchTextVM.updateSearchText(selectedOption)
                            randomScope.launch { searchTextVM.updateSearchRest(selectedOption) }
                            searchTextVM.loadShowSearchText()
                            navController.navigate(context.getString(R.string.SearchToGoogleMap))
                        }, // 點擊確定按鈕，關閉對話框
                        colors = ButtonDefaults.buttonColors(FColor.Orange_3rd)
                    ) {
                        Text("確定")
                    }
                }
            )
        }
    }
}

@Composable
fun CityDialog(onDismiss:()->Unit) {
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var selectedCityIndex by remember { mutableStateOf(0) }
    var citySet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cities = remember { parseCityJson(context, "taiwan_districts.json") }
    var districtExpand by remember { mutableStateOf(false) }
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(500.dp)
                .shadow(elevation = 32.dp, shape = RectangleShape) // 添加陰影
                .clip(RectangleShape) // 將陰影範圍限制在對話框邊界內
                .background(FColor.Yellow_2)
                .padding(16.dp)
        ) {

            Row(modifier = Modifier.align(Alignment.TopStart)) {
                LazyColumn(modifier = Modifier.padding(start = 16.dp)) {
                    itemsIndexed(cities) { index, city ->
                        val textColor = if (selectedCity == city.name) Color.Blue else Color.Black

                        Text(text = city.name, color = textColor,
                            modifier = Modifier
                                .clickable {
                                    districtExpand = !districtExpand
                                    selectedCity =
                                        if (selectedCity == city.name) null else city.name
                                    selectedCityIndex = index
                                }
                        )
                    }
                }
                VerticalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 5.dp,
                    color = Color.Black
                )


//                Spacer(modifier = Modifier.width(16.dp)) // 使用 Spacer 代替 VerticalDivider
//
//                foodLabel("restaurant_tags", randomFoodVM)
                Column {
                    if (districtExpand) {
                        DistrictLabel(selectedCityIndex, cities, onDismiss)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingDialog(onDismiss: () -> Unit, randomFoodVM: RandomFoodVM) {
    var restSet by remember { mutableStateOf(false) }
    var foodSet by remember { mutableStateOf(false) }
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(500.dp)
                .shadow(elevation = 32.dp, shape = RectangleShape) // 添加陰影
                .clip(RectangleShape) // 將陰影範圍限制在對話框邊界內
                .background(FColor.Yellow_2)
                .padding(16.dp)
        ) {

            Row(modifier = Modifier.align(Alignment.TopStart)) {
                Column {
                    Text("餐廳種類",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clickable { restSet = !restSet })
                    Text("食物種類", style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clickable { foodSet = !foodSet })
                }
                VerticalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 5.dp,
                    color = Color.Black
                )


//                Spacer(modifier = Modifier.width(16.dp)) // 使用 Spacer 代替 VerticalDivider
//
//                foodLabel("restaurant_tags", randomFoodVM)
                Column {
                    if (restSet) {
                        FoodLabel("restaurant_tags", randomFoodVM)
                    }
                    if (foodSet) {
                        FoodLabel("food_tags", randomFoodVM)
                    }
                }
            }

            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(FColor.Orange_3rd)
            ) {
                Text("確認", color = FColor.Dark)
            }

        }
    }
}

@Composable
fun FoodLabel(selectLabel: String, randomFoodVM: RandomFoodVM) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        randomFoodVM.loadFoodLabel(context)
    }
    val foodLabel by randomFoodVM.foodLabel.collectAsState()
    val selectedItems = remember { mutableStateListOf<String>() }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 60.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        items(foodLabel[selectLabel] ?: emptyList()) { filter ->
            var selected by remember { mutableStateOf(false) }
            FilterChip(
                selected = selected,
                onClick = {
                    selected = !selected
                    if (selected) {
                        selectedItems.add(filter)
                    } else {
                        selectedItems.remove(filter)
                    }
                    // 更新 ViewModel 中的設定隨機食物列表
                    randomFoodVM.updateSettingRandomFood(selectedItems)
                },
                label = { Text(filter) },
                colors =
                FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    selectedContainerColor = (colorResource(R.color.orange_5th)),
                    selectedLabelColor = FColor.Orange_3rd

                ),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun DistrictLabel(cityIndex: Int, cities: List<City>, onDismiss:() -> Unit){
    val context = LocalContext.current
    val selectedItems = remember { mutableStateListOf<String>() }
    val city = cities[cityIndex]
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
    ) {
        items(city.districts) { districts ->
            var selected by remember { mutableStateOf(false) }
            FilterChip(
                selected = selected,
                onClick = {onDismiss()},
                label = { Text(districts.name) },
                colors =
                FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    selectedContainerColor = (colorResource(R.color.orange_5th)),
                    selectedLabelColor = FColor.Orange_3rd

                ),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RandomFoodPreview() {
//    RandomFood(navController = rememberNavController())
//}