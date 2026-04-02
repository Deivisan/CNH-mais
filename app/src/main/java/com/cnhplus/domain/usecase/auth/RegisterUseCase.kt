package com.cnhplus.domain.usecase.auth

import com.cnhplus.domain.model.User
import com.cnhplus.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case para registro de usuário.
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, nome: String): Result<User> {
        if (email.isBlank() || password.isBlank() || nome.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos os campos são obrigatórios"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Senha deve ter pelo menos 6 caracteres"))
        }
        return authRepository.register(email, password, nome)
    }
}
