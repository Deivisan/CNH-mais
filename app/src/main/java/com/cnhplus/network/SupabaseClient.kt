package com.cnhplus.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

class SupabaseClient(
    @PublishedApi internal val baseUrl: String,
    @PublishedApi internal val anonKey: String
) {
    @PublishedApi internal val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @PublishedApi internal val json = Json { ignoreUnknownKeys = true; encodeDefaults = true; isLenient = true }

    // ==================== GET ====================

    inline fun <reified T : Any> get(
        table: String,
        params: Map<String, String> = emptyMap()
    ): Result<List<T>> {
        val urlPart = if (params.isEmpty()) "" else "?${params.entries.joinToString("&") { "${it.key}=${it.value}" }}"
        val url = "$baseUrl/rest/v1/$table$urlPart"
        val request = Request.Builder()
            .url(url)
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .get().build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("GET failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                Result.success(json.decodeFromString<List<T>>(response.body?.string() ?: "[]"))
            }
        }
    }

    inline fun <reified T : Any> getById(table: String, id: String): Result<T?> {
        val request = Request.Builder()
            .url("$baseUrl/rest/v1/$table?id=eq.$id")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .get().build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("GET failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                val list = json.decodeFromString<List<T>>(response.body?.string() ?: "[]")
                Result.success(list.firstOrNull())
            }
        }
    }

    inline fun <reified T : Any> getByColumn(table: String, column: String, value: String): Result<T?> {
        val request = Request.Builder()
            .url("$baseUrl/rest/v1/$table?$column=eq.$value")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .get().build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("GET failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                val list = json.decodeFromString<List<T>>(response.body?.string() ?: "[]")
                Result.success(list.firstOrNull())
            }
        }
    }

    inline fun <reified T : Any> getAllWithRelations(
        table: String,
        select: String,
        params: Map<String, String> = emptyMap()
    ): Result<List<T>> {
        val queryParts = listOf("select=$select") +
            params.entries.map { "${it.key}=${it.value}" }
        val query = queryParts.joinToString("&")
        val request = Request.Builder()
            .url("$baseUrl/rest/v1/$table?$query")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .get().build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("GET failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                Result.success(json.decodeFromString<List<T>>(response.body?.string() ?: "[]"))
            }
        }
    }

    // ==================== INSERT ====================

    inline fun <reified T : Any> insert(table: String, item: T): Result<T> {
        val body = json.encodeToString(item)
        val request = Request.Builder()
            .url("$baseUrl/rest/v1/$table")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .header("Prefer", "return=representation")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("POST failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                val list = json.decodeFromString<List<T>>(response.body?.string() ?: "[]")
                list.firstOrNull()
                    ?.let { Result.success(it) }
                    ?: Result.failure(Exception("No data returned"))
            }
        }
    }

    // ==================== UPDATE ====================

    inline fun <reified T : Any> toJsonString(value: T): String = json.encodeToString(value)

    fun update(
        table: String,
        column: String,
        value: String,
        fields: Map<String, Any?>
    ): Result<Unit> {
        val body = buildString {
            append("{")
            fields.entries.forEachIndexed { i, (k, v) ->
                if (i > 0) append(",")
                append("\"$k\":")
                append(when (v) {
                    is String -> "\"${v.replace("\"", "\\\"")}\""
                    is Number, is Boolean -> v.toString()
                    null -> "null"
                    else -> "\"${v.toString().replace("\"", "\\\"")}\""
                })
            }
            append("}")
        }
        val request = Request.Builder()
            .url("$baseUrl/rest/v1/$table?$column=eq.$value")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .header("Prefer", "return=minimal")
            .patch(body.toRequestBody("application/json".toMediaType()))
            .build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("PATCH failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                Result.success(Unit)
            }
        }
    }

    // ==================== DELETE ====================

    fun delete(table: String, column: String, value: String): Result<Unit> {
        val request = Request.Builder()
            .url("$baseUrl/rest/v1/$table?$column=eq.$value")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .delete().build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("DELETE failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                Result.success(Unit)
            }
        }
    }

    // ==================== RPC ====================

    fun callRpc(functionName: String, params: Map<String, Any?>): Result<String> {
        val body = buildString {
            append("{")
            params.entries.forEachIndexed { i, (k, v) ->
                if (i > 0) append(",")
                append("\"$k\":")
                append(when (v) {
                    is String -> "\"${v.replace("\"", "\\\"")}\""
                    is Number, is Boolean -> v.toString()
                    null -> "null"
                    else -> "\"${v.toString().replace("\"", "\\\"")}\""
                })
            }
            append("}")
        }
        val request = Request.Builder()
            .url("$baseUrl/rest/v1/rpc/$functionName")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .header("Prefer", "return=representation")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("RPC failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                Result.success(response.body?.string() ?: "")
            }
        }
    }

    // ==================== AUTH ====================

    fun signInWithEmail(email: String, password: String): Result<AuthResponse> {
        return _doAuth("$baseUrl/auth/v1/token?grant_type=password", email, password)
    }

    fun signUpWithEmail(email: String, password: String): Result<AuthResponse> {
        return _doAuth("$baseUrl/auth/v1/signup", email, password)
    }

    fun signOut(token: String): Result<Unit> {
        val request = Request.Builder()
            .url("$baseUrl/auth/v1/logout")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $token")
            .post("".toRequestBody())
            .build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) Result.failure(Exception("Logout failed: ${response.code}"))
            else Result.success(Unit)
        }
    }

    fun getUser(token: String): Result<AuthUser> {
        val request = Request.Builder()
            .url("$baseUrl/auth/v1/user")
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $token")
            .get().build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) Result.failure(Exception("Get user failed: ${response.code}"))
            else Result.success(json.decodeFromString(AuthUser.serializer(), response.body?.string() ?: "{}"))
        }
    }

    // ==================== STORAGE ====================

    /**
     * Upload file to Supabase Storage bucket.
     * @param bucket Nome do bucket (ex: "avatars", "documentos")
     * @param filePath Caminho relativo dentro do bucket (ex: "user123/profile.jpg")
     * @param fileBytes Bytes do arquivo
     * @param contentType MIME type (ex: "image/jpeg", "application/pdf")
     * @return URL pública do arquivo se sucesso
     */
    fun uploadFile(
        bucket: String,
        filePath: String,
        fileBytes: ByteArray,
        contentType: String = "image/jpeg"
    ): Result<String> {
        val url = "$baseUrl/storage/v1/object/$bucket/$filePath"
        val body = fileBytes.toRequestBody(contentType.toMediaType())
        
        val request = Request.Builder()
            .url(url)
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", contentType)
            .post(body)
            .build()
            
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: "Unknown"
                Result.failure(Exception("Upload failed (${response.code}): $errorBody"))
            } else {
                // Supabase retorna URL pública no response
                val publicUrl = "$baseUrl/storage/v1/object/public/$bucket/$filePath"
                Result.success(publicUrl)
            }
        }
    }

    /**
     * Delete file from Supabase Storage.
     */
    fun deleteFile(bucket: String, filePath: String): Result<Unit> {
        val url = "$baseUrl/storage/v1/object/$bucket/$filePath"
        
        val request = Request.Builder()
            .url(url)
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .delete()
            .build()
            
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("Delete failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                Result.success(Unit)
            }
        }
    }

    /**
     * Get public URL for a file in storage (sem autenticação).
     */
    fun getPublicUrl(bucket: String, filePath: String): String {
        return "$baseUrl/storage/v1/object/public/$bucket/$filePath"
    }

    // ==================== PRIVATE HELPERS ====================

    private fun _doAuth(url: String, email: String, password: String): Result<AuthResponse> {
        val body = """{"email":"$email","password":"$password"}"""
        val request = Request.Builder()
            .url(url)
            .header("apikey", anonKey)
            .header("Content-Type", "application/json")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        return okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("Auth failed (${response.code}): ${response.body?.string() ?: "Unknown"}"))
            } else {
                Result.success(json.decodeFromString(AuthResponse.serializer(), response.body?.string() ?: ""))
            }
        }
    }
}

// ==================== AUTH MODELS ====================

@Serializable
data class AuthResponse(
    val access_token: String? = null,
    val refresh_token: String? = null,
    val user: AuthUser? = null,
    val error: String? = null
)

@Serializable
data class AuthUser(
    val id: String? = null,
    val email: String? = null,
    @SerialName("email_confirmed_at")
    val email_confirmed_at: String? = null,
    val created_at: String? = null
)
