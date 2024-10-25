package com.tibame.foodhunter.sharon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.checkTopBarBackButtonShow
import com.tibame.foodhunter.checkTopBarNoShow
import com.tibame.foodhunter.global.TopFunctionBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsideTopBar(
    canback:Boolean = true,
    navController: NavHostController? = null,
    scrollBehavior: TopAppBarScrollBehavior
){
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(stringResource(R.string.str_member))
        },
        navigationIcon = {
            if (canback) {
                IconButton(onClick = {
                    navController?.popBackStack()
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
                    Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.str_notice)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.str_chat)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote1(
    navController: NavHostController = rememberNavController(), // 這裡創建或接收 NavController，用於控制導航
//    callback: @Composable () -> Unit // 接收一個可組合的回調函數，用於在頁面中展示額外的 UI
) {
    var phone by remember { mutableStateOf("") }
    val destination = navController.currentBackStackEntryAsState().value?.destination
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            InsideTopBar(true, navController, scrollBehavior)
        },

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.LightGray)
        ) {

            // 輸入的文字對齊末端
            TextField(
                value = phone,
                // 輸入的值改變時呼叫此方法
                onValueChange = { phone = it },
                // 提示文字
                label = { Text(text = "Phone number") },
                // 輸入的文字對齊末端
                textStyle = TextStyle(textAlign = TextAlign.End),
                singleLine = true,
                // 鍵盤種類為電話
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Text(
                text = "$phone",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun NiaTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        text = {
            val style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(
                value = style,
                content = {
                    Box(modifier = Modifier.padding(top = NiaTabDefaults.TabTopPadding)) {
                        text()
                    }
                },
            )
        },
    )
}

@Composable
fun NiaTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        indicator = { tabPositions ->
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 2.dp,
            )
        },
        tabs = tabs,
    )
}

/** 日曆、手札、收藏的切換 **/
@Composable
fun TabBarComponent(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabList = listOf(
        stringResource(id = R.string.str_calendar),
        stringResource(id = R.string.str_note),
        stringResource(id = R.string.str_favorite)
    )

    NiaTabRow(selectedTabIndex = selectedTab) {
        tabList.forEachIndexed { index, title ->
            NiaTab(
                text = { Text(text = title) },
                selected = selectedTab == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}






// 呼叫並預覽
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TabsPreview() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val mockNavController = rememberNavController()

    MaterialTheme {
        AddNote1(mockNavController)
        val titles = listOf("Topics", "People")
        NiaTabRow(selectedTabIndex = selectedTab) {
            titles.forEachIndexed { index, title ->
                NiaTab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title) },
                )
            }
        }

    }
}

// 修改全部tab的風格樣式
object NiaTabDefaults {
    val TabTopPadding = 7.dp
}

