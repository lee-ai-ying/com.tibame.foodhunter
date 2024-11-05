package com.tibame.foodhunter.ai_ying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FColor
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun GroupCreate(
    navController: NavHostController,
    gChatVM: GroupViewModel
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val inputData by remember { mutableStateOf(GroupCreateData()) }
    var selectDate by remember { mutableStateOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(1) {
            //*
            GroupTitleText(text = stringResource(R.string.str_create_group))
            Column (
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            {
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
                    readOnly = true,
                    placeholder = {
                        Text(inputData.time)
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
                GroupPriceSlider { min,max->
                    inputData.priceMin = min
                    inputData.priceMax = max
                }
                GroupText(text = stringResource(R.string.str_create_member))
                GroupSelectMember {
                    inputData.joinMember = it.toString()
                }
                GroupText(text = stringResource(R.string.str_create_public))
                GroupDropDownMenu(listOf("public", "invite", "private")) {
                    inputData.isPublic = it
                }
                GroupText(text = stringResource(R.string.str_create_describe))
                GroupBigInput(5) {
                    inputData.describe = it
                }
                //*/
                Spacer(modifier = Modifier.size(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            gChatVM.createGroup(inputData)
                            //navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FColor.Orange_1st
                        )
                    ) {
                        Text("確定")
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
            }

        }
    }
    Box {
        if (showDatePickerDialog) {
            MyDatePickerDialog(
                //showData = { inputData.time },
                onDismissRequest = {
                    showDatePickerDialog = false
                },
                // 確定時會接收到選取日期
                onConfirm = { utcTimeMillis ->
                    selectDate = utcTimeMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                            .toLocalDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))?:selectDate
                            //.toLocalDate().format(ofLocalizedDate(FormatStyle.MEDIUM))
                    }
                    inputData.time=selectDate
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
fun MyLocationPicker(){

}

@Preview(showBackground = true)
@Composable
fun GroupCreatePreview() {
    MaterialTheme {
        GroupCreate(rememberNavController(),viewModel())
//        MyDatePickerDialog(
//            onDismissRequest = {
//            },
//            // 確定時會接收到選取日期
//            onConfirm = {
//            },
//            // 設定取消時欲執行內容
//            onDismiss = {
//            }
//        )
    }
}