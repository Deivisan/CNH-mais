package com.cnhplus.data.repository

import com.cnhplus.data.LocalidadeDto
import com.cnhplus.network.SupabaseClient

class LocalidadeRepository(private val client: SupabaseClient) {
    
    fun fetchByAulaId(aulaId: String): Result<LocalidadeDto?> {
        return client.getByColumn("localidades", "aula_id", aulaId)
    }
    
    fun createLocalidade(localidade: LocalidadeDto): Result<LocalidadeDto> {
        return client.insert("localidades", localidade)
    }
    
    fun updateLocalidade(id: String, localidade: LocalidadeDto): Result<Unit> {
        // Note: client.update takes fields map. We'd need a full DTO for patch.
        // For now, use raw approach - this is a known limitation
        throw NotImplementedError("Update requires raw HTTP. Use client directly for now.")
    }
}
