@file:OptIn(ExperimentalMaterial3Api::class)

package com.tibame.foodhunter.ai_ying

import android.graphics.drawable.shapes.Shape
import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.Main
import com.tibame.foodhunter.R

@Composable
fun GroupMain(
    navController: NavHostController = rememberNavController(),
    groupViewModel: GroupViewModel = viewModel()
) {
    var selectTabIndex by remember { mutableIntStateOf(0) }
    val groupChats by groupViewModel.groupChatFlow.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GroupTopTabs(selectTabIndex,
            onTabClick1 = {
                selectTabIndex = 0
            },
            onTabClick2 = {
                selectTabIndex = 1
            })
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            GroupSearchBar()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(groupChats) { groupChat ->
                    if (groupChat.groupState==99){
                        Text(
                            modifier = Modifier.height(40.dp).fillMaxWidth(),
                            text = groupChat.groupName
                        )
                    }
                    else{
                        Row(
                            modifier = Modifier.height(56.dp).fillMaxWidth().background(Color.Yellow).clickable {
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = groupChat.groupName
                            )
                        }
                    }

                }
            }
        }
    }
}

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
fun GroupSearchBar() {
    var input by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        placeholder = { Text(text = "") },
        // 設定開頭圖示
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "password"
            )
        },
        trailingIcon = {
            if (!input.isEmpty()) {
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
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp)
    )
}

@Composable
fun Group1(
    groupViewModel: GroupViewModel = viewModel()
) {
    val groupChats by groupViewModel.groupChatFlow.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(groupChats) { groupChat ->
            if (groupChat.groupState==99){
                Text(
                    modifier = Modifier.height(40.dp).fillMaxWidth(),
                    text = groupChat.groupName
                )
            }
            else{
                Row(
                    modifier = Modifier.height(56.dp).fillMaxWidth().background(Color.Yellow).clickable {
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = groupChat.groupName
                    )
                }
            }

        }
    }

}

@Composable
fun Group2(
    navController: NavHostController = rememberNavController(),
    callback: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
        Text(text = "Group2")
        callback()
    }
}

@Preview(showBackground = true)
@Composable
fun FoodHunterPreview() {
    MaterialTheme {
        GroupMain()
    }
}