package com.cnhplus.data.repository

import com.cnhplus.data.DenunciaDto
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.json.*

class DenunciaRepository(private val client: SupabaseClient) {
    suspend fun fetchByAulaId(aulaId: String): Result<List<DenunciaDto>> = runCatching {
        val response = client.getRequest(
            path = "rest/v1/denuncias?aula_id=eq.$aulaId",
            headers = mapOf("Accept" to "application/json")
        )
        
        if (response.statusCode == 200) {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<List<DenunciaDto>>(response.body)
        } else {
            throw Exception("Failed to fetch denuncias: ${response.statusCode}")
        }
    }
    
    suspend fun fetchByUserId(userId: String): Result<List<DenunciaDto>> = runCatching {
        val response = client.getRequest(
            path = "rest/v1/denuncias?denunciante_id=eq.$userId",
            headers = mapOf("Accept" to "application/json")
        )
        
        if (response.statusCode == 200) {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<List<DenunciaDto>>(response.body)
        } else {
            throw Exception("Failed to fetch denuncias: ${response.statusCode}")
        }
    }
    
    suspend fun createDenuncia(denuncia: DenunciaDto): Result<DenunciaDto> = runCatching {
        val json = Json { encodeDefaults = true }
        val body = json.encodeToString(denuncia)
        
        val response = client.postRequest(
            path = "rest/v1/denuncias",
            body = body,
            headers = mapOf(
                "Content-Type" to "application/json",
                "Prefer" to "return=representation"
            )
        )
        
        if (response.statusCode in 200..201) {
            val responseJson = Json { ignoreUnknownKeys = true }
            val list = responseJson.decodeFromString<List<DenunciaDto>>(response.body)
            list.firstOrNull() ?: throw Exception("No denuncia returned")
        } else {
            throw Exception("Failed to create denuncia: ${response.statusCode}")
        }
    }
}
