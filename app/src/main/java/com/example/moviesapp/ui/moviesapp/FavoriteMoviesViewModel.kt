package com.example.moviesapp.ui.moviesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.AppContextHolder
import com.example.moviesapp.data.LocalFavoriteMoviesRepositoryProvider
import com.example.moviesapp.data.RemoteMoviesRepositoryProvider
import com.example.moviesapp.model.Movie
import com.example.moviesapp.utils.toFavoriteEntity
import com.example.moviesapp.utils.toMovie
import kotlinx.coroutines.launch
import okio.IOException

sealed interface FavoriteMoviesScreenUiState {
    class Success(val favorites: List<Movie>): FavoriteMoviesScreenUiState
    object Error: FavoriteMoviesScreenUiState
    object Loading: FavoriteMoviesScreenUiState
}

class FavoriteMoviestViewModel(): ViewModel() {
    private val localRepository = LocalFavoriteMoviesRepositoryProvider.getRepository(AppContextHolder.appContext)
    var favoriteMoviesScreenUiState: FavoriteMoviesScreenUiState by mutableStateOf(FavoriteMoviesScreenUiState.Loading)

    init {
        getMovies()
    }

    private fun getMovies(){
        viewModelScope.launch {
            favoriteMoviesScreenUiState = try {
                val favorites = localRepository.getAllFavorites().map { it.toMovie() }
                FavoriteMoviesScreenUiState.Success(favorites = favorites)
            }
            catch(e: IOException){
                FavoriteMoviesScreenUiState.Error
            }
        }
    }

    fun toggleFavorite(movieId:Int) {
        if (favoriteMoviesScreenUiState is FavoriteMoviesScreenUiState.Success) {
            val movieToToggle: Movie? =
                (favoriteMoviesScreenUiState as FavoriteMoviesScreenUiState.Success).favorites.find { movie -> movieId == movie.id }
            movieToToggle?.let {
                viewModelScope.launch {
                    if (localRepository.getFavorite(movieId) != null) {

                        localRepository.removeFavorite(movieToToggle.toFavoriteEntity())

                    } else {
                        localRepository.addFavorite(movieToToggle.toFavoriteEntity())
                    }
                    val favorites = localRepository.getAllFavorites().map { it.toMovie() }
                    favoriteMoviesScreenUiState =
                        FavoriteMoviesScreenUiState.Success(favorites = favorites)
                }
            }
        }
    }
}