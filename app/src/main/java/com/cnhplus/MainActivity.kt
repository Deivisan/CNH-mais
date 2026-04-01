package com.cnhplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.cnhplus.app.AppState
import com.cnhplus.app.SessionState
import com.cnhplus.navigation.CNHNavHost
import com.cnhplus.navigation.Screen
import com.cnhplus.ui.theme.CNHMaisTheme
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.Secondary

const val SUPABASE_URL = "https://ibyngfqddoefatqtojfj.supabase.co"
const val SUPABASE_ANON_KEY = "sb_publishable_REPLACE_WITH_REAL_ANON_KEY_BEFORE_BUILD"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val supabaseUrl = SUPABASE_URL
            val supabaseAnonKey = SUPABASE_ANON_KEY

            val appState = remember { AppState(applicationContext, supabaseUrl, supabaseAnonKey) }
            val session by appState.sessionState.collectAsStateWithLifecycle()

            val startDestination = remember(session, appState.currentRole.value) {
                when (session) {
                    is SessionState.Loading -> Screen.Login.route
                    is SessionState.Unauthenticated -> Screen.Login.route
                    is SessionState.Authenticated -> {
                        when (appState.currentRole.value) {
                            "instrutor" -> Screen.InstrutorHome.route
                            "admin" -> Screen.AdminHome.route
                            "candidato" -> Screen.CandidatoHome.route
                            else -> Screen.Login.route
                        }
                    }
                    is SessionState.OnboardingRequired -> Screen.OnboardingCandidato.route
                }
            }

            CompositionLocalProvider(LocalAppState provides appState) {
                CNHMaisTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        val navController = rememberNavController()

                        if (session is SessionState.Loading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Secondary)
                            }
                        } else {
                            CNHNavHost(
                                navController = navController,
                                startDestination = startDestination
                            )
                        }
                    }
                }
            }
        }
    }
}
