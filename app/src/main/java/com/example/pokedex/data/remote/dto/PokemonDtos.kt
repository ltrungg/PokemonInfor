// app/src/main/java/com/example/pokedex/data/remote/dto/PokemonDto.kt
package com.example.pokedex.data.remote.dto

import com.squareup.moshi.Json

data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int?,
    val weight: Int?,
    val sprites: Sprites?,
    val types: List<TypeSlot>?,
    val abilities: List<AbilitySlot>?,   // ✅ thêm
    val stats: List<StatEntry>?          // ✅ thêm
)

data class Sprites(
    @Json(name = "front_default") val frontDefault: String?,
    val other: OtherSprites?
)

data class OtherSprites(
    @Json(name = "official-artwork") val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    @Json(name = "front_default") val frontDefault: String?
)

data class TypeSlot(
    val slot: Int?,
    val type: NamedApiResource
)

data class AbilitySlot(
    val ability: NamedApiResource
)

data class StatEntry(
    @Json(name = "base_stat") val baseStat: Int,
    val stat: NamedApiResource
)

data class NamedApiResource(
    val name: String,
    val url: String?
)
