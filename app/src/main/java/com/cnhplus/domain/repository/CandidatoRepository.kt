package com.cnhplus.domain.repository

import com.cnhplus.domain.model.Candidato
import kotlinx.coroutines.flow.Flow

/**
 * Interface do repositório de candidatos.
 */
interface CandidatoRepository {
    suspend fun getCandidato(id: String): Result<Candidato?>
    suspend fun createCandidato(candidato: Candidato): Result<Candidato>
    suspend fun updateCandidato(id: String, fields: Map<String, Any?>): Result<Unit>
    suspend fun updatePerfil(id: String, perfil: com.cnhplus.domain.model.PerfilCandidato): Result<Unit>
    suspend fun updatePacote(id: String, pacote: com.cnhplus.domain.model.PacoteCandidato): Result<Unit>
    fun observeCandidato(id: String): Flow<Candidato?>
}
