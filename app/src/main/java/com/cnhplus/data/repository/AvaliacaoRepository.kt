package com.cnhplus.data.repository

import com.cnhplus.data.AvaliacaoDto
import com.cnhplus.network.SupabaseClient

class AvaliacaoRepository(
    private val client: SupabaseClient
) {
    private val table = "avaliacoes"

    fun getAvaliacoesByInstrutor(instrutorId: String): Result<List<AvaliacaoDto>> {
        return client.get<AvaliacaoDto>(table, mapOf("instrutor_id" to "eq.$instrutorId"))
    }

    fun createAvaliacao(avaliacao: AvaliacaoDto): Result<AvaliacaoDto> {
        return client.insert(table, avaliacao)
    }
}
