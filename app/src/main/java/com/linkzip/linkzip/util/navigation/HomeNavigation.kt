package com.linkzip.linkzip.util.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.linkzip.linkzip.data.model.HomeScreenState
import com.linkzip.linkzip.presentation.feature.home.HomeViewModel
import com.linkzip.linkzip.presentation.feature.home.all.AllView
import com.linkzip.linkzip.presentation.feature.home.favorite.FavoriteView


sealed class HomePath(val path: String) {
    object All : HomePath("All")
    object Favorite : HomePath("Favorite")
}

@Composable
fun HomeNavigation(
    homeViewModel: HomeViewModel =  hiltViewModel()
){
    val navController = rememberNavController()
    val screenState by homeViewModel.homeScreenState.collectAsState(initial = HomeScreenState.ALL)

    LaunchedEffect(screenState) {
        when(screenState) {
            HomeScreenState.ALL -> {navController.navigate(HomePath.All.path) }
            HomeScreenState.FAVORITE -> { navController.navigate(HomePath.Favorite.path) }
        }
    }


    NavHost(
        navController =  navController,
        startDestination = HomePath.All.path){
        composable(HomePath.All.path) {
            AllView()
        }
        composable(HomePath.Favorite.path) {
            FavoriteView()
        }
    }
}