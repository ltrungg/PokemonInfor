// app/src/main/java/com/example/pokedex/ui/detail/DetailScreen.kt
package com.example.pokedex.ui.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll // ✅ import thiếu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.pokedex.data.local.entities.FavoriteEntity
import com.example.pokedex.data.local.entities.PokemonEntity
import com.example.pokedex.domain.repo.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.round

@Composable
fun DetailScreen(
    id: Int,
    onBack: () -> Unit,
    vm: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(id) { vm.setId(id) }
    val ui by vm.ui.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ui?.entity?.name ?: "Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val isFav = ui?.isFavorite == true
                    IconButton(onClick = { ui?.entity?.id?.let(vm::toggleFavorite) }) {
                        Icon(
                            if (isFav) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (ui == null || ui?.entity == null) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                Text("Loading…")
                return@Column
            }

            // ✅ ép non-null rõ ràng
            val e: PokemonEntity = ui!!.entity!!

            Text(
                text = "#${e.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = e.name,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(12.dp))

            AsyncImage(
                model = e.imageUrl,
                contentDescription = e.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            if (ui!!.types.isNotEmpty()) {
                Text("Types", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    ui!!.types.forEach { t ->
                        AssistChip(
                            onClick = {},
                            label = { Text(t.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Column(Modifier.weight(1f)) {
                    Text("Height", style = MaterialTheme.typography.titleSmall)
                    Text(e.height?.let { dmToMeterString(it) } ?: "—",
                        style = MaterialTheme.typography.titleMedium)
                }
                Column(Modifier.weight(1f)) {
                    Text("Weight", style = MaterialTheme.typography.titleSmall)
                    Text(e.weight?.let { hgToKgString(it) } ?: "—",
                        style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(Modifier.height(16.dp))

            if (ui!!.abilities.isNotEmpty()) {
                Text("Abilities", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    ui!!.abilities.forEach { ab ->
                        AssistChip(
                            onClick = {},
                            label = { Text(ab) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            if (ui!!.stats.isNotEmpty()) {
                Text("Stats", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                ui!!.stats.forEach { s ->
                    StatRow(label = s.name, value = s.value)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label)
            Text(value.toString())
        }
        val progress = (value.coerceIn(0, 120)) / 120f
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
    }
}

private fun dmToMeterString(dm: Int): String {
    val m = dm / 10f
    return "${trim1(m)} m"
}

private fun hgToKgString(hg: Int): String {
    val kg = hg / 10f
    return "${trim1(kg)} kg"
}

private fun trim1(v: Float): String {
    val r = round(v * 10f) / 10f
    return if (r % 1.0f == 0f) r.toInt().toString() else r.toString()
}

data class StatItem(val name: String, val value: Int)

data class DetailUi(
    val entity: PokemonEntity? = null,
    val types: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val abilities: List<String> = emptyList(),
    val stats: List<StatItem> = emptyList()
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: PokemonRepository,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val idKey = "id"
    private val _ui = MutableStateFlow(DetailUi())
    val ui: StateFlow<DetailUi> = _ui.asStateFlow()

    fun setId(id: Int) {
        if (handle.get<Int>(idKey) != id) handle[idKey] = id
    }

    init {
        val idFlow = handle.getStateFlow(idKey, 0)

        // ✅ tách Flows để suy luận kiểu rõ ràng
        val entityFlow: Flow<PokemonEntity?> = idFlow.flatMapLatest { repo.getPokemonById(it) }
        val typesFlow: Flow<List<String>> = idFlow.flatMapLatest { repo.typesOf(it) }
        val favsFlow: Flow<List<FavoriteEntity>> = repo.favorites()

        viewModelScope.launch {
            combine(entityFlow, typesFlow, favsFlow) { entity, types, favs ->
                val favSet = favs.map { it.pokemonId }.toSet()
                DetailUi(
                    entity = entity,
                    types = types,
                    isFavorite = entity?.id?.let { id -> id in favSet } == true,
                    abilities = _ui.value.abilities, // giữ nguyên khi chưa fetch
                    stats = _ui.value.stats
                )
            }.collect { _ui.value = it }
        }

        // fetch abilities + stats từ API
        viewModelScope.launch {
            idFlow.collectLatest { id ->
                if (id == 0) return@collectLatest
                runCatching { repo.fetchDetailRemote(id) }
                    .onSuccess { dto ->
                        val abilities = dto.abilities?.map { it.ability.name }
                            ?.map { s -> s.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase() else c.toString() } }
                            ?: emptyList()

                        val stats = dto.stats?.map { entry ->
                            StatItem(
                                name = entry.stat.name.replace('-', ' ')
                                    .replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase() else c.toString() },
                                value = entry.baseStat
                            )
                        } ?: emptyList()

                        _ui.update { it.copy(abilities = abilities, stats = stats) }
                    }
            }
        }
    }

    fun toggleFavorite(id: Int) = viewModelScope.launch {
        val nowFav = ui.value.isFavorite
        repo.toggleFavorite(id, nowFav)
    }
}
