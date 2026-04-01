package com.cnhplus.data.repository

import com.cnhplus.data.AvatarDto
import com.cnhplus.network.SupabaseClient

class AvatarRepository(private val client: SupabaseClient) {
    
    fun fetchByUserId(userId: String): Result<AvatarDto?> {
        return client.getByColumn("avatares", "user_id", userId)
    }
    
    fun createOrUpdateAvatar(avatar: AvatarDto): Result<AvatarDto> {
        return client.insert("avatares", avatar)
    }
}
