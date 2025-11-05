// app/src/main/java/com/example/pokedex/ui/components/PokemonCard.kt
package com.example.pokedex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.pokedex.data.local.entities.PokemonEntity
import com.google.accompanist.placeholder.material3.placeholder

// Fallback phòng khi DB chưa có imageUrl
private fun officialArtworkFromId(id: Int) =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

@Composable
fun PokemonCard(
    p: PokemonEntity,
    isFavorite: Boolean,
    onOpen: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onOpen(p.id) }
    ) {
        var loading by remember { mutableStateOf(true) }

        val urlToLoad = p.imageUrl?.ifBlank { null } ?: officialArtworkFromId(p.id)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(urlToLoad)
                .crossfade(true)
                .build(),
            contentDescription = p.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .placeholder(visible = loading),
            contentScale = ContentScale.Fit,
            onSuccess = { loading = false },
            onError = { loading = false }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "#${p.id.toString().padStart(3, '0')}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = p.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { onToggleFavorite(p.id) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}
