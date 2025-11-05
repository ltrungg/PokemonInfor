//package com.example.pokedex.di
//
//import android.content.Context
//import coil3.ImageLoader
//import coil3.disk.DiskCache
//import coil3.memory.MemoryCache
//import coil3.network.okhttp.OkHttpNetworkFetcher
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object CoilModule {
//
//    @Provides @Singleton
//    fun provideImageLoader(@ApplicationContext ctx: Context): ImageLoader =
//        ImageLoader.Builder(ctx)
//            .components { add(OkHttpNetworkFetcher.Factory()) }
//            .memoryCache { MemoryCache.Builder(ctx).maxSizePercent(0.25).build() }
//            .diskCache { DiskCache.Builder().directory(ctx.cacheDir.resolve("image_cache")).maxSizeBytes(128L * 1024 * 1024).build() }
//            .crossfade(true)
//            .build()
//}
