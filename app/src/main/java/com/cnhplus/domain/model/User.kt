package com.cnhplus.domain.model

/**
 * Domain model para User/Profile genérico.
 */
data class User(
    val id: String,
    val email: String,
    val nome: String? = null,
    val role: UserRole = UserRole.CANDIDATO,
    val fotoUrl: String? = null,
    val telefone: String? = null,
    val createdAt: String? = null
)

enum class UserRole { CANDIDATO, INSTRUTOR, ADMIN }
