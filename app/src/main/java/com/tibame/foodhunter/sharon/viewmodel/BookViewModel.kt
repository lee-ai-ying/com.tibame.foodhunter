package com.tibame.foodhunter.sharon.viewmodel

import androidx.lifecycle.ViewModel
import com.tibame.foodhunter.sharon.data.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate



class BookViewModel : ViewModel() {
    // MutableStateFlow 用來監控書籍列表，當資料改變時通知 UI 更新
    private val _bookState = MutableStateFlow(emptyList<Book>())
    val bookState: StateFlow<List<Book>> = _bookState.asStateFlow()

    // 初始化時載入測試資料
    init {
        _bookState.update { fetchBooks() }
    }

    /** 新增一本書到書籍列表，並更新 _bookState 的內容 */
    fun addItem(item: Book) {
        _bookState.update {
            val books = it.toMutableList()
            books.add(item)
            books
        }
    }

    /** 從書籍列表中移除一本書，並更新 _bookState 的內容 */
    fun removeItem(item: Book) {
        _bookState.update {
            val books = it.toMutableList()
            books.remove(item)
            books
        }
    }

    /**
     * 產生測試書籍資料，每本書都有對應的日期
     * @return 多本書籍資訊
     */
    private fun fetchBooks(): List<Book> {
        return listOf(
            Book("0001", "Android App", 600.0, android.R.drawable.ic_menu_camera, LocalDate.now().minusDays(3)),
            Book("0002", "iOS App", 650.0, android.R.drawable.ic_menu_gallery, LocalDate.now().minusDays(2)),
            Book("0003", "MySQL DB", 550.0, android.R.drawable.ic_menu_manage, LocalDate.now().minusDays(1)),
            Book("0004", "Python", 500.0, android.R.drawable.ic_menu_compass, LocalDate.now()),
            Book("0005", "Kotlin", 520.0, android.R.drawable.ic_menu_mapmode, LocalDate.now().plusDays(1)),
            Book("0006", "MongoDB", 630.0, android.R.drawable.ic_menu_search, LocalDate.now().plusDays(2)),
            Book("0007", "C/C++", 420.0, android.R.drawable.ic_menu_info_details, LocalDate.now().plusDays(3)),
            Book("0008", "C#", 480.0, android.R.drawable.ic_menu_help, LocalDate.now().plusDays(4)),
            Book("0009", "Swift", 540.0, android.R.drawable.ic_menu_share, LocalDate.now().plusDays(5)),
            Book("0010", "Java", 520.0, android.R.drawable.ic_menu_agenda, LocalDate.now().plusDays(6)),
            Book("0011", "SQL Server", 620.0, android.R.drawable.ic_menu_camera, LocalDate.now().plusDays(7)),
            Book("0012", "Oracle DB", 680.0, android.R.drawable.ic_menu_gallery, LocalDate.now().plusDays(8)),
            Book("0013", "HTML", 400.0, android.R.drawable.ic_menu_manage, LocalDate.now().plusDays(9)),
            Book("0014", "Java Script", 470.0, android.R.drawable.ic_menu_compass, LocalDate.now().plusDays(10))
        )
    }
}
