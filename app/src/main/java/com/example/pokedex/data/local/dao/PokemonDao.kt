package com.example.pokedex.data.local.dao

import androidx.room.*
import com.example.pokedex.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun page(offset: Int, limit: Int): List<PokemonEntity>

    @Query("""
        SELECT * FROM pokemon
        WHERE LOWER(name) LIKE '%' || LOWER(:q) || '%'
        ORDER BY id ASC
    """)
    fun search(q: String): Flow<List<PokemonEntity>>

    @Query("""
        SELECT p.*
        FROM pokemon AS p
        LEFT JOIN pokemon_type AS pt ON pt.pokemonId = p.id
        WHERE
            (:type IS NULL OR :type = '' OR LOWER(pt.typeName) = LOWER(:type))
        AND
            (:q IS NULL OR :q = '' OR LOWER(p.name) LIKE '%' || LOWER(:q) || '%')
        GROUP BY p.id
        ORDER BY p.id ASC
    """)
    fun filter(type: String?, q: String?): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPokemon(list: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTypes(types: List<PokemonTypeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRefs(x: List<PokemonTypeCrossRef>)

    // ===== Detail & Favorites =====
    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun countPokemon(): Int

    @Query("SELECT typeName FROM pokemon_type WHERE pokemonId = :id")
    fun typesOf(id: Int): Flow<List<String>>

    @Query("SELECT * FROM pokemon WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<PokemonEntity?>

    @Query("""
        SELECT p.*
        FROM pokemon p
        INNER JOIN favorite f ON f.pokemonId = p.id
        ORDER BY f.createdAt DESC
    """)
    fun favoritesJoin(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM favorite ORDER BY createdAt DESC")
    fun favorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(f: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE pokemonId = :id")
    suspend fun removeFavorite(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE pokemonId = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}
