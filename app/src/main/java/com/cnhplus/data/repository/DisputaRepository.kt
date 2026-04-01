package com.cnhplus.data.repository

import com.cnhplus.data.DisputaDto
import com.cnhplus.network.SupabaseClient

class DisputaRepository(
    private val client: SupabaseClient
) {
    private val table = "disputas"

    fun getDisputasByAula(aulaId: String): Result<List<DisputaDto>> {
        return client.get<DisputaDto>(table, mapOf("aula_id" to "eq.$aulaId"))
    }

    fun createDisputa(disputa: DisputaDto): Result<DisputaDto> {
        return client.insert(table, disputa)
    }

    fun updateDisputaStatus(id: String, status: String, decisao: String?): Result<Unit> {
        val fields = buildMap {
            put("status", status)
            if (decisao != null) {
                put("decisao", decisao)
            }
        }
        return client.update(table, "id", id, fields)
    }
}
