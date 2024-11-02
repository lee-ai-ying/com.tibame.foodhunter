package com.tibame.foodhunter

import NewPost
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tibame.foodhunter.a871208s.AddFriendScreen
import com.tibame.foodhunter.a871208s.DeleteMemberScreen
import com.tibame.foodhunter.a871208s.ForgetPassword1Screen
import com.tibame.foodhunter.a871208s.ForgetPassword2Screen
import com.tibame.foodhunter.a871208s.FriendManagementScreen
import com.tibame.foodhunter.a871208s.FriendViewModel
import com.tibame.foodhunter.a871208s.LoginScreen
import com.tibame.foodhunter.a871208s.MemberInformationScreen
import com.tibame.foodhunter.a871208s.MemberMainScreen
import com.tibame.foodhunter.a871208s.ModifyInformationScreen
import com.tibame.foodhunter.a871208s.OtherSettingScreen
import com.tibame.foodhunter.a871208s.PrivateChatRoom
import com.tibame.foodhunter.a871208s.PrivateChatRoomBottomBar
import com.tibame.foodhunter.a871208s.PrivateChatRoomTopBar
import com.tibame.foodhunter.a871208s.PrivateChatScreen
import com.tibame.foodhunter.a871208s.PrivateViewModel
import com.tibame.foodhunter.a871208s.RegisterScreen
import com.tibame.foodhunter.a871208s.UserViewModel

import com.tibame.foodhunter.global.*
import com.tibame.foodhunter.ai_ying.*
import com.tibame.foodhunter.andysearch.RandomFood
import com.tibame.foodhunter.andysearch.SearchResult
import com.tibame.foodhunter.sharon.PersonalToolsScreen

import com.tibame.foodhunter.zoe.Home

import com.tibame.foodhunter.andysearch.SearchScreen
import com.tibame.foodhunter.sharon.NoteEdit
import com.tibame.foodhunter.andysearch.SearchScreenVM
import com.tibame.foodhunter.sharon.NoteEditRoute
import com.tibame.foodhunter.sharon.NoteEditTopBar
import com.tibame.foodhunter.sharon.TabConstants
import com.tibame.foodhunter.sharon.components.topbar.CalendarTopBar
import com.tibame.foodhunter.sharon.components.topbar.NoteTopBar
import com.tibame.foodhunter.sharon.viewmodel.CalendarViewModel
import com.tibame.foodhunter.sharon.viewmodel.NoteViewModel
import com.tibame.foodhunter.wei.RestaurantDetail
import com.tibame.foodhunter.zoe.PostDetailScreen
import com.tibame.foodhunter.zoe.PostViewModel
import com.tibame.foodhunter.zoe.SearchPost


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Main()
            }
        }
    }
}

/** 將 **不顯示** TopBar的route寫進list裡 */
@Composable
fun checkTopBarNoShow(destination: NavDestination?): Boolean {
    val context = LocalContext.current
    return !listOf(
        "",
        context.getString(R.string.str_login),
        context.getString(R.string.str_login) + "/2",
        context.getString(R.string.str_login) + "/3",
        context.getString(R.string.str_login) + "/4",
    ).contains(destination?.route)
}

/** 將 **要顯示** BackButton的route寫進list裡 */
@Composable
fun checkTopBarBackButtonShow(destination: NavDestination?): Boolean {
    val context = LocalContext.current
    return listOf(
        context.getString(R.string.str_create_group),
        context.getString(R.string.SearchToGoogleMap) + "/{id}",
        context.getString(R.string.randomFood),
        "PrivateChatRoom/{roomid}",
        "GroupChatRoom/{groupId}",
        context.getString(R.string.str_group) + "/2",
        context.getString(R.string.str_member) + "/2",
        context.getString(R.string.str_member) + "/3",
        context.getString(R.string.str_member) + "/4",
        context.getString(R.string.str_member) + "/5",
        context.getString(R.string.str_member) + "/6",
        context.getString(R.string.str_member) + "/7",
        context.getString(R.string.str_member) + "/8",
        context.getString(R.string.restaurantDetail),
        "postDetail/{postId}"
    ).contains(destination?.route)
}

