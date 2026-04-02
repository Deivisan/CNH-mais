package com.cnhplus.data.repository

import com.cnhplus.domain.model.User
import com.cnhplus.domain.model.UserRole
import com.cnhplus.domain.repository.AuthRepository
import com.cnhplus.domain.repository.SessionRepository
import com.cnhplus.network.SupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val sessionRepository: SessionRepository
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = supabaseClient.signInWithEmail(email, password)
            response.fold(
                onSuccess = { authResponse ->
                    val user = authResponse.user
                    if (user != null && authResponse.access_token != null) {
                        val domainUser = User(
                            id = user.id ?: "",
                            email = user.email ?: email,
                            nome = null,
                            role = UserRole.CANDIDATO // Default, será atualizado depois
                        )
                        sessionRepository.saveSession(
                            authResponse.access_token,
                            user.id ?: "",
                            "candidato"
                        )
                        Result.success(domainUser)
                    } else {
                        Result.failure(Exception("Resposta inválida do servidor"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, nome: String): Result<User> {
        return try {
            val response = supabaseClient.signUpWithEmail(email, password)
            response.fold(
                onSuccess = { authResponse ->
                    val user = authResponse.user
                    if (user != null && authResponse.access_token != null) {
                        val domainUser = User(
                            id = user.id ?: "",
                            email = user.email ?: email,
                            nome = nome,
                            role = UserRole.CANDIDATO
                        )
                        sessionRepository.saveSession(
                            authResponse.access_token,
                            user.id ?: "",
                            "candidato"
                        )
                        Result.success(domainUser)
                    } else {
                        Result.failure(Exception("Registro falhou: token ausente"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val token = sessionRepository.getAccessToken()
            if (token != null) {
                supabaseClient.signOut(token)
            }
            sessionRepository.clearSession()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val userId = sessionRepository.getUserId() ?: return null
        val email = sessionRepository.getAccessToken() ?: return null
        return User(
            id = userId,
            email = email,
            role = UserRole.valueOf(sessionRepository.getUserRole()?.uppercase() ?: "CANDIDATO")
        )
    }

    override fun observeAuthState(): Flow<Boolean> = flow {
        emit(sessionRepository.getAccessToken() != null)
    }
}
