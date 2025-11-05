package com.example.pokedex.di

import com.example.pokedex.data.remote.api.PokeApi
import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.domain.repo.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton
    fun provideRepo(api: PokeApi, dao: PokemonDao): PokemonRepository = PokemonRepository(api, dao)
}
