package com.example.pokedex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey val pokemonId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
