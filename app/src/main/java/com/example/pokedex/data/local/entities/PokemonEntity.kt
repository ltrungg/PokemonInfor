// app/src/main/java/com/example/pokedex/data/local/entities/PokemonEntity.kt
package com.example.pokedex.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon", indices = [Index("name")])
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String?,   // URL ảnh đã được fill ở Repository
    val height: Int?,
    val weight: Int?
)
