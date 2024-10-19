@file:OptIn(ExperimentalMaterial3Api::class)

package com.tibame.foodhunter.global

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.tibame.foodhunter.R

@Composable
fun TopFunctionBar(canback:Boolean=false,navController: NavHostController){
    TopAppBar(
        title = {
            Text(stringResource(R.string.app_name))
        },
        navigationIcon = {
            if (canback) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.str_back)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.str_notice)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.MailOutline,
                    contentDescription = stringResource(R.string.str_chat)
                )
            }
        }
    )
}

@Composable
fun BottomFunctionBar(
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPostClick: () -> Unit,
    onGroupClick: () -> Unit,
    onMemberClick: () -> Unit,
    selectScene:String
) {

    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = stringResource(id = R.string.str_home)
                )
            },
            label = {
                Text(stringResource(id = R.string.str_home))
            },
            selected = selectScene==stringResource(id = R.string.str_home),
            onClick = {
                onHomeClick()
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.str_search)
                )
            },
            label = {
                Text(stringResource(id = R.string.str_search))
            },
            selected = selectScene==stringResource(id = R.string.str_search),
            onClick = {
                onSearchClick()
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = stringResource(id = R.string.str_post)
                )
            },
            label = {
                Text(stringResource(id = R.string.str_post))
            },
            selected = selectScene==stringResource(id = R.string.str_post),
            onClick = {
                onPostClick()
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Outlined.DateRange,
                    contentDescription = stringResource(id = R.string.str_group)
                )
            },
            label = {
                Text(stringResource(id = R.string.str_group))
            },
            selected = selectScene == stringResource(id = R.string.str_group),
            onClick = {
                onGroupClick()
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = stringResource(id = R.string.str_member)
                )
            },
            label = {
                Text(stringResource(id = R.string.str_member))
            },
            selected = selectScene == stringResource(id = R.string.str_member),
            onClick = {
                onMemberClick()
            }
        )
    }
}
