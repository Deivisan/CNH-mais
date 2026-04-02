package com.cnhplus.domain.usecase.candidato

import com.cnhplus.domain.model.Candidato
import com.cnhplus.domain.repository.CandidatoRepository
import javax.inject.Inject

/**
 * Use case para obter dados do candidato.
 */
class GetCandidatoUseCase @Inject constructor(
    private val candidatoRepository: CandidatoRepository
) {
    suspend operator fun invoke(id: String): Result<Candidato?> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID do candidato não pode ser vazio"))
        }
        return candidatoRepository.getCandidato(id)
    }
}
