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

    fun register(email: String, password: String, onComplete: (Result<Unit>) -> Unit) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val authResult = _supabase.signUpWithEmail(email, password)
            authResult.fold(
                onSuccess = { response ->
                    val token = response.access_token ?: ""
                    val userId = response.user?.id ?: ""
                    if (token.isNotBlank() && userId.isNotBlank()) {
                        // Create profile
                        val profile = ProfileDto(id = userId, email = email, role = "candidato")
                        profileRepo.createProfile(profile).fold(
                            onSuccess = {
                                saveSession(token, userId)
                                _currentUser.value = it
                                _currentRole.value = "candidato"
                                _sessionState.value = SessionState.Authenticated
                                _isLoading.value = false
                                onComplete(Result.success(Unit))
                            },
                            onFailure = { e ->
                                _isLoading.value = false
                                onComplete(Result.failure(e))
                            }
                        )
                    } else {
                        _isLoading.value = false
                        onComplete(Result.failure(Exception("Registro falhou")))
                    }
                },
                onFailure = { error ->
                    _isLoading.value = false
                    onComplete(Result.failure(error))
                }
            )
        }
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
                        "admin" -> {
                            // No additional records needed
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
        dataStore.edit { prefs ->
            prefs[SessionKeys.ACCESS_TOKEN] = token
            prefs[SessionKeys.USER_ID] = userId
        }
        val profile = profileRepo.getProfile(userId).getOrNull()
        if (profile != null) {
            prefs[SessionKeys.USER_ROLE] = profile.role
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
