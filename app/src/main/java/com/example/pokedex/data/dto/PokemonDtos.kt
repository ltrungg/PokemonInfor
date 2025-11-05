//package com.example.pokedex.data.remote.dto
//
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//
//@JsonClass(generateAdapter = true)
//data class PokemonListDto(val count: Int, val results: List<NamedUrl>)
//
//@JsonClass(generateAdapter = true)
//data class NamedUrl(val name: String, val url: String)
//
//@JsonClass(generateAdapter = true)
//data class PokemonDto(
//    val id: Int,
//    val name: String,
//    val height: Int?,
//    val weight: Int?,
//    val abilities: List<AbilityWrapper>?,
//    val types: List<TypeWrapper>?,
//    val stats: List<StatWrapper>?,
//    val sprites: Sprites
//) {
//    @JsonClass(generateAdapter = true) data class AbilityWrapper(val ability: NamedUrl)
//    @JsonClass(generateAdapter = true) data class TypeWrapper(val type: NamedUrl)
//    @JsonClass(generateAdapter = true) data class StatWrapper(@Json(name = "base_stat") val baseStat: Int, val stat: NamedUrl)
//    @JsonClass(generateAdapter = true) data class Sprites(val other: Other?) {
//        @JsonClass(generateAdapter = true) data class Other(@Json(name = "official-artwork") val officialArtwork: OfficialArtwork?)
//        @JsonClass(generateAdapter = true) data class OfficialArtwork(@Json(name = "front_default") val url: String?)
//    }
//}
