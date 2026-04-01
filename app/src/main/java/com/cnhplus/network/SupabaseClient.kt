package com.cnhplus.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * Supabase PostgREST client para comunicação com o banco de dados.
 */
class SupabaseClient(
    private val baseUrl: String,
    private val anonKey: String
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    // ==================== GET ====================

    inline fun <reified T> get(
        table: String,
        params: Map<String, String> = emptyMap()
    ): Result<List<T>> {
        return runCatching {
            val url = buildUrl(table, params)
            executeGet<List<T>>(url)
        }
    }

    inline fun <reified T> getById(table: String, id: String): Result<T?> {
        return runCatching {
            val url = "$baseUrl/rest/v1/$table?id=eq.$id"
            executeGet<List<T>>(url).firstOrNull()
        }
    }

    inline fun <reified T> getByColumn(
        table: String,
        column: String,
        value: String
    ): Result<T?> {
        return runCatching {
            val url = "$baseUrl/rest/v1/$table?$column=eq.$value"
            executeGet<List<T>>(url).firstOrNull()
        }
    }

    inline fun <reified T> getAllWithRelations(
        table: String,
        select: String,
        params: Map<String, String> = emptyMap()
    ): Result<List<T>> {
        return runCatching {
            val url = buildString {
                append("$baseUrl/rest/v1/$table?select=$select")
                if (params.isNotEmpty()) {
                    append("&")
                    append(params.entries.joinToString("&") { "${it.key}=${it.value}" })
                }
            }
            executeGet<List<T>>(url)
        }
    }

    // ==================== INSERT ====================

    inline fun <reified T : Any> insert(table: String, item: T): Result<T> {
        return runCatching {
            val body = json.encodeToString(item)
            val request = Request.Builder()
                .url("$baseUrl/rest/v1/$table")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val error = response.body?.string() ?: "Unknown error"
                    throw Exception("INSERT $table failed (${response.code}): $error")
                }
                val respBody = response.body?.string() ?: "[]"
                val list = json.decodeFromString<List<T>>(respBody)
                list.firstOrNull() ?: throw Exception("No data returned from insert into $table")
            }
        }
    }

    // ==================== UPDATE ====================

    inline fun <reified T : Any> update(
        table: String,
        column: String,
        value: String,
        fields: Map<String, Any?>
    ): Result<Unit> {
        return runCatching {
            val body = buildJson(fields)
            val request = Request.Builder()
                .url("$baseUrl/rest/v1/$table?$column=eq.$value")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .patch(body.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val error = response.body?.string() ?: "Unknown error"
                    throw Exception("UPDATE $table failed (${response.code}): $error")
                }
            }
        }
    }

    // ==================== DELETE ====================

    fun delete(table: String, column: String, value: String): Result<Unit> {
        return runCatching {
            val request = Request.Builder()
                .url("$baseUrl/rest/v1/$table?$column=eq.$value")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .delete()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val error = response.body?.string() ?: "Unknown error"
                    throw Exception("DELETE $table failed (${response.code}): $error")
                }
            }
        }
    }

    // ==================== RPC ====================

    fun callRpc(functionName: String, params: Map<String, Any?>): Result<String> {
        return runCatching {
            val body = buildJson(params)
            val request = Request.Builder()
                .url("$baseUrl/rest/v1/rpc/$functionName")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $anonKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("RPC $functionName failed: ${response.code}")
                }
                response.body?.string() ?: ""
            }
        }
    }

    // ==================== AUTH ====================

    fun signInWithEmail(email: String, password: String): Result<AuthResponse> {
        return runCatching {
            val body = """{"email":"$email","password":"$password"}"""
            val request = Request.Builder()
                .url("$baseUrl/auth/v1/token?grant_type=password")
                .header("apikey", anonKey)
                .header("Content-Type", "application/json")
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val error = response.body?.string() ?: "Unknown error"
                    throw Exception("Login failed: $error")
                }
                val resp = response.body?.string() ?: ""
                json.decodeFromString<AuthResponse>(resp)
            }
        }
    }

    fun signUpWithEmail(email: String, password: String): Result<AuthResponse> {
        return runCatching {
            val body = """{"email":"$email","password":"$password"}"""
            val request = Request.Builder()
                .url("$baseUrl/auth/v1/signup")
                .header("apikey", anonKey)
                .header("Content-Type", "application/json")
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val error = response.body?.string() ?: "Unknown error"
                    throw Exception("Sign up failed: $error")
                }
                val resp = response.body?.string() ?: ""
                json.decodeFromString<AuthResponse>(resp)
            }
        }
    }

    fun signOut(token: String): Result<Unit> {
        return runCatching {
            val request = Request.Builder()
                .url("$baseUrl/auth/v1/logout")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $token")
                .post("".toRequestBody())
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("Logout failed: ${response.code}")
                }
            }
        }
    }

    fun getUser(token: String): Result<AuthUser> {
        return runCatching {
            val request = Request.Builder()
                .url("$baseUrl/auth/v1/user")
                .header("apikey", anonKey)
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("Get user failed: ${response.code}")
                }
                val resp = response.body?.string() ?: "{}"
                json.decodeFromString<AuthUser>(resp)
            }
        }
    }

    // ==================== PRIVATE ====================

    inline fun <reified T> executeGet(url: String): T {
        val request = Request.Builder()
            .url(url)
            .header("apikey", anonKey)
            .header("Authorization", "Bearer $anonKey")
            .header("Content-Type", "application/json")
            .get()
            .build()

        return client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val error = response.body?.string() ?: "Unknown error"
                throw Exception("GET failed (${response.code}): $error")
            }
            val body = response.body?.string() ?: ""
            json.decodeFromString<T>(body)
        }
    }

    private fun buildUrl(table: String, params: Map<String, String>): String {
        val base = "$baseUrl/rest/v1/$table"
        return if (params.isEmpty()) base else {
            "$base?${params.entries.joinToString("&") { "${it.key}=${it.value}" }}"
        }
    }

    private fun buildJson(fields: Map<String, Any?>): String {
        return buildString {
            append("{")
            fields.entries.forEachIndexed { index, (key, value) ->
                if (index > 0) append(",")
                append("\"$key\":")
                when (value) {
                    is String -> append("\"${value.replace("\"", "\\\"")}\"")
                    is Int, is Long, is Float, is Double, is Boolean -> append(value.toString())
                    null -> append("null")
                    else -> append("\"${value.toString().replace("\"", "\\\"")}\"")
                }
            }
            append("}")
        }
    }
}

// ==================== AUTH MODELS ====================

import kotlinx.serialization.Serializable

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
    val email_confirmed_at: String? = null,
    val created_at: String? = null
)
