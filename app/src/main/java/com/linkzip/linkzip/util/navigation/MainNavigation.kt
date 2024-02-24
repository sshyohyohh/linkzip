package com.linkzip.linkzip.util.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.linkzip.linkzip.data.model.MainScreenState
import com.linkzip.linkzip.data.room.GroupData
import com.linkzip.linkzip.data.room.IconData
import com.linkzip.linkzip.presentation.feature.addgroup.AddGroupView
import com.linkzip.linkzip.presentation.feature.addlink.LinkAddView
import com.linkzip.linkzip.presentation.feature.group.GroupView
import com.linkzip.linkzip.presentation.feature.main.MainView
import com.linkzip.linkzip.presentation.feature.main.MainViewModel
import com.linkzip.linkzip.presentation.feature.onboarding.OnBoardingView
import com.linkzip.linkzip.ui.theme.LinkZipTheme
import com.linkzip.linkzip.util.extention.navigateSingleTopTo

sealed class MainPath(val path: String, var data: Pair<GroupData, IconData>?) {
    object Onboarding : MainPath("Onboarding", null)
    object Main : MainPath("Main", null)
    object GroupAdd : MainPath("GroupAdd", null)
    object LinkAdd : MainPath("LinkAdd", null)
    object Group : MainPath("Group", null)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val screenState by mainViewModel.screenState.collectAsStateWithLifecycle()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    LaunchedEffect(screenState) {
        when (screenState) {
            MainScreenState.ONBOARDING.state -> {
                navController.navigateSingleTopTo(MainPath.Onboarding.path)
            }

            MainScreenState.MAIN.state -> {
                navController.navigateSingleTopTo(MainPath.Main.path)
            }

            MainScreenState.GROUPADD.state -> {
                navController.navigateSingleTopTo(MainPath.GroupAdd.path)
            }

            MainScreenState.LINKADD.state -> {
                navController.navigateSingleTopTo(MainPath.LinkAdd.path)
            }

            MainScreenState.GROUP.state -> {
                MainPath.Group.data = MainScreenState.GROUP.data
                navController.navigateSingleTopTo(MainPath.Group.path)
            }
        }
    }



    NavHost(
        navController = navController,
        startDestination = MainPath.Onboarding.path
    ) {
        composable(MainPath.Onboarding.path) {
            OnBoardingView {
                mainViewModel.updateScreenState(MainScreenState.MAIN.state)
            }
        }
        composable(MainPath.Main.path) {
            val view = LocalView.current
            val window = (view.context as Activity).window
            window.statusBarColor = LinkZipTheme.color.white.toArgb()

            // 그룹 데이터 초기화
            MainPath.GroupAdd.data = null

            MainView(mainViewModel)
        }
        composable(MainPath.GroupAdd.path) {
            AddGroupView(MainPath.GroupAdd.data) {
                mainViewModel.updateScreenState(MainScreenState.MAIN.state)
            }
        }
        composable(MainPath.LinkAdd.path) {
            LinkAddView {
                mainViewModel.updateScreenState(MainScreenState.MAIN.state)
            }
        }
        composable(MainPath.Group.path) {
            GroupView(
                groupData = MainPath.Group.data,
                onBackButtonPressed = {
                mainViewModel.updateScreenState(MainScreenState.MAIN.state)
            }, onActionButtonPressed = {
                    MainPath.GroupAdd.data = MainScreenState.GROUP.data
                mainViewModel.updateScreenState(MainScreenState.GROUPADD.state)
            })
            it.updateState()
        }
    }
}