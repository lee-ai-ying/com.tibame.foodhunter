package com.tibame.foodhunter.sharon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R


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
    onTabSelected: (Int) -> Unit,
    tabList: List<String>
) {


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

