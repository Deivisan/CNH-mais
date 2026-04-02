package com.cnhplus.domain.repository

import com.cnhplus.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface do repositório de autenticação.
 */
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, nome: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): User?
    fun observeAuthState(): Flow<Boolean>
}

/**
 * Interface do repositório de sessão.
 */
interface SessionRepository {
    suspend fun saveSession(token: String, userId: String, role: String)
    suspend fun clearSession()
    suspend fun getAccessToken(): String?
    suspend fun getUserId(): String?
    suspend fun getUserRole(): String?
    fun observeUserRole(): Flow<String?>
}
