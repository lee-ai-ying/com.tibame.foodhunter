package com.tibame.foodhunter.sharon.components.card

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tibame.foodhunter.sharon.data.CardContentType

@Composable
fun CardContent(
    type: CardContentType,
    color: Color,
    date: String,
    day: String,
    circleColor: Color,
    title: String,
    modifier: Modifier = Modifier,
    // Group 特有參數
    restaurantName: String? = null,
    restaurantAddress: String? = null,
    headcount: Int? = null,
    isPublic: Boolean? = null,
    // Note 特有參數
    noteContent: String? = null,
    imageResId: Int? = null
) {
    Row(modifier = modifier.fillMaxSize()) {
        ColorBox(color = color)

        DateBox(
            date = date,
            day = day,
            color = circleColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        when (type) {
            CardContentType.GROUP -> {
                GroupTextContent(
                    title = title,
                    restaurantName = restaurantName ?: "",
                    restaurantAddress = restaurantAddress ?: "",
                    headcount = headcount ?: 0,
                )
                // 揪團才需要顯示公開/私密切換
                isPublic?.let {
                    GroupVisibilityToggle(isPublic = it)
                }
            }

            CardContentType.NOTE -> {
                NoteTextContent(
                    title = title,
                    content = noteContent ?: "",
                    modifier = Modifier.weight(1f)
                )
                // 筆記才需要顯示圖片
                imageResId?.let {
                    NoteImage(imageResId = it)
                }
            }
        }
    }
}

