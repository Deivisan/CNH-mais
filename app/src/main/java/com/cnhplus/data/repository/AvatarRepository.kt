package com.cnhplus.data.repository

import com.cnhplus.data.AvatarDto
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.json.*

class AvatarRepository(private val client: SupabaseClient) {
    suspend fun fetchByUserId(userId: String): Result<AvatarDto?> = runCatching {
        val response = client.getRequest(
            path = "rest/v1/avatares?user_id=eq.$userId",
            headers = mapOf("Accept" to "application/json")
        )
        
        if (response.statusCode == 200) {
            val json = Json { ignoreUnknownKeys = true }
            val list = json.decodeFromString<List<AvatarDto>>(response.body)
            list.firstOrNull()
        } else {
            throw Exception("Failed to fetch avatar: ${response.statusCode}")
        }
    }
    
    suspend fun createOrUpdateAvatar(avatar: AvatarDto): Result<AvatarDto> = runCatching {
        val json = Json { encodeDefaults = true }
        val body = json.encodeToString(avatar)
        
        // Try to update first
        val updateResponse = client.patchRequest(
            path = "rest/v1/avatares?user_id=eq.${avatar.user_id}",
            body = body,
            headers = mapOf(
                "Content-Type" to "application/json",
                "Prefer" to "return=representation"
            )
        )
        
        if (updateResponse.statusCode == 200) {
            val responseJson = Json { ignoreUnknownKeys = true }
            val list = responseJson.decodeFromString<List<AvatarDto>>(updateResponse.body)
            return@runCatching list.firstOrNull() ?: throw Exception("No avatar returned")
        }
        
        // If update failed, try insert
        val insertResponse = client.postRequest(
            path = "rest/v1/avatares",
            body = body,
            headers = mapOf(
                "Content-Type" to "application/json",
                "Prefer" to "return=representation"
            )
        )
        
        if (insertResponse.statusCode in 200..201) {
            val responseJson = Json { ignoreUnknownKeys = true }
            val list = responseJson.decodeFromString<List<AvatarDto>>(insertResponse.body)
            list.firstOrNull() ?: throw Exception("No avatar returned")
        } else {
            throw Exception("Failed to create/update avatar: ${insertResponse.statusCode}")
        }
    }
}
