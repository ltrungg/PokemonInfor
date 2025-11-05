package com.example.pokedex.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

private val POKEMON_TYPES = listOf(
    "All Types",
    "Bug","Dragon","Electric","Fairy","Fighting","Fire","Flying","Ghost",
    "Grass","Ground","Ice","Normal","Poison","Psychic","Rock","Steel","Water","Dark"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(
    selected: String?,                // null hoặc tên hệ
    onSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val label = selected ?: "All Types"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = label,
            onValueChange = {},
            label = { Text("Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            POKEMON_TYPES.forEach { t ->
                DropdownMenuItem(
                    text = { Text(t) },
                    onClick = {
                        expanded = false
                        onSelected(if (t == "All Types") null else t)
                    }
                )
            }
        }
    }
}
