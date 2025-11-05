package com.example.pokedex.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.pokedex.data.local.entities.PokemonEntity
import com.example.pokedex.domain.repo.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@Composable
fun FavoritesScreen(
    onOpen: (Int) -> Unit,
    onBack: () -> Unit,
    vm: FavoritesViewModel = hiltViewModel()
) {
    val items by vm.items.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { p ->
        if (items.isEmpty()) {
            Column(Modifier.padding(p).padding(24.dp)) {
                Text("Chưa có yêu thích nào.")
            }
        } else {
            LazyColumn(Modifier.padding(p).fillMaxSize()) {
                items(items) { pkm ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { onOpen(pkm.id) }
                    ) {
                        Row(Modifier.padding(12.dp)) {
                            AsyncImage(
                                model = pkm.imageUrl,
                                contentDescription = pkm.name,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(pkm.name, style = MaterialTheme.typography.titleMedium)
                                Text("#${pkm.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    repo: PokemonRepository
) : ViewModel() {
    val items = repo
        .favoritesPokemon()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<PokemonEntity>())
}
