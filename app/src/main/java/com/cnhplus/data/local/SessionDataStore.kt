package com.cnhplus.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cnhplus.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_ROLE = stringPreferencesKey("user_role")
    }

    override suspend fun saveSession(token: String, userId: String, role: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
            prefs[USER_ID] = userId
            prefs[USER_ROLE] = role
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.first()[ACCESS_TOKEN]
    }

    override suspend fun getUserId(): String? {
        return dataStore.data.first()[USER_ID]
    }

    override suspend fun getUserRole(): String? {
        return dataStore.data.first()[USER_ROLE]
    }

    override fun observeUserRole(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[USER_ROLE]
        }
    }
}
