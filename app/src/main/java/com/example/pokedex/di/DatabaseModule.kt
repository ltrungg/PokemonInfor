package com.example.pokedex.di

import android.content.Context
import androidx.room.Room
import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "pokedex.db")
            .fallbackToDestructiveMigration()   // ⬅️ phá DB cũ khi version đổi
            .build()

    @Provides
    fun providePokemonDao(db: AppDatabase): PokemonDao = db.pokemonDao()
}
