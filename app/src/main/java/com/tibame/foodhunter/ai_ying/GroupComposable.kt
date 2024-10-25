package com.tibame.foodhunter.ai_ying

import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tibame.foodhunter.R
import java.nio.file.WatchEvent
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupPriceSlider() {
    var lowNumber by remember { mutableIntStateOf(1) }
    var highNumber by remember { mutableIntStateOf(2000) }
    val rangeSliderState = remember {
        RangeSliderState(
            1f,
            2000f,
            valueRange = 1f..2000f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
            steps = 9,
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy((-12).dp)
    ) {
        lowNumber = rangeSliderState.activeRangeStart.roundToInt()
        highNumber = rangeSliderState.activeRangeEnd.roundToInt()

        Text(
            "$${if (lowNumber > 1 && lowNumber % 10 == 1) lowNumber - 1 else lowNumber}-$" +
                    "${if (highNumber > 1 && highNumber % 10 == 1) highNumber - 1 else highNumber}" +
                    (if (highNumber >= 2000) "以上" else "")
        )
        RangeSlider(
            state = rangeSliderState,
            enabled = true
        )
    }
}

@Composable
fun GroupSelectMember() {
    val input by remember { mutableIntStateOf(1) }
    OutlinedTextField(
        value = "參加人數:${input}",
        onValueChange = {

        },
        trailingIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                // 可設定該元件點擊時要執行指定程式
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDropDownMenu(options: List<String>) {
//    val options = listOf("Large", "Medium", "Small")
    var expanded by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(options[0]) }
    var selectedText by remember { mutableStateOf("") }
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        // 是否彈出下拉選單
        expanded = expanded,
        // 點擊下拉選單且展開狀態改變時呼叫，會傳入目前是否為展開狀態
        onExpandedChange = { expanded = it }
    ) {
        // 使用TextField以外元件會造成無法正常顯示下拉選單
        TextField(
            // 設為true則無法輸入
            readOnly = true,
            // 正確設定TextField與ExposedDropdownMenu對應位置。enabled為true方可展開下拉選單
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = inputText,
            // 當使用者修改文字值時呼叫
            onValueChange = {
                inputText = it
                // 值改變時會彈出選項
                expanded = true
            },
            singleLine = true,
            // 末端顯示下箭頭圖示
            trailingIcon = { TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        ExposedDropdownMenu(
            // 設定是否彈出下拉選單
            expanded = expanded,
            // 點擊下拉選單外部時
            onDismissRequest = { expanded = false }
        ) {
            // 下拉選單內容由DropdownMenuItem選項元件組成
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    // 點選項目後呼叫
                    onClick = {
                        // 將所有文字都改成選取項目文字
                        selectedText = option
                        inputText = option
                        // 將狀態設定為收回下拉選單
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun GroupText(text: String) {
    Column(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text)
    }
}

@Composable
fun GroupSingleInput() {
    GroupTextInputField(
        modifier = Modifier.fillMaxWidth(),
        placeholder = {},
        trailingIcon = {},
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun GroupSingleWithIcon(trailingIcon: @Composable () -> Unit) {
    GroupTextInputField(
        modifier = Modifier.fillMaxWidth(),
        placeholder = {},
        trailingIcon = trailingIcon,
        singleLine = true,
        maxLines = 1,
        readOnly = true
    )
}@Composable
fun GroupSingleInputWithIcon(
    placeholder:@Composable () -> Unit,
    trailingIcon: @Composable () -> Unit) {
    GroupTextInputField(
        modifier = Modifier.fillMaxWidth(),
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun GroupBigInput(maxLines: Int) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GroupTextInputField(
            modifier = Modifier
//                .weight(1f)
                .fillMaxWidth(),
            placeholder = {},
            trailingIcon = {},
            singleLine = false,
            maxLines = maxLines
        )
    }
}

@Composable
fun GroupTextInputField(
    modifier: Modifier,
    placeholder: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    singleLine: Boolean,
    maxLines: Int,
    readOnly: Boolean = false
) {
    var input by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        readOnly = readOnly,
        modifier = modifier,
        value = input,
        onValueChange = {
            input = it
        },
        placeholder = {
            placeholder()
        },
        trailingIcon = {
            if (input.isBlank()) {
                trailingIcon()

            } else {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear",
                    // 可設定該元件點擊時要執行指定程式
                    modifier = Modifier.clickable {
                        input = ""
                        focusManager.clearFocus()
                    }
                )
            }
        },
        singleLine = singleLine,
        maxLines = maxLines
    )
}

@Composable
fun GroupCreate(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = {
            Icon(
                Icons.Outlined.Edit,
                contentDescription = "" // Add a valid content description
            )
        },
        text = { Text(stringResource(R.string.str_create_group)) },
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomEnd)
            .offset((-20).dp, (-20).dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTopTabs(selectedTabIndex: Int, onTabClick1: () -> Unit, onTabClick2: () -> Unit) {
    PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = onTabClick1,
            text = { Text(text = stringResource(R.string.str_my_group)) }
        )
        Tab(
            selected = selectedTabIndex == 1,
            onClick = onTabClick2,
            text = { Text(text = stringResource(R.string.str_search_group)) }
        )
    }
}

@Composable
fun GroupSearchBar(onValueChange: (String) -> String, onClearClick: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var isTextFieldFocus by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = input,
        onValueChange = {
            input = onValueChange(it)
        },
        placeholder = { Text(text = "") },
        // 設定開頭圖示
        leadingIcon = {
            if (isTextFieldFocus && input.isNotBlank()) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "search",
                    modifier = Modifier.clickable {
                        isTextFieldFocus = false
                        focusManager.clearFocus()
                    }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            }
        },
        trailingIcon = {
            if (input.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear",
                    // 可設定該元件點擊時要執行指定程式
                    modifier = Modifier.clickable {
                        input = ""
                        onClearClick()
                    }
                )
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isTextFieldFocus = it.isFocused
            },
        shape = RoundedCornerShape(0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GroupSearchBarPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            GroupTextInputField({Text(text="111")},{})
//            GroupSingleInput()
//            GroupSearchBar(
//                onValueChange = { it },
//                onClearClick = { "" }
//            )
//            GroupDropDownMenu(listOf("Large", "Medium", "Small"))
//            GroupSelectMember()
//            GroupPriceSlider()
            GroupBigInput(5)
        }
    }
}