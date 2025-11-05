package com.example.pokedex.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.entities.PokemonEntity
import com.example.pokedex.domain.repo.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repo: PokemonRepository
) : ViewModel() {

    data class UiState(
        val items: List<PokemonEntity> = emptyList(),
        val query: String = "",
        val type: String? = null,
        val loading: Boolean = false,
        val favorites: Set<Int> = emptySet()
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val query = MutableStateFlow("")
    private val type  = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            try { repo.syncPage(offset = 0, limit = 40) }
            finally { _state.update { it.copy(loading = false) } }
        }
        viewModelScope.launch {
            combine(query, type) { q, t -> t to q }
                .flatMapLatest { (t, q) -> repo.filterLocal(t, q) }
                .collect { list -> _state.update { it.copy(items = list) } }
        }
        viewModelScope.launch {
            repo.favorites().collect { favs ->
                _state.update { it.copy(favorites = favs.map { f -> f.pokemonId }.toSet()) }
            }
        }
    }

    fun onQueryChange(q: String) { query.value = q; _state.update { it.copy(query = q) } }
    fun onTypeChange(t: String?) { type.value = t; _state.update { it.copy(type = t) } }

    fun toggleFavorite(id: Int) = viewModelScope.launch {
        val isFav = id in _state.value.favorites
        repo.toggleFavorite(id, isFav)
    }
}
