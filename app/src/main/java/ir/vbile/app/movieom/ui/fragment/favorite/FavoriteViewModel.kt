package ir.vbile.app.movieom.ui.fragment.favorite

import androidx.hilt.lifecycle.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.ui.base.*

class FavoriteViewModel @ViewModelInject constructor(private val repository: MovieRepository) : BaseViewModel() {
    fun getAllFavorite() = repository.getAllFavorite()
}