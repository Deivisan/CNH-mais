package com.cnhplus.data.repository

import com.cnhplus.data.DadosBancariosDto
import com.cnhplus.network.SupabaseClient

class DadosBancariosRepository(
    private val client: SupabaseClient
) {
    private val table = "dados_bancarios"

    fun getDadosByInstrutor(instrutorId: String): Result<DadosBancariosDto?> {
        return client.getByColumn<DadosBancariosDto>(table, "instrutor_id", instrutorId)
    }

    fun createOrUpdate(instrutorId: String, dados: DadosBancariosDto): Result<DadosBancariosDto> {
        val existing = getDadosByInstrutor(instrutorId).getOrNull()
        return if (existing != null) {
            val id = existing.id ?: ""
            client.update(table, "id", id, mapOf(
                "instrutor_id" to instrutorId,
                "tipo" to (dados.tipo ?: "pix"),
                "titular" to (dados.titular ?: ""),
                "chave_pix" to dados.chave_pix,
                "banco" to dados.banco,
                "agencia" to dados.agencia,
                "conta" to dados.conta,
                "validado" to false
            )).mapCatching { existing }
        } else {
            val toInsert = dados.copy(instrutor_id = instrutorId)
            client.insert(table, toInsert)
        }
    }
}
