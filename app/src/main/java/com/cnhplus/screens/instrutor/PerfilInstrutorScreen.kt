package com.cnhplus.screens.instrutor

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
import com.cnhplus.data.DiasTurnos
import com.cnhplus.data.Veiculo
import kotlinx.coroutines.launch

/**
 * Tela de onboarding para instrutor. Coleta dados + documentos.
 */
@Composable
fun PerfilInstrutorScreen(
    onComplete: () -> Unit,
    onSkip: () -> Unit = onComplete // skip por enquanto
) {
    val app = LocalAppState.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Form state
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var biografia by remember { mutableStateOf("") }
    var photoBytes by remember { mutableStateOf<ByteArray?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Permissões
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Perfil do Instrutor",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

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
                if (!photoPermissions.allPermissionsGranted) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Warning.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Warning)
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    "Permissoes necessarios. Toque para permitir acesso a camera e galeria.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = { photoPermissions.launchPermissionRequest() },
                                modifier = Modifier.height(32.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Warning)
                            ) {
                                Text("Permitir")
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                ImagePickerButton(
                    currentImageUrl = null,
                    onImageSelected = { bytes -> photoBytes = bytes },
                    size = 120
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (photoBytes != null) "Foto selecionada ✓" else "Toque para adicionar foto",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (photoBytes != null) Success else Color.Gray
                )

                Spacer(Modifier.height(32.dp))

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
                    Spacer(Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it; error = null },
                    label = { Text("Nome Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = cpf,
                    onValueChange = { if (it.length <= 14) cpf = formatCPF(it) },
                    label = { Text("CPF") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("000.000.000-00") }
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = telefone,
                    onValueChange = { if (it.length <= 15) telefone = formatPhone(it) },
                    label = { Text("Telefone") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    placeholder = { Text("(00) 00000-0000") }
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = cidade,
                    onValueChange = { cidade = it },
                    label = { Text("Cidade") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = biografia,
                    onValueChange = { if (it.length <= 300) biografia = it },
                    label = { Text("Biografia (max 300 caracteres)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    placeholder = { Text("Conte sua experiencia como instrutor...") }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "${biografia.length}/300",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (biografia.length > 280) Warning else Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Accent.copy(alpha = 0.1f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Documentos necessários (em breve)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Após salvar, voce podera enviar sua CNH e CRLV.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (!validateInstrutorForm(nome, cpf, telefone, cidade, biografia)) {
                            error = "Preencha todos os campos corretamente"
                            return@Button
                        }

                        isLoading = true
                        scope.launch {
                            try {
                                val userId = app.currentUser.value?.id ?: run {
                                    error = "Usuario nao autenticado"
                                    isLoading = false
                                    return@launch
                                }

                                // Upload photo
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
                                val profileUpdates = mutableMapOf<String, Any?>(
                                    "nome" to nome,
                                    "telefone" to telefone
                                )
                                photoUrl?.let { profileUpdates["foto"] = it }

                                app.profileRepo.updateProfile(userId, profileUpdates).fold(
                                    onSuccess = {
                                        // Create/update instrutor record
                                        val instrutorData = mapOf(
                                            "cpf" to cpf.replace(Regex("[^0-9]"), ""),
                                            "telefone" to telefone.replace(Regex("[^0-9]"), ""),
                                            "cidade" to cidade,
                                            "biografia" to biografia,
                                            "status" to "pendente",
                                            "verificado" to false
                                        )
                                        app.instrutorRepo.updateInstrutor(userId, instrutorData)
                                        
                                        // Update role
                                        app.selectRole("instrutor") { _ ->
                                            isLoading = false
                                            onComplete()
                                        }
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

                Spacer(Modifier.height(16.dp))

                TextButton(onClick = onSkip) {
                    Text("Preencher depois", color = TextSecondary)
                }

                Spacer(Modifier.height(32.dp))
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

private fun validateInstrutorForm(
    nome: String,
    cpf: String,
    telefone: String,
    cidade: String,
    biografia: String
): Boolean {
    return nome.isNotBlank() &&
           cpf.replace(Regex("[^0-9]"), "").length == 11 &&
           telefone.replace(Regex("[^0-9]"), "").length >= 10 &&
           cidade.isNotBlank() &&
           biografia.length in 10..300
}
