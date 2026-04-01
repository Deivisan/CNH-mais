package com.cnhplus.data.repository

import com.cnhplus.data.MensagemDto
import com.cnhplus.network.SupabaseClient

class MensagemRepository(
    private val client: SupabaseClient
) {
    private val table = "mensagens"

    fun getMensagens(aulaId: String): Result<List<MensagemDto>> {
        return client.get<MensagemDto>(table, mapOf("aula_id" to "eq.$aulaId"))
    }

    fun createMensagem(mensagem: MensagemDto): Result<MensagemDto> {
        return client.insert(table, mensagem)
    }

    fun markAsRead(id: String): Result<Unit> {
        return client.update(table, "id", id, mapOf("lida" to true))
    }
}
