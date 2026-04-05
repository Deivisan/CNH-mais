package com.cnhplus.data.repository

import com.cnhplus.data.DiasTurnos
import com.cnhplus.data.InstrutorDto
import com.cnhplus.data.Veiculo
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.encodeToString
import com.cnhplus.core.di.JsonConfig

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
        val json = JsonConfig.default.encodeToString(disponibilidade)
        return client.update(table, "id", id, mapOf("disponibilidade" to json))
    }

    fun updateVeiculo(id: String, veiculo: Veiculo): Result<Unit> {
        val json = JsonConfig.default.encodeToString(veiculo)
        return client.update(table, "id", id, mapOf("veiculo" to json))
    }

    /**
     * Upload documento (CNH ou CRLV) para Supabase Storage.
     * @param userId ID do instrutor
     * @param tipo "cnh" ou "crlv"
     * @param fileBytes Bytes do arquivo (imagem ou PDF)
     * @return URL pública do documento
     */
    fun uploadDocumento(
        userId: String,
        tipo: String,
        fileBytes: ByteArray
    ): Result<String> {
        // Validações
        if (fileBytes.isEmpty()) {
            return Result.failure(Exception("Arquivo vazio"))
        }
        
        // Limite de 10MB
        val maxSize = 10 * 1024 * 1024 // 10MB em bytes
        if (fileBytes.size > maxSize) {
            return Result.failure(Exception("Arquivo muito grande (máx 10MB)"))
        }
        
        // Validar tipo
        val tiposValidos = listOf("cnh", "crlv")
        if (tipo !in tiposValidos) {
            return Result.failure(Exception("Tipo de documento inválido: $tipo"))
        }
        
        val bucket = "documentos"
        val filePath = "$userId/$tipo.jpg"
        return client.uploadFile(bucket, filePath, fileBytes, "image/jpeg")
    }
}
