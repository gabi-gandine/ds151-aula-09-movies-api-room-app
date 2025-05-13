package com.example.moviesapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.fourMovies
import com.example.moviesapp.ui.moviesapp.MovieDetailsScreen
import com.example.moviesapp.ui.moviesapp.MoviesScreen
import com.example.moviesapp.ui.moviesapp.FavoriteMoviesScreen
import com.example.moviesapp.ui.theme.MoviesAppTheme

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    object MovieList : BottomNavScreen("movie_list", "Filmes", Icons.Filled.List)
    object Favorites : BottomNavScreen("favorites", "Favoritos", Icons.Filled.Favorite)
    companion object { val values = listOf(MovieList, Favorites) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesApp() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomNavRoutes = BottomNavScreen.values.map { it.route }

    Scaffold(
        topBar = {
            val topBarTitle = when {
                currentRoute?.startsWith("movie") == true -> "Filmes"
                currentRoute?.startsWith("favorites") == true -> "Favoritos"
                else -> ""
            }
            val showBack = currentRoute != null && bottomNavRoutes.none { currentRoute == it }

            TopAppBar(
                    title = { Text(text = topBarTitle) },
                    navigationIcon = {
                        if(showBack) {
                            IconButton(
                                onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                        else null
                    },
                )

        },
        bottomBar = {
            NavigationBar {
                BottomNavScreen.values.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = (currentRoute == screen.route),
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.MovieList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.MovieList.route) {
                MoviesScreen(
                    onGoToMovieDetailsClick = { movieId ->
                        navController.navigate("movie_list/$movieId")
                    }
                )
            }
            composable(BottomNavScreen.Favorites.route) {
                FavoriteMoviesScreen(
                    onGoToMovieDetailsClick = { movieId ->
                        navController.navigate("movie_list/$movieId")
                    }
                )
            }
            composable(
                route="movie_list/{movieId}",
                arguments = listOf(
                    navArgument ("movieId") {
                        defaultValue = 0
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val movieId:Int? = backStackEntry.arguments?.getInt("movieId")

                movieId?.let {
                    MovieDetailsScreen(
                        movieId = it,
                        onGoBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MoviesAppPreview(){
    MoviesAppTheme{
        MoviesApp()
    }
}