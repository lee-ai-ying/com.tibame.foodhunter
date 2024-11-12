package com.tibame.foodhunter.wei



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FColor
import java.lang.reflect.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.ImageBitmap
import java.security.Timestamp


data class ReviewCreateData(
    var reviewId: Int = 0,
    var reviewer: String = "",
    var rating: Int = 0,
    var content: String = "",
    var location: String = "",
    var timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var serviceCharge: Int = 0,
)

data class Reviews(
    var reviewId: Int,
    var reviewer: Reviewer,
    var restaurantId: Int,
    var rating: Int = 0,
    var content: String,
    var timestamp: String,
    var thumbsup: Int?, //從資料庫中得到的累積鑽數
    var thumbsdown: Int?,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false,
    var replies: List<Reply>,
    var maxPrice: Int,
    var minPrice: Int,
    var serviceCharge:Int = 0,
)

data class Reviewer(
    val id: Int,
    val name: String,
    val avatarImage: ImageBitmap? = null,
    val followers: Int = 0, // 追蹤者人數
    val following: Int = 0  // 追蹤中人數
)






