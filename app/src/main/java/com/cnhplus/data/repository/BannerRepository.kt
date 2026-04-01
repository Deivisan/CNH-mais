package com.cnhplus.data.repository

import com.cnhplus.data.BannerDto
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.json.*

class BannerRepository(private val client: SupabaseClient) {
    suspend fun fetchBanners(): Result<List<BannerDto>> = runCatching {
        val response = client.getRequest(
            path = "rest/v1/banners?ativo=eq.true&order=ordem.asc",
            headers = mapOf(
                "Accept" to "application/json"
            )
        )
        
        if (response.statusCode == 200) {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<List<BannerDto>>(response.body)
        } else {
            throw Exception("Failed to fetch banners: ${response.statusCode}")
        }
    }
    
    suspend fun createBanner(banner: BannerDto): Result<BannerDto> = runCatching {
        val json = Json { encodeDefaults = true }
        val body = json.encodeToString(banner)
        
        val response = client.postRequest(
            path = "rest/v1/banners",
            body = body,
            headers = mapOf(
                "Content-Type" to "application/json",
                "Prefer" to "return=representation"
            )
        )
        
        if (response.statusCode in 200..201) {
            val responseJson = Json { ignoreUnknownKeys = true }
            val list = responseJson.decodeFromString<List<BannerDto>>(response.body)
            list.firstOrNull() ?: throw Exception("No banner returned")
        } else {
            throw Exception("Failed to create banner: ${response.statusCode}")
        }
    }
}
