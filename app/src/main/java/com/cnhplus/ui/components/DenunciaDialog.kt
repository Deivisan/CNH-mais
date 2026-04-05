package com.cnhplus.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.*

/**
 * Dialog para enviar denúncia de aula
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DenunciaDialog(
    aulaId: String,
    denunciadoNome: String = "",
    onDismiss: () -> Unit,
    onSubmit: (motivo: String, descricao: String, midiaUri: Uri?) -> Unit
) {
    var motivo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var midiaUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val motivos = listOf(
        "Comportamento inadequado",
        "Problema de segurança",
        "Qualidade da aula",
        "Atraso excessivo",
        "Veículo em más condições",
        "Outro"
    )
    
    // File picker para mídia
    val mediaPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        midiaUri = uri
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ReportProblem,
                    contentDescription = null,
                    tint = Error,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Reportar Problema")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Info
                if (denunciadoNome.isNotBlank()) {
                    Text(
                        text = "Aula com: $denunciadoNome",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                // Erro
                error?.let { msg ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = msg,
                            color = Error,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                // Motivo (dropdown)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = motivo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Motivo *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        motivos.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m) },
                                onClick = {
                                    motivo = m
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Descrição
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição *") },
                    placeholder = { Text("Descreva o que aconteceu...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Upload de mídia
                Text(
                    text = "Anexo (opcional)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (midiaUri != null) {
                    // Mostrar arquivo selecionado
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Success.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, null, tint = Success)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Arquivo selecionado",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { midiaUri = null }) {
                                Icon(Icons.Default.Close, null, tint = Error)
                            }
                        }
                    }
                } else {
                    // Botão para selecionar arquivo
                    OutlinedButton(
                        onClick = { mediaPicker.launch("image/*,video/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AttachFile, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Anexar Foto ou Vídeo")
                    }
                }
                
                Text(
                    text = "Formatos aceitos: JPG, PNG, MP4 (máx 10MB)",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validar
                    when {
                        motivo.isBlank() -> error = "Selecione um motivo"
                        descricao.length < 10 -> error = "Descreva o problema (mínimo 10 caracteres)"
                        else -> {
                            onSubmit(motivo, descricao, midiaUri)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Error)
            ) {
                Text("Enviar Denúncia")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Versão simplificada do dialog de denúncia (para uso rápido)
 */
@Composable
fun DenunciaSimpleDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.ReportProblem, null, tint = Error) },
        title = { Text("Confirmar Denúncia") },
        text = {
            Text("Você está prestes a enviar uma denúncia. Esta ação será analisada pela nossa equipe.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Error)
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
