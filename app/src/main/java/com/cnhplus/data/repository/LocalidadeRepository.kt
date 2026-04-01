package com.cnhplus.data.repository

import com.cnhplus.data.LocalidadeDto
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.json.*

class LocalidadeRepository(private val client: SupabaseClient) {
    suspend fun fetchByAulaId(aulaId: String): Result<LocalidadeDto?> = runCatching {
        val response = client.getRequest(
            path = "rest/v1/localidades?aula_id=eq.$aulaId",
            headers = mapOf("Accept" to "application/json")
        )
        
        if (response.statusCode == 200) {
            val json = Json { ignoreUnknownKeys = true }
            val list = json.decodeFromString<List<LocalidadeDto>>(response.body)
            list.firstOrNull()
        } else {
            throw Exception("Failed to fetch localidade: ${response.statusCode}")
        }
    }
    
    suspend fun createLocalidade(localidade: LocalidadeDto): Result<LocalidadeDto> = runCatching {
        val json = Json { encodeDefaults = true }
        val body = json.encodeToString(localidade)
        
        val response = client.postRequest(
            path = "rest/v1/localidades",
            body = body,
            headers = mapOf(
                "Content-Type" to "application/json",
                "Prefer" to "return=representation"
            )
        )
        
        if (response.statusCode in 200..201) {
            val responseJson = Json { ignoreUnknownKeys = true }
            val list = responseJson.decodeFromString<List<LocalidadeDto>>(response.body)
            list.firstOrNull() ?: throw Exception("No localidade returned")
        } else {
            throw Exception("Failed to create localidade: ${response.statusCode}")
        }
    }
    
    suspend fun updateLocalidade(id: String, localidade: LocalidadeDto): Result<LocalidadeDto> = runCatching {
        val json = Json { encodeDefaults = true }
        val body = json.encodeToString(localidade)
        
        val response = client.patchRequest(
            path = "rest/v1/localidades?id=eq.$id",
            body = body,
            headers = mapOf(
                "Content-Type" to "application/json",
                "Prefer" to "return=representation"
            )
        )
        
        if (response.statusCode == 200) {
            val responseJson = Json { ignoreUnknownKeys = true }
            val list = responseJson.decodeFromString<List<LocalidadeDto>>(response.body)
            list.firstOrNull() ?: throw Exception("No localidade returned")
        } else {
            throw Exception("Failed to update localidade: ${response.statusCode}")
        }
    }
}
