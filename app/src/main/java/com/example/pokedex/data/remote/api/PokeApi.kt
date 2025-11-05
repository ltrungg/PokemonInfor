// app/src/main/java/com/example/pokedex/data/remote/api/PokeApi.kt
package com.example.pokedex.data.remote.api

import com.example.pokedex.data.remote.dto.PokemonDto
import com.example.pokedex.data.remote.dto.PokemonPageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    // https://pokeapi.co/api/v2/pokemon?offset=0&limit=40
    @GET("pokemon")
    suspend fun listPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonPageDto

    // https://pokeapi.co/api/v2/pokemon/{nameOrId}
    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") nameOrId: String
    ): PokemonDto
}
