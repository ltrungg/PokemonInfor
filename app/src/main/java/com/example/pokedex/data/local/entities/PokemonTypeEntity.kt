package com.example.pokedex.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "type")
data class PokemonTypeEntity(
    @PrimaryKey val name: String
)

@Entity(
    tableName = "pokemon_type",
    primaryKeys = ["pokemonId", "typeName"],
    indices = [Index("typeName"), Index("pokemonId")]
)
data class PokemonTypeCrossRef(
    val pokemonId: Int,
    val typeName: String
)
