package com.example.pokedex.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun TypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val c = TypeColors.colorOf(label)
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(if (label == "All Types") label else label.replaceFirstChar { it.uppercase() }) },
        shape = RoundedCornerShape(999.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = c.copy(alpha = 0.18f),
            selectedLabelColor = c,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