/** 將 **要顯示** BottomBar的route寫進list裡 */
@Composable
fun checkBottomButtonShow(destination: NavDestination?): Boolean {
    val context = LocalContext.current
    return listOf(
        context.getString(R.string.str_Recommended_posts),
        context.getString(R.string.str_searchpost),
        context.getString(R.string.str_search),
        context.getString(R.string.str_post),
        context.getString(R.string.str_group),
        context.getString(R.string.str_member),
        "GroupChatRoom/{groupId}",
        "PrivateChatRoom/{roomid}",
        context.getString(R.string.SearchToGoogleMap) + "/{restId}",
        context.getString(R.string.randomFood),
        context.getString(R.string.str_create_group),
        "postDetail/{postId}"
    ).contains(destination?.route)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    navController: NavHostController = rememberNavController(),
    gChatVM: GroupViewModel = viewModel(),
    searchVM: SearchScreenVM = viewModel(),
    friendVM: FriendViewModel = viewModel(),
    pChatVM: PrivateViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var currectScene by remember { mutableStateOf(context.getString(R.string.str_login)) }
    val destination = navController.currentBackStackEntryAsState().value?.destination


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (destination?.route == "GroupChatRoom/{groupId}") {
                GroupChatRoomTopBar(
                    navController,
                    TopAppBarDefaults.pinnedScrollBehavior(),
                    gChatVM
                )
                return@Scaffold
            }
            if (destination?.route == "PrivateChatRoom/{roomid}") {
                PrivateChatRoomTopBar(
                    navController,
                    TopAppBarDefaults.pinnedScrollBehavior(),
                    pChatVM
                )
                return@Scaffold
            }
            if (checkTopBarNoShow(destination)) {
                TopFunctionBar(
                    checkTopBarBackButtonShow(destination),
                    navController,
                    scrollBehavior
                )
            }
            if (destination?.parent?.route == "personal_tools") {
                when (destination.route) {
                    "note_edit" -> NoteEditTopBar(

                    )
                    context.getString(R.string.str_calendar) -> CalendarTopBar(
                        navController,
                        scrollBehavior,
                        CalendarViewModel()
                    )
                    context.getString(R.string.str_note) -> NoteTopBar(
                        navController,
                        scrollBehavior,
                        NoteViewModel()
                    )

                }
            }
        },
        bottomBar = {
            if (destination?.route == "GroupChatRoom/{groupId}") {
                GroupChatRoomBottomBar(navController,gChatVM)
                return@Scaffold
            }
            if (destination?.route == "PrivateChatRoom/{roomId}") {
                PrivateChatRoomBottomBar(pChatVM)
                return@Scaffold
            }
            if (checkBottomButtonShow(destination)) {
                BottomFunctionBar(
                    onHomeClick = {
                        currectScene = context.getString(R.string.str_Recommended_posts)
                    },
                    onSearchClick = {
                        currectScene = context.getString(R.string.str_search)
                    },
                    onPostClick = {
                        currectScene = context.getString(R.string.str_post)
                    },
                    onGroupClick = {
                        currectScene = context.getString(R.string.str_group)
                    },
                    onMemberClick = {
                        currectScene = context.getString(R.string.str_member)
                    },
                    selectScene = currectScene
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            navController = navController,
            startDestination = currectScene
        ) {

            composable(context.getString(R.string.str_login)) {
                LoginScreen(navController = navController,userViewModel)
            }
            composable(context.getString(R.string.str_login) + "/2") {
                RegisterScreen(navController = navController)
            }
            composable(context.getString(R.string.str_login) + "/3") {
                ForgetPassword1Screen(navController = navController, {})
            }
            composable(context.getString(R.string.str_login) + "/4") {
                ForgetPassword2Screen(navController = navController, {})
            }
            composable(context.getString(R.string.str_Recommended_posts)) {
                Home(navController, 0)
            }
            composable(context.getString(R.string.str_searchpost)) {
                Home(navController, 1)
            }
            composable(
                "postDetail/{postId}",
                arguments = listOf(navArgument("postId") { type = NavType.IntType })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("postId")
                PostDetailScreen(
                    postId = postId,
                    navController = navController,
                    postViewModel = postViewModel
                )
            }




























            composable(context.getString(R.string.str_post)) {
                NewPost(navController = navController,  postViewModel)
            }
            composable(context.getString(R.string.str_searchpost)) {
                SearchPost(navController)
            }

            composable(context.getString(R.string.str_search)) {
                SearchScreen(navController, searchVM)
            }
            composable("${context.getString(R.string.SearchToGoogleMap)}/{restId}",
                arguments = listOf(
                    navArgument("restId") { type = NavType.IntType }
                )
            ) {
                SearchResult(
                    navController = navController,
                    restaurantID = it.arguments?.getInt("restId") ?: -1,
                    searchTextVM = searchVM
                )
            }
            composable(context.getString(R.string.randomFood)) {
                RandomFood(
                    navController = navController, searchTextVM = searchVM
                )
            }

            composable(context.getString(R.string.restaurantDetail)){
                RestaurantDetail(navController = navController, restaurantVM = searchVM)
            }











            composable(context.getString(R.string.str_group)) {
                GroupMain(navController, gChatVM)
            }
            composable(context.getString(R.string.str_create_group)) {
                GroupCreate(navController, gChatVM)
            }
            composable("GroupChatRoom/{groupId}",
                arguments = listOf(
                    navArgument("groupId") { type = NavType.IntType }
                )
            ) {
                GroupChatRoom(it.arguments?.getInt("groupId") ?: -1, gChatVM)//,gChatRoomVM)
            }


            composable("PrivateChatRoom/{roomid}",
                arguments = listOf(
                    navArgument("roomid") { type = NavType.StringType }
                )
            ) {
                PrivateChatRoom(it.arguments?.getString("roomid") ?: "-1", pChatVM)//,gChatRoomVM)
            }







            composable(context.getString(R.string.str_member)) {
                MemberMainScreen(navController = navController)
            }

            composable(context.getString(R.string.str_member) + "/2") {
                MemberInformationScreen(navController = navController)
            }
            composable(context.getString(R.string.str_member) + "/3") {
                ModifyInformationScreen(navController = navController)
            }
            composable(context.getString(R.string.str_member) + "/4") {
                DeleteMemberScreen(navController = navController)
            }
            composable(context.getString(R.string.str_member) + "/5") {
                OtherSettingScreen(navController = navController)
            }
            composable(context.getString(R.string.str_member) + "/6") {
                FriendManagementScreen(navController = navController, friendVM)
            }
            composable(context.getString(R.string.str_member) + "/7") {
                AddFriendScreen(navController = navController)
            }
            composable(context.getString(R.string.str_member) + "/8") {
                PrivateChatScreen(navController = navController,pChatVM)
            }


            navigation(
                startDestination = context.getString((R.string.str_calendar)),
                route = "personal_tools"
            ) {
                composable(context.getString(R.string.str_calendar)) {
                    PersonalToolsScreen(navController, TabConstants.CALENDAR)
                }
                composable(context.getString(R.string.str_note)) {
                    PersonalToolsScreen(navController, TabConstants.NOTE)
                }
                composable(context.getString(R.string.str_favorite)) {
                    PersonalToolsScreen(navController, TabConstants.FAVORITE)
                }

                composable(
                    route = "note_edit/{noteId}",
                    arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("id")
                        ?: return@composable  // 防止空值

                    NoteEditRoute(navController = navController, noteId = noteId)
                }
            }

        }
    }


}


@Preview(showBackground = true)
@Composable
fun FoodHunterPreview() {
    MaterialTheme {
        Main()
    }
}