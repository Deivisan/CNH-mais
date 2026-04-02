package com.cnhplus.core.di

import com.cnhplus.data.local.SessionDataStore
import com.cnhplus.data.repository.AuthRepositoryImpl
import com.cnhplus.data.repository.CandidatoRepositoryImpl
import com.cnhplus.data.repository.InstrutorRepositoryImpl
import com.cnhplus.domain.repository.AuthRepository
import com.cnhplus.domain.repository.CandidatoRepository
import com.cnhplus.domain.repository.InstrutorRepository
import com.cnhplus.domain.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DI Module para repositórios.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        impl: SessionDataStore
    ): SessionRepository

    @Binds
    @Singleton
    abstract fun bindCandidatoRepository(
        impl: CandidatoRepositoryImpl
    ): CandidatoRepository

    @Binds
    @Singleton
    abstract fun bindInstrutorRepository(
        impl: InstrutorRepositoryImpl
    ): InstrutorRepository
}
