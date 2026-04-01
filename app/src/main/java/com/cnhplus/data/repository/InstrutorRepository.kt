package com.cnhplus.data.repository

import com.cnhplus.data.DiasTurnos
import com.cnhplus.data.InstrutorDto
import com.cnhplus.data.Veiculo
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class InstrutorRepository(
    private val client: SupabaseClient
) {
    private val table = "instrutores"

    fun getInstrutor(id: String): Result<InstrutorDto?> {
        return client.getById<InstrutorDto>(table, id)
    }

    fun getActiveInstrutores(): Result<List<InstrutorDto>> {
        return client.get<InstrutorDto>(table, mapOf("status" to "eq.ativo"))
    }

    fun getAllInstrutores(): Result<List<InstrutorDto>> {
        return client.get<InstrutorDto>(table, emptyMap())
    }

    fun createInstrutor(instrutor: InstrutorDto): Result<InstrutorDto> {
        return client.insert(table, instrutor)
    }

    fun updateInstrutor(id: String, fields: Map<String, Any?>): Result<Unit> {
        return client.update(table, "id", id, fields)
    }

    fun updateDisponibilidade(id: String, disponibilidade: DiasTurnos): Result<Unit> {
        val json = Json { encodeDefaults = true }.encodeToString(disponibilidade)
        return client.update(table, "id", id, mapOf("disponibilidade" to json))
    }

    fun updateVeiculo(id: String, veiculo: Veiculo): Result<Unit> {
        val json = Json { encodeDefaults = true }.encodeToString(veiculo)
        return client.update(table, "id", id, mapOf("veiculo" to json))
    }
}
