package com.tibame.foodhunter.zoe

import com.tibame.foodhunter.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PostRepository {
    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList.asStateFlow()

    // 创建一些示例用户
    private val samplePublishers = listOf(
        Publisher(
            id = "user1",
            name = "John Doe",
            avatarImage = R.drawable.user1,
            joinDate = "2024-01-01"
        ),
        Publisher(
            id = "user2",
            name = "Mary Johnson",
            avatarImage = R.drawable.user2,
            joinDate = "2024-01-15"
        ),
        Publisher(
            id = "user3",
            name = "Sarah Chen",
            avatarImage = R.drawable.user3,
            joinDate = "2024-02-01"
        )
    )

    init {
        // 初始化示例数据
        _postList.value = listOf(
            Post(
                postId = "1",
                publisher = samplePublishers[0],  // 使用 Publisher 对象
                content = "這是一個美味的早午餐！推薦大家來試試。",
                location = "台北市信義區",
                timestamp = "2024-03-15 10:30",
                postTag = "早午餐",
                carouselItems = listOf(
                    CarouselItem(0, R.drawable.sushi_image_1, "Breakfast image 1"),
                    CarouselItem(1, R.drawable.breakfast_image_2, "Breakfast image 2")
                ),
                comments = listOf(
                    Comment(
                        id = "c1",
                        commenter = Commenter(
                            id = "user2",
                            name = "Jane Smith",
                            avatarImage = R.drawable.user2
                        ),
                        content = "看起來好好吃！",
                        timestamp = "2024-03-15 11:00"
                    )
                )
            ),
            Post(
                postId = "2",
                publisher = samplePublishers[1],
                content = "今天的午餐很特別，是米其林餐廳的特製料理。",
                location = "台北市大安區",
                timestamp = "2024-03-15 13:30",
                postTag = "午餐",
                carouselItems = listOf(
                    CarouselItem(0, R.drawable.steak_image, "Breakfast image 1"),
                    CarouselItem(1, R.drawable.breakfast_image_2, "Breakfast image 2")
                ),
                comments = listOf(
                    Comment(
                        id = "c2",
                        commenter = Commenter(
                            id = "user4",
                            name = "Bob Wilson",
                            avatarImage = R.drawable.user4
                        ),
                        content = "看起來超高級的！",
                        timestamp = "2024-03-15 14:00"
                    )
                )
            ),
            Post(
                postId = "3",
                publisher = samplePublishers[2],
                content = "晚餐約會的好去處，氣氛很好！",
                location = "台北市中山區",
                timestamp = "2024-03-15 19:30",
                postTag = "晚餐",
                carouselItems = listOf(
                    CarouselItem(0, R.drawable.breakfast_image_1, "Breakfast image 1"),
                    CarouselItem(1, R.drawable.breakfast_image_2, "Breakfast image 2")
                ),
                comments = listOf(
                    Comment(
                        id = "c3",
                        commenter = Commenter(
                            id = "user6",
                            name = "David Lee",
                            avatarImage = R.drawable.user1
                        ),
                        content = "謝謝推薦，我也想去試試！",
                        timestamp = "2024-03-15 20:00"
                    )
                )
            )
        )
    }

    // Repository 方法
    fun addPost(post: Post) {
        val currentList = _postList.value.toMutableList()
        currentList.add(0, post)
        _postList.value = currentList
    }

    fun updatePost(post: Post) {
        val currentList = _postList.value.toMutableList()
        val index = currentList.indexOfFirst { it.postId == post.postId }
        if (index != -1) {
            currentList[index] = post
            _postList.value = currentList
        }
    }

    fun deletePost(postId: String) {
        val currentList = _postList.value.toMutableList()
        currentList.removeAll { it.postId == postId }
        _postList.value = currentList
    }

    fun getPublisherById(publisherId: String): Publisher? {
        return samplePublishers.find { it.id == publisherId }
    }
}