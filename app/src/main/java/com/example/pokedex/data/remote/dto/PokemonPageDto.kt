package com.example.pokedex.data.remote.dto

// Response của: https://pokeapi.co/api/v2/pokemon?offset=0&limit=40
data class PokemonPageDto(
    val count: Int,
    val next: String?,          // có thể null ở trang cuối
    val previous: String?,      // có thể null ở trang đầu
    val results: List<PokemonSummaryDto>
)

// Mỗi item trong "results"
data class PokemonSummaryDto(
    val name: String,           // ví dụ: "bulbasaur"
    val url: String             // ví dụ: "https://pokeapi.co/api/v2/pokemon/1/"
)
