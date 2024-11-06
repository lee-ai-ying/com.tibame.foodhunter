package com.tibame.foodhunter.ai_ying

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
    var isShowSearchResult by remember { mutableStateOf(false) }
    //val groupChats by groupVM.groupChat.collectAsState()
    LaunchedEffect(Unit) {
        groupVM.getGroupChatList("1")//TODO:memberId
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GroupTopTabs(selectTabIndex,
            onTabClick1 = {
                selectTabIndex = 0
                isShowSearchResult = false
            },
            onTabClick2 = {
                selectTabIndex = 1
            })
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            when (selectTabIndex) {
                0 -> {
                    GroupChatList(navController, groupVM)//groupChats, navController, groupVM)
                }

                1 -> {
                    if (!isShowSearchResult) {
                        GroupSearch(navController, groupVM) {
                            isShowSearchResult = true
                        }
                    } else {
                        GroupSearchResult(
                            navController,
                            groupVM,
                            onBackClick = {
                                isShowSearchResult = false
                            },
                            onJoinClick = {
                                selectTabIndex = 0
                            }
                        )
                    }
                }
            }
        }
    }
    if (selectTabIndex == 0) {
        GroupCreateButton {
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