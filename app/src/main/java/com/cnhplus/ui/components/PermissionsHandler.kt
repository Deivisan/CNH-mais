package com.cnhplus.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Hook para gerenciar permissões runtime de forma declarativa.
 * 
 * Usage:
 * ```
 * val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
 * 
 * if (cameraPermission.hasPermission) {
 *     // Usar câmera
 * } else {
 *     Button(onClick = { cameraPermission.launchPermissionRequest() }) {
 *         Text("Permitir Câmera")
 *     }
 * }
 * ```
 */
@Composable
fun rememberPermissionState(permission: String): PermissionState {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }
    
    return remember(permission, hasPermission) {
        PermissionState(
            permission = permission,
            hasPermission = hasPermission,
            launchPermissionRequest = { launcher.launch(permission) }
        )
    }
}

data class PermissionState(
    val permission: String,
    val hasPermission: Boolean,
    val launchPermissionRequest: () -> Unit
)

/**
 * Hook para múltiplas permissões (ex: CAMERA + READ_MEDIA_IMAGES).
 */
@Composable
fun rememberMultiplePermissionsState(vararg permissions: String): MultiplePermissionsState {
    val context = LocalContext.current
    var permissionsMap by remember {
        mutableStateOf(
            permissions.associateWith {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        )
    }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        permissionsMap = results
    }
    
    return remember(permissions.toList(), permissionsMap) {
        MultiplePermissionsState(
            permissionsMap = permissionsMap,
            allPermissionsGranted = permissionsMap.values.all { it },
            launchPermissionRequest = { launcher.launch(permissions.toList().toTypedArray()) }
        )
    }
}

data class MultiplePermissionsState(
    val permissionsMap: Map<String, Boolean>,
    val allPermissionsGranted: Boolean,
    val launchPermissionRequest: () -> Unit
)

/**
 * Retorna permissões necessárias baseado na versão do Android.
 * Android 13+ usa READ_MEDIA_IMAGES, versões antigas READ_EXTERNAL_STORAGE.
 */
fun getPhotoPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
