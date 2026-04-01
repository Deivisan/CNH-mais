package com.cnhplus.data.repository

import com.cnhplus.data.ProfileDto
import com.cnhplus.network.SupabaseClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileRepository(
    private val client: SupabaseClient
) {
    private val table = "profiles"

    fun getProfile(userId: String): Result<ProfileDto?> {
        return client.getById<ProfileDto>(table, userId)
    }

    fun createProfile(profile: ProfileDto): Result<ProfileDto> {
        return client.insert(table, profile)
    }

    fun updateProfile(userId: String, fields: Map<String, Any?>): Result<Unit> {
        return client.update(table, "id", userId, fields)
    }

    fun getProfileByRole(userId: String, role: String): Result<ProfileDto?> {
        return runCatching {
            val result = client.getByColumn<ProfileDto>(table, "id", userId)
            result.getOrNull()?.takeIf { it.role == role }
        }
    }
}
