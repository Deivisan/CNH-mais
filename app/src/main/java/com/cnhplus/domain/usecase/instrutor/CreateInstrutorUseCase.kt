package com.cnhplus.domain.usecase.instrutor

import com.cnhplus.domain.model.Instrutor
import com.cnhplus.domain.repository.InstrutorRepository
import javax.inject.Inject

/**
 * Use case para criar instrutor.
 */
class CreateInstrutorUseCase @Inject constructor(
    private val instrutorRepository: InstrutorRepository
) {
    suspend operator fun invoke(instrutor: Instrutor): Result<Instrutor> {
        return instrutorRepository.createInstrutor(instrutor)
    }
}
