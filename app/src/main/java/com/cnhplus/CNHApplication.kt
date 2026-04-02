package com.cnhplus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class com Hilt DI.
 * Necessário para injeção de dependência funcionar.
 */
@HiltAndroidApp
class CNHApplication : Application()
