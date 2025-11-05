// app/src/main/java/com/example/pokedex/domain/repo/PokemonRepository.kt
package com.example.pokedex.domain.repo

import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.entities.*
import com.example.pokedex.data.remote.api.PokeApi
import com.example.pokedex.data.remote.dto.PokemonDto
import javax.inject.Inject
import java.util.Locale
import kotlinx.coroutines.flow.Flow

class PokemonRepository @Inject constructor(
    private val api: PokeApi,
    private val dao: PokemonDao
) {
    /**
     * Tải 1 trang danh sách và nạp vào Room.
     * Dùng for-loop tuần tự để tránh lỗi "suspend trong map".
     */
    suspend fun syncPage(offset: Int, limit: Int) {
        // 1) Gọi list để lấy các tên
        val page = api.listPokemon(offset, limit)

        // 2) Lấy chi tiết từng con (tuần tự, ổn định)
        val details = mutableListOf<PokemonDto>()
        for (s in page.results) {
            try {
                details += api.getPokemon(s.name)
            } catch (_: Exception) {
                // Bỏ qua lỗi mạng/ID lẻ để không chặn cả trang
            }
        }

        // 3) Map & lưu Room
        val entities = details.map { it.toEntity() }

        val types = details
            .flatMap { d -> d.types?.map { PokemonTypeEntity(it.type.name) } ?: emptyList() }
            .distinctBy { it.name }

        val cross = details
            .flatMap { d -> d.types?.map { PokemonTypeCrossRef(d.id, it.type.name) } ?: emptyList() }

        dao.upsertPokemon(entities)
        dao.insertTypes(types)
        dao.insertCrossRefs(cross)
    }

    fun filterLocal(type: String?, q: String?) = dao.filter(type, q)

    fun favorites() = dao.favorites()

    suspend fun toggleFavorite(id: Int, isFav: Boolean) {
        if (isFav) dao.removeFavorite(id) else dao.addFavorite(FavoriteEntity(id))
    }

    fun getPokemonById(id: Int) = dao.getById(id)

    fun favoritesPokemon() = dao.favoritesJoin()

    fun typesOf(id: Int): Flow<List<String>> = dao.typesOf(id)

    // Dùng khi mở màn chi tiết để lấy abilities + stats online (không ảnh hưởng offline)
    suspend fun fetchDetailRemote(id: Int): PokemonDto = api.getPokemon(id.toString())
}

// ===== Helpers =====

private fun fallbackArtworkById(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

// Ưu tiên official-artwork.front_default → sprites.front_default → fallback theo id
private fun PokemonDto.bestImageUrl(): String =
    this.sprites?.other?.officialArtwork?.frontDefault
        ?: this.sprites?.frontDefault
        ?: fallbackArtworkById(this.id)

private fun String.toTitle(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

private fun PokemonDto.toEntity() = PokemonEntity(
    id = id,
    name = name.toTitle(),
    imageUrl = bestImageUrl(),
    height = height,   // đơn vị dm
    weight = weight    // đơn vị hg
)
