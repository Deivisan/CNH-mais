package com.cnhplus.screens.candidato

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.components.ImagePickerButton
import com.cnhplus.ui.components.getPhotoPermissions
import com.cnhplus.ui.components.rememberMultiplePermissionsState
import com.cnhplus.ui.theme.*
import kotlinx.coroutines.launch

/**
 * Tela para completar perfil do candidato após registro.
 * Coleta: foto, nome completo, CPF, celular, cidade.
 */
@Composable
fun PerfilCompletoScreen(
    onComplete: () -> Unit
) {
    val app = LocalAppState.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Form state
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var photoBytes by remember { mutableStateOf<ByteArray?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Permissões de foto
    val photoPermissions = rememberMultiplePermissionsState(*getPhotoPermissions(), Manifest.permission.CAMERA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Primary, PrimaryLight, Secondary)
                )
            )
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Complete seu Perfil",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Form card
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Foto de perfil
                if (!photoPermissions.allPermissionsGranted) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Warning.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Permissões Necessárias",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Para adicionar sua foto, permita acesso à câmera e galeria.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { photoPermissions.launchPermissionRequest() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Permitir Acesso")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                ImagePickerButton(
                    currentImageUrl = null,
                    onImageSelected = { bytes -> photoBytes = bytes },
                    size = 120
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (photoBytes != null) "Foto selecionada ✓" else "Toque para adicionar foto",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (photoBytes != null) Success else Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Error message
                error?.let { errMsg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = errMsg,
                            color = Error,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Nome completo
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it; error = null },
                    label = { Text("Nome Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CPF
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { 
                        if (it.length <= 14) cpf = formatCPF(it)
                        error = null
                    },
                    label = { Text("CPF") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("000.000.000-00") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Celular
                OutlinedTextField(
                    value = celular,
                    onValueChange = { 
                        if (it.length <= 15) celular = formatPhone(it)
                        error = null
                    },
                    label = { Text("Celular") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    placeholder = { Text("(00) 00000-0000") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Cidade
                OutlinedTextField(
                    value = cidade,
                    onValueChange = { cidade = it; error = null },
                    label = { Text("Cidade") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    placeholder = { Text("Ex: Feira de Santana") }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Submit button
                Button(
                    onClick = {
                        if (!validateForm(nome, cpf, celular, cidade)) {
                            error = "Preencha todos os campos corretamente"
                            return@Button
                        }

                        isLoading = true
                        scope.launch {
                            try {
                                val userId = app.currentUser.value?.id ?: run {
                                    error = "Usuário não autenticado"
                                    isLoading = false
                                    return@launch
                                }

                                // Upload foto se selecionada
                                var photoUrl: String? = null
                                photoBytes?.let { bytes ->
                                    val uploadResult = app.supabase.uploadFile(
                                        bucket = "avatars",
                                        filePath = "$userId/profile.jpg",
                                        fileBytes = bytes,
                                        contentType = "image/jpeg"
                                    )
                                    uploadResult.fold(
                                        onSuccess = { url -> photoUrl = url },
                                        onFailure = { e -> 
                                            error = "Erro ao enviar foto: ${e.message}"
                                            isLoading = false
                                            return@launch
                                        }
                                    )
                                }

                                // Update profile
                                val updates = mutableMapOf<String, Any?>(
                                    "nome" to nome,
                                    "telefone" to celular
                                )
                                photoUrl?.let { updates["foto"] = it }

                                app.profileRepo.updateProfile(userId, updates).fold(
                                    onSuccess = {
                                        // Update candidato com CPF e cidade
                                        val candidatoUpdates = mapOf(
                                            "cpf" to cpf.replace(Regex("[^0-9]"), ""),
                                            "celular" to celular.replace(Regex("[^0-9]"), ""),
                                            "cidade" to cidade
                                        )
                                        app.candidatoRepo.updateCandidato(userId, candidatoUpdates)
                                        
                                        isLoading = false
                                        onComplete()
                                    },
                                    onFailure = { e ->
                                        error = "Erro ao salvar: ${e.message}"
                                        isLoading = false
                                    }
                                )
                            } catch (e: Exception) {
                                error = "Erro inesperado: ${e.message}"
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Salvar e Continuar", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ==================== HELPERS ====================

private fun formatCPF(raw: String): String {
    val digits = raw.replace(Regex("[^0-9]"), "")
    return when {
        digits.length <= 3 -> digits
        digits.length <= 6 -> "${digits.substring(0, 3)}.${digits.substring(3)}"
        digits.length <= 9 -> "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${digits.substring(6)}"
        else -> "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${digits.substring(6, 9)}-${digits.substring(9, minOf(11, digits.length))}"
    }
}

private fun formatPhone(raw: String): String {
    val digits = raw.replace(Regex("[^0-9]"), "")
    return when {
        digits.length <= 2 -> digits
        digits.length <= 7 -> "(${digits.substring(0, 2)}) ${digits.substring(2)}"
        digits.length <= 11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
        else -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7, 11)}"
    }
}

private fun validateForm(nome: String, cpf: String, celular: String, cidade: String): Boolean {
    return nome.isNotBlank() &&
           cpf.replace(Regex("[^0-9]"), "").length == 11 &&
           celular.replace(Regex("[^0-9]"), "").length >= 10 &&
           cidade.isNotBlank()
}
