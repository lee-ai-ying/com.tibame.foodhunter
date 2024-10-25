@file:OptIn(ExperimentalMaterial3Api::class)

package com.tibame.foodhunter.ai_ying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R

@Composable
fun GroupMain(
    navController: NavHostController,
    groupVM: GroupViewModel
) {
    val context = LocalContext.current
    var selectTabIndex by remember { mutableIntStateOf(0) }
    val groupChats by groupVM.groupChatFlow.collectAsState()

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
            when (selectTabIndex) {
                0 -> {
                    GroupChatList(groupChats, navController,groupVM)
                }
                1 -> {
                    GroupSearch(navController,groupVM)
                }
            }
        }
    }
    if (selectTabIndex == 0) {
        GroupCreate {
            navController.navigate(context.getString(R.string.str_create_group))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroupMainPreview() {
    MaterialTheme {
        GroupMain(rememberNavController(), viewModel())
    }
}