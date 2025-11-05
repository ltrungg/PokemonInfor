package com.example.pokedex.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.entities.*

@Database(
    entities = [
        PokemonEntity::class,
        PokemonTypeEntity::class,
        PokemonTypeCrossRef::class,
        FavoriteEntity::class
    ],
    version = 2,                 // ⬅️ tăng từ 1 lên 2
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
