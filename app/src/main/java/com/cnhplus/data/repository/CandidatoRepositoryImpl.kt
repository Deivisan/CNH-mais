package com.cnhplus.data.repository

import com.cnhplus.data.remote.mappers.CandidatoMapper
import com.cnhplus.domain.model.Candidato
import com.cnhplus.domain.model.PacoteCandidato
import com.cnhplus.domain.model.PerfilCandidato
import com.cnhplus.domain.repository.CandidatoRepository as CandidatoRepositoryInterface
import com.cnhplus.network.SupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CandidatoRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : CandidatoRepositoryInterface {

    private val table = "candidatos"

    override suspend fun getCandidato(id: String): Result<Candidato?> {
        return try {
            val result = supabaseClient.getById<com.cnhplus.data.CandidatoDto>(table, id)
            result.map { dto ->
                dto?.let { CandidatoMapper.toDomain(it) }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createCandidato(candidato: Candidato): Result<Candidato> {
        return Result.failure(NotImplementedError("Use DTO diretamente para create"))
    }

    override suspend fun updateCandidato(id: String, fields: Map<String, Any?>): Result<Unit> {
        return supabaseClient.update(table, "id", id, fields)
    }

    override suspend fun updatePerfil(id: String, perfil: PerfilCandidato): Result<Unit> {
        val perfilJson = com.cnhplus.core.di.JsonConfig.default.encodeToString(
            com.cnhplus.data.PerfilCandidato(
                abriuProcesso = perfil.abriuProcesso,
                passouTeorica = perfil.passouTeorica,
                experiencia = perfil.experiencia.name.lowercase(),
                ansiedade = perfil.ansiedade.name.lowercase(),
                reprovou = perfil.reprovou,
                maiorDificuldade = perfil.maiorDificuldade.map { it.name.lowercase() },
                objetivo = perfil.objetivo.name.lowercase(),
                temCarro = perfil.temCarro.name.lowercase(),
                disponibilidade = com.cnhplus.data.DiasTurnos(
                    dias = perfil.disponibilidade.dias.map { it.name.lowercase() },
                    turnos = perfil.disponibilidade.turnos.map { it.name.lowercase() }
                )
            )
        )
        return supabaseClient.update(table, "id", id, mapOf("perfil" to perfilJson))
    }

    override suspend fun updatePacote(id: String, pacote: PacoteCandidato): Result<Unit> {
        val pacoteJson = com.cnhplus.core.di.JsonConfig.default.encodeToString(
            com.cnhplus.data.PacoteCandidato(
                aulasCompradas = pacote.aulasCompradas,
                aulasRestantes = pacote.aulasRestantes,
                aulasRecomendadas = pacote.aulasRecomendadas
            )
        )
        return supabaseClient.update(table, "id", id, mapOf("pacote" to pacoteJson))
    }

    override fun observeCandidato(id: String): Flow<Candidato?> = flow {
        val candidato = getCandidato(id).getOrNull()
        emit(candidato)
    }
}
