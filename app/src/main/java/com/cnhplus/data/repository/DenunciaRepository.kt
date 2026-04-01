package com.cnhplus.data.repository

import com.cnhplus.data.DenunciaDto
import com.cnhplus.network.SupabaseClient

class DenunciaRepository(private val client: SupabaseClient) {
    
    fun fetchByAulaId(aulaId: String): Result<List<DenunciaDto>> {
        return client.get("denuncias", mapOf("aula_id" to "eq.$aulaId"))
    }
    
    fun fetchByUserId(userId: String): Result<List<DenunciaDto>> {
        return client.get("denuncias", mapOf("denunciante_id" to "eq.$userId"))
    }
    
    fun createDenuncia(denuncia: DenunciaDto): Result<DenunciaDto> {
        return client.insert("denuncias", denuncia)
    }
}
