package com.tibame.foodhunter.sharon

import Roboto
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.components.ColorBox
import com.tibame.foodhunter.sharon.components.DateBox
import com.tibame.foodhunter.sharon.components.GroupTextContent
import com.tibame.foodhunter.sharon.components.GroupVisibilityToggle

@Preview(showBackground = true)
@Composable
fun CalendarGroupCardPreview() {
    CalendarGroupItem()
}

@Composable
fun CalendarGroupItem(
    navController: NavHostController = rememberNavController(),
    circleColor: Color = colorResource(id = R.color.orange_5th),
    boxColor: Color = colorResource(id = R.color.orange_1st),
) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .width(380.dp)
            .height(88.dp)
            .border(width = 2.dp, color = Color(0xFFEBEBEB), shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // 黃色:筆記、橘色:揪團
            ColorBox(color = boxColor)
            // 日期 跟 底圖顏色
            DateBox(
                date = "10/17",
                day = "星期四",
                color = circleColor,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            // 標題、內文。傳遞 .weight(1f) 保持填補Row空間
            GroupTextContent(
                title = "說好的減肥呢",
                restaurantName = "麥噹噹",
                restaurantAddress = "台北市中山區南京東路三段222號",
                headcount = 4
            )
            GroupVisibilityToggle(isPublic = true)
        }
    }
}




