// data/remote/dto/PokemonDetailDto.kt
package com.example.pokedex.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot> = emptyList(),
) {
    @JsonClass(generateAdapter = true)
    data class Sprites(
        @Json(name = "front_default") val frontDefault: String?,
        val other: Other?
    )

    @JsonClass(generateAdapter = true)
    data class Other(
        @Json(name = "official-artwork") val officialArtwork: OfficialArtwork?
    )

    @JsonClass(generateAdapter = true)
    data class OfficialArtwork(
        @Json(name = "front_default") val frontDefault: String?
    )

    @JsonClass(generateAdapter = true)
    data class TypeSlot(
        val slot: Int,
        val type: TypeRef
    ) {
        @JsonClass(generateAdapter = true)
        data class TypeRef(val name: String)
    }
}

// Helper: chọn ảnh “đẹp nhất”
fun PokemonDetailDto.bestImageUrl(): String? =
    sprites.other?.officialArtwork?.frontDefault
        ?: sprites.frontDefault
