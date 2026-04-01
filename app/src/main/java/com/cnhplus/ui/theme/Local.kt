package com.cnhplus.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.cnhplus.app.AppState

// CompositionLocal para acessar o AppState de qualquer composable
val LocalAppState = staticCompositionLocalOf<AppState> {
    error("AppState não fornecido - certifique-se de usar CompositionLocalProvider(LocalAppState provides appState)")
}
