package com.cnhplus.data.repository

import com.cnhplus.data.CandidatoDto
import com.cnhplus.data.PacoteCandidato
import com.cnhplus.data.PerfilCandidato
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CandidatoRepository(
    private val client: SupabaseClient
) {
    private val table = "candidatos"

    fun getCandidato(id: String): Result<CandidatoDto?> {
        return client.getById<CandidatoDto>(table, id)
    }

    fun createCandidato(candidato: CandidatoDto): Result<CandidatoDto> {
        return client.insert(table, candidato)
    }

    fun updateCandidato(id: String, fields: Map<String, Any?>): Result<Unit> {
        return client.update(table, "id", id, fields)
    }

    fun updateProfile(id: String, perfil: PerfilCandidato): Result<Unit> {
        val perfilJson = Json { encodeDefaults = true }.encodeToString(perfil)
        return client.update(table, "id", id, mapOf("perfil" to perfilJson))
    }

    fun updatePacote(id: String, pacote: PacoteCandidato): Result<Unit> {
        val pacoteJson = Json { encodeDefaults = true }.encodeToString(pacote)
        return client.update(table, "id", id, mapOf("pacote" to pacoteJson))
    }

    fun getCandidatoByUserId(userId: String): Result<CandidatoDto?> {
        return client.getByColumn<CandidatoDto>(table, "id", userId)
    }
}
