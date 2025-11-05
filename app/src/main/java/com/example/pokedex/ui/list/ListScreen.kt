package com.example.pokedex.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokedex.ui.components.PokemonCard
import com.example.pokedex.ui.components.TypeDropdown

@Composable
fun ListScreen(
    onOpen: (Int) -> Unit,
    onOpenFav: () -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    // ✅ Lấy toàn bộ UiState từ ViewModel
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pokedex") },
                actions = {
                    IconButton(onClick = onOpenFav) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorites")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {
            // Search — dùng state.query làm source of truth
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = { Text("Search Pokemon...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            // Dropdown hệ — dùng state.type
            TypeDropdown(
                selected = state.type,
                onSelected = viewModel::onTypeChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            if (state.loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
            }

            // Grid 2 cột — KHÔNG dùng items(...) để khỏi cần import extension
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                val data = state.items
                for (p in data) {
                    item(key = p.id) {
                        PokemonCard(
                            p = p,
                            isFavorite = p.id in state.favorites,
                            onOpen = onOpen,
                            onToggleFavorite = { id -> viewModel.toggleFavorite(id) }
                        )
                    }
                }
            }
        }
    }
}
