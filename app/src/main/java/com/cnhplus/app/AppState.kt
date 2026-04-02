package com.cnhplus.app

import android.content.Context
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.cnhplus.data.*
import com.cnhplus.data.repository.*
import com.cnhplus.network.SupabaseClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// ==================== DATASTORE ====================

private val Context.dataStore by preferencesDataStore("cnhmais_session")

object SessionKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val USER_ID = stringPreferencesKey("user_id")
    val USER_ROLE = stringPreferencesKey("user_role")
    val ONBOARDING_DONE = stringPreferencesKey("onboarding_done")
}

// ==================== APP STATE ====================

@Stable
class AppState(
    context: Context,
    supabaseUrl: String,
    supabaseAnonKey: String
) {
    private val _supabase = SupabaseClient(supabaseUrl, supabaseAnonKey)
    val supabase: SupabaseClient get() = _supabase

    // Repositories
    val profileRepo = ProfileRepository(_supabase)
    val candidatoRepo = CandidatoRepository(_supabase)
    val instrutorRepo = InstrutorRepository(_supabase)
    val aulaRepo = AulaRepository(_supabase)
    val pagamentoRepo = PagamentoRepository(_supabase)
    val avaliacaoRepo = AvaliacaoRepository(_supabase)
    val disputaRepo = DisputaRepository(_supabase)
    val mensagemRepo = MensagemRepository(_supabase)
    val dadosBancariosRepo = DadosBancariosRepository(_supabase)
    val repasseRepo = RepasseRepository(_supabase)
    val bannerRepo = BannerRepository(_supabase)
    val localidadeRepo = LocalidadeRepository(_supabase)
    val denunciaRepo = DenunciaRepository(_supabase)
    val avatarRepo = AvatarRepository(_supabase)

    // Session state
    private val dataStore = context.dataStore

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _currentUser = MutableStateFlow<ProfileDto?>(null)
    val currentUser: StateFlow<ProfileDto?> = _currentUser.asStateFlow()

    private val _currentRole = MutableStateFlow<String?>(null)
    val currentRole: StateFlow<String?> = _currentRole.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var sessionScope: CoroutineScope? = null

    init {
        loadSession()
    }

    // ==================== AUTH ====================

    fun login(email: String, password: String, onComplete: (Result<Unit>) -> Unit) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val authResult = _supabase.signInWithEmail(email, password)
            authResult.fold(
                onSuccess = { response ->
                    val token = response.access_token ?: ""
                    val userId = response.user?.id ?: ""
                    if (token.isNotBlank() && userId.isNotBlank()) {
                        saveSession(token, userId)
                        fetchUserProfile(userId)
                        onComplete(Result.success(Unit))
                    } else {
                        _isLoading.value = false
                        onComplete(Result.failure(Exception("Token ou ID ausente")))
                    }
                },
                onFailure = { error ->
                    _isLoading.value = false
                    onComplete(Result.failure(error))
                }
            )
        }
    }

    fun register(email: String, password: String, nome: String, onComplete: (Result<Unit>) -> Unit) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            val authResult = _supabase.signUpWithEmail(email, password)
            authResult.fold(
                onSuccess = { response ->
                    try {
                        val token = response.access_token ?: ""
                        val userId = response.user?.id ?: ""

                        if (token.isBlank() || userId.isBlank()) {
                            _isLoading.value = false
                            onComplete(Result.failure(Exception("Registro falhou: token ou ID ausente. Verifique se a confirmação de email está desativada no Supabase.")))
                            return@fold
                        }

                        // Wait for trigger to create profile, then fallback if needed
                        var profile: ProfileDto? = null
                        repeat(3) { attempt ->
                            delay(1000L * (attempt + 1)) // 1s, 2s, 3s backoff
                            profile = profileRepo.getProfile(userId).getOrNull()
                            if (profile != null) return@repeat
                        }

                        // If trigger didn't create profile, create manually with role 'pendente'
                        if (profile == null) {
                            profile = ProfileDto(id = userId, email = email, nome = nome, role = "pendente")
                            profileRepo.createProfile(profile!!)
                        } else {
                            // Update nome if profile was created by trigger without nome
                            profileRepo.updateProfile(userId, mapOf("nome" to nome))
                            profile = profile!!.copy(nome = nome)
                        }

                        // Save session with role from profile
                        val finalProfile = profile!! // smart cast workaround
                        dataStore.edit { prefs ->
                            prefs[SessionKeys.ACCESS_TOKEN] = token
                            prefs[SessionKeys.USER_ID] = userId
                            prefs[SessionKeys.USER_ROLE] = finalProfile.role
                        }

                        _currentUser.value = finalProfile
                        _currentRole.value = finalProfile.role
                        _sessionState.value = SessionState.Authenticated
                        _isLoading.value = false
                        onComplete(Result.success(Unit))
                    } catch (e: Exception) {
                        _isLoading.value = false
                        onComplete(Result.failure(Exception("Erro ao criar conta: ${e.message}")))
                    }
                },
                onFailure = { error ->
                    _isLoading.value = false
                    onErrorResult(error, onComplete)
                }
            )
        }
    }

    /** Extract user-friendly error messages from Supabase auth errors */
    private fun onErrorResult(error: Throwable, onComplete: (Result<Unit>) -> Unit) {
        val msg = error.message ?: "Erro desconhecido"
        val friendly = when {
            msg.contains("User already registered") -> "Este email já está cadastrado. Faça login."
            msg.contains("Invalid email") -> "Email inválido. Verifique o formato."
            msg.contains("Password should be at least") -> "Senha muito curta. Use pelo menos 6 caracteres."
            msg.contains("Unable to validate email") || msg.contains("Email") -> "Erro com email. Verifique se o endereço existe."
            msg.contains("401") -> "Chave da API inválida. Entre em contato com suporte."
            else -> "Erro ao criar conta: ${msg.take(100)}"
        }
        onComplete(Result.failure(Exception(friendly)))
    }

    fun selectRole(role: String, onComplete: (Result<Unit>) -> Unit) {
        _isLoading.value = true
        val userId = _currentUser.value?.id ?: run {
            onComplete(Result.failure(Exception("User not authenticated")))
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            // Update profile role
            profileRepo.updateProfile(userId, mapOf("role" to role)).fold(
                onSuccess = {
                    _currentRole.value = role
                    // Create corresponding user record
                    when (role) {
                        "candidato" -> {
                            val candidato = CandidatoDto(id = userId)
                            candidatoRepo.createCandidato(candidato)
                        }
                        "instrutor" -> {
                            val instrutor = InstrutorDto(id = userId, status = "pendente")
                            instrutorRepo.createInstrutor(instrutor)
                        }
                    }
                    _sessionState.value = SessionState.Authenticated
                    _isLoading.value = false
                    onComplete(Result.success(Unit))
                },
                onFailure = { e ->
                    _isLoading.value = false
                    onComplete(Result.failure(e))
                }
            )
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            _sessionState.value = SessionState.Unauthenticated
            _currentUser.value = null
            _currentRole.value = null
            clearSession()
        }
    }

    // ==================== SESSION ====================

    private fun loadSession() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataStore.data.first().let { prefs ->
                    val token = prefs[SessionKeys.ACCESS_TOKEN]
                    val userId = prefs[SessionKeys.USER_ID]
                    val role = prefs[SessionKeys.USER_ROLE]
                    if (!token.isNullOrBlank() && !userId.isNullOrBlank()) {
                        fetchUserProfile(userId)
                    } else {
                        _sessionState.value = SessionState.Unauthenticated
                    }
                }
            } catch (e: Exception) {
                _sessionState.value = SessionState.Unauthenticated
            }
        }
    }

    private suspend fun fetchUserProfile(userId: String) {
        profileRepo.getProfile(userId).fold(
            onSuccess = { profile ->
                if (profile != null) {
                    _currentUser.value = profile
                    _currentRole.value = profile.role
                    _sessionState.value = SessionState.Authenticated
                } else {
                    _sessionState.value = SessionState.Unauthenticated
                }
            },
            onFailure = {
                _sessionState.value = SessionState.Unauthenticated
            }
        )
        _isLoading.value = false
    }

    private suspend fun saveSession(token: String, userId: String) {
        val profile = profileRepo.getProfile(userId).getOrNull()
        dataStore.edit { prefs ->
            prefs[SessionKeys.ACCESS_TOKEN] = token
            prefs[SessionKeys.USER_ID] = userId
            if (profile != null) {
                prefs[SessionKeys.USER_ROLE] = profile.role
            }
        }
        if (profile != null) {
            _currentUser.value = profile
            _currentRole.value = profile.role
            _sessionState.value = SessionState.Authenticated
        }
    }

    private fun saveSession(token: String, userId: String, role: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { prefs ->
                prefs[SessionKeys.ACCESS_TOKEN] = token
                prefs[SessionKeys.USER_ID] = userId
                prefs[SessionKeys.USER_ROLE] = role
            }
        }
    }

    private suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}

// ==================== SESSION STATES ====================

sealed class SessionState {
    object Loading : SessionState()
    object Unauthenticated : SessionState()
    object Authenticated : SessionState()
    object OnboardingRequired : SessionState()
}
