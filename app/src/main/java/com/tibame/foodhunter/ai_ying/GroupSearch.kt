package com.tibame.foodhunter.ai_ying

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GroupSearch(
    navController: NavHostController,
    gChatVM: GroupViewModel,
    onSearchClick: () -> Unit = {}
) {
    val tags = listOf(
        "aaa",
        "bbbb",
        "cc",
        "dddddd",
        "eeeeeeee",
        "fffff",
        "gggg",
        "h"
    )
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectDate by remember {
        mutableStateOf(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        )
    }
    val inputData by remember { mutableStateOf(GroupSearchData()) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(1) {
            //*
            GroupTitleText(text = stringResource(R.string.str_search_group))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                GroupText(text = stringResource(R.string.str_create_name))
                GroupSingleInput {
                    inputData.name = it
                }
                GroupText(text = stringResource(R.string.str_create_location))
                GroupSingleWithIcon(
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = ""
                        )
                    }
                ) {
                    inputData.location = it
                }
                GroupText(text = stringResource(R.string.str_create_time))
                GroupSingleInputWithIcon(
                    placeholder = {
                        Text(selectDate)
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                showDatePickerDialog = true
                            }
                        )
                    }
                ) {
                    inputData.time = it
                }
                GroupText(text = stringResource(R.string.str_create_price))
                GroupPriceSlider {
                    inputData.price = it
                }
                Spacer(modifier = Modifier.size(8.dp))
                GroupText(text = stringResource(R.string.str_search_tags))
                FlowRow(
                    modifier = Modifier
                        .border(1.dp, Color.LightGray)
                        .background(Color.White)
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach {//.shuffled().forEach {
                        FoodLabel(it) {
                            inputData.tags.toMutableList().add(it)
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                gChatVM.setGroupSearchData(inputData)
                                onSearchClick()
                            },
                        ) {
                            Text("搜尋")
                        }
                        Button(
                            onClick = {

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text("清空", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }

            }

        }
    }
    Box {
        if (showDatePickerDialog) {
            MyDatePickerDialog(
                showData = selectDate,
                onDismissRequest = {
                    showDatePickerDialog = false
                },
                // 確定時會接收到選取日期
                onConfirm = { utcTimeMillis ->
                    selectDate = utcTimeMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                            .toLocalDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
//                            .toLocalDate().format(ofLocalizedDate(FormatStyle.MEDIUM))
                    }
                    showDatePickerDialog = false
                },
                // 設定取消時欲執行內容
                onDismiss = {
                    showDatePickerDialog = false
                }
            )
        }
    }
}

@Composable
fun FoodLabel(text: String, onClick: () -> Unit) {
    var selected by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.clickable {
            selected = !selected
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .background(
                    when (selected) {
                        true -> MaterialTheme.colorScheme.primary//colorResource(R.color.orange_1st)
                        false -> MaterialTheme.colorScheme.primaryContainer//colorResource(R.color.orange_4th)
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(top = 8.dp, end = 16.dp, start = 8.dp, bottom = 8.dp)
        ) {
            Icon(
                when (selected) {
                    true -> Icons.Outlined.Check
                    false -> Icons.Outlined.Add
                },
                contentDescription = "",
                tint = when (selected) {
                    true -> Color.White
                    false -> Color.Black
                }
            )
            Text(
                text = text,
                color = when (selected) {
                    true -> Color.White
                    false -> Color.Black
                }
            )
        }
    }

//    TextField(
//        value = text,
//        onValueChange = {
//        },
//        modifier = Modifier.clipToBounds(),
//        shape = RoundedCornerShape(12.dp),
//        colors = TextFieldDefaults.colors(
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent
//        ),
//        readOnly = true
//    )
}

@Preview(showBackground = true)
@Composable
fun GroupSearchPreview() {
    MaterialTheme {
        GroupSearch(rememberNavController(), viewModel())
    }
}