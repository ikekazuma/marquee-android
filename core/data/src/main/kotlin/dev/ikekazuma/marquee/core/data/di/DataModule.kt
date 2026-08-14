package dev.ikekazuma.marquee.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ikekazuma.marquee.core.data.DefaultMovieRepository
import dev.ikekazuma.marquee.core.data.MovieRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: DefaultMovieRepository): MovieRepository
}
