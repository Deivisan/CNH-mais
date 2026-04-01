package com.cnhplus.data.repository

import com.cnhplus.data.AulaDto
import com.cnhplus.network.SupabaseClient

class AulaRepository(
    private val client: SupabaseClient
) {
    private val table = "aulas"

    fun getAulasByCandidato(candidatoId: String): Result<List<AulaDto>> {
        return client.get<AulaDto>(table, mapOf("candidato_id" to "eq.$candidatoId"))
    }

    fun getAulasByInstrutor(instrutorId: String): Result<List<AulaDto>> {
        return client.get<AulaDto>(table, mapOf("instrutor_id" to "eq.$instrutorId"))
    }

    fun getAulaById(id: String): Result<AulaDto?> {
        return client.getById<AulaDto>(table, id)
    }

    fun createAula(aula: AulaDto): Result<AulaDto> {
        return client.insert(table, aula)
    }

    fun updateAula(id: String, fields: Map<String, Any?>): Result<Unit> {
        return client.update(table, "id", id, fields)
    }

    fun confirmCandidato(id: String): Result<Unit> {
        return client.update(table, "id", id, mapOf("confirmacao_candidato" to true))
    }

    fun confirmInstrutor(id: String): Result<Unit> {
        return client.update(table, "id", id, mapOf("confirmacao_instrutor" to true))
    }

    fun cancelAula(id: String): Result<Unit> {
        return client.update(table, "id", id, mapOf("status" to "cancelada"))
    }

    fun completeAula(id: String): Result<Unit> {
        return client.update(table, "id", id, mapOf("status" to "concluida"))
    }
}
