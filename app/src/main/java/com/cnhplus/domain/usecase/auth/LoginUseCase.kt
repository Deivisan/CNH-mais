package com.cnhplus.domain.usecase.auth

import com.cnhplus.domain.model.User
import com.cnhplus.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case para login de usuário.
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email e senha são obrigatórios"))
        }
        return authRepository.login(email, password)
    }
}
