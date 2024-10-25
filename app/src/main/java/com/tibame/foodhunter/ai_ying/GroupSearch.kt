package com.tibame.foodhunter.ai_ying

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDefaults.DatePickerHeadline
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofLocalizedDate
import java.time.format.FormatStyle


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GroupSearch(
    navController: NavHostController,
    gChatVM: GroupViewModel
) {
    val tags = listOf(
        "一",
        "二二",
        "三三三",
        "四四四四",
        "五五五五五",
        "六六六六六六",
        "七七七七七七七",
        "八八八八八八八八"
    )
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectDate by remember {
        mutableStateOf(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(1) {
            //*
            GroupText(text = stringResource(R.string.str_search_group))
            GroupText(text = stringResource(R.string.str_create_name))
            GroupSingleInput()
            GroupText(text = stringResource(R.string.str_create_location))
            GroupSingleWithIcon {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = ""
                )
            }
            GroupText(text = stringResource(R.string.str_create_time))
            GroupSingleInputWithIcon(
                placeholder = {
                    Text(selectDate)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        showDatePickerDialog = true
                    }
                )
            }
            GroupText(text = stringResource(R.string.str_create_price))
            GroupPriceSlider()
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
                tags.shuffled().forEach {
                    FoodLabel(it)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {},
                ) {
                    Text("搜尋")
                }
                Button(
                    onClick = {},
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

@Composable
fun FoodLabel(text: String) {
    var selected by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.clickable {
            selected = !selected
        }
    ) {
        Row(
            modifier = Modifier
                .background(
                    when (selected) {
                        true -> colorResource(R.color.orange_1st)
                        false -> colorResource(R.color.orange_4th)
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