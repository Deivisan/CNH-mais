package com.cnhplus.core.di

import kotlinx.serialization.json.Json

/**
 * Configuração singleton para serialização JSON.
 * Elimina warnings de "Redundant creation of Json format".
 */
object JsonConfig {
    val default = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }
    
    val pretty = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }
}
