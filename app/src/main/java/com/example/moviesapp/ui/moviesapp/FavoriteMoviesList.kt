package com.example.moviesapp.ui.moviesapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.fourMovies
import com.example.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun FavoriteMoviesList(
    onMovieClick: (Int) -> Unit = {},
    onFavoriteClick: (Int) -> Unit = {},
    favorites: List<Movie> = emptyList<Movie>()
){
    LazyColumn(){
        items(favorites) { movie ->
            MovieItem(
                onTitleClick = { onMovieClick(movie.id) },
                onFavoriteClick = { onFavoriteClick(movie.id) },
                movie = movie,
                isFavorite = favorites.find{favorite -> favorite.id == movie.id} != null
            )
            HorizontalDivider(modifier = Modifier.height(2.dp).padding(horizontal = 5.dp))
        }
    }
}