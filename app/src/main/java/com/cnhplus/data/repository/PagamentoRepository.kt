package com.cnhplus.data.repository

import com.cnhplus.data.PagamentoDto
import com.cnhplus.network.SupabaseClient

class PagamentoRepository(
    private val client: SupabaseClient
) {
    private val table = "pagamentos"

    fun getPagamentosByCandidato(candidatoId: String): Result<List<PagamentoDto>> {
        return client.get<PagamentoDto>(table, mapOf("candidato_id" to "eq.$candidatoId"))
    }

    fun createPagamento(pagamento: PagamentoDto): Result<PagamentoDto> {
        return client.insert(table, pagamento)
    }

    fun updatePagamentoStatus(id: String, status: String): Result<Unit> {
        return client.update(table, "id", id, mapOf("status" to status))
    }
}
