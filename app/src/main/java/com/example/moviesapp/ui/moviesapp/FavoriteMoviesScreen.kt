package com.example.moviesapp.ui.moviesapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviesapp.model.fourMovies
import com.example.moviesapp.ui.theme.MoviesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteMoviesScreen(
    favoriteMoviesViewModel: FavoriteMoviestViewModel = viewModel(),
    onGoToMovieDetailsClick: (movieId:Int) -> Unit = {},
) {
    val favoriteMoviesScreenUiState = favoriteMoviesViewModel.favoriteMoviesScreenUiState

    Column() {
        when(favoriteMoviesScreenUiState) {
            is FavoriteMoviesScreenUiState.Success -> {
                FavoriteMoviesList(
                    favorites = favoriteMoviesScreenUiState.favorites,
                    onFavoriteClick = { movieId -> favoriteMoviesViewModel.toggleFavorite(movieId) },
                    onMovieClick = { movieId -> onGoToMovieDetailsClick(movieId) }
                )
            }
            is FavoriteMoviesScreenUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is FavoriteMoviesScreenUiState.Error -> ErrorScreen( modifier = Modifier.fillMaxSize())
        }
    }
}