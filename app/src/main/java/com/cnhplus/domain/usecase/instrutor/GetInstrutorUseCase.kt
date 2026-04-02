package com.cnhplus.domain.usecase.instrutor

import com.cnhplus.domain.model.Instrutor
import com.cnhplus.domain.repository.InstrutorRepository
import javax.inject.Inject

/**
 * Use case para obter dados do instrutor.
 */
class GetInstrutorUseCase @Inject constructor(
    private val instrutorRepository: InstrutorRepository
) {
    suspend operator fun invoke(id: String): Result<Instrutor?> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID do instrutor não pode ser vazio"))
        }
        return instrutorRepository.getInstrutor(id)
    }
}
