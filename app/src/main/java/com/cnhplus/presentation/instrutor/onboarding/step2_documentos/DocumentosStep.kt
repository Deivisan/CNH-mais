package com.cnhplus.presentation.instrutor.onboarding.step2_documentos

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Shapes
import com.cnhplus.ui.theme.Success

import com.cnhplus.presentation.instrutor.onboarding.InstrutorWizardViewModel

/**
 * Step 2: Documentos do Instrutor
 */
@Composable
fun DocumentosStep(
    viewModel: InstrutorWizardViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    // File pickers - agora atualizam tanto URI quanto Bytes no ViewModel
    val cnhLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> 
        uri?.let { 
            viewModel.cnhUri = it
            // Converter URI para ByteArray
            viewModel.cnhBytes = readBytesFromUri(context, it)
        }
    }
    
    val crlvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> 
        uri?.let { 
            viewModel.crlvUri = it
            // Converter URI para ByteArray
            viewModel.crlvBytes = readBytesFromUri(context, it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress
        LinearProgressIndicator(
            progress = { 0.4f },
            modifier = Modifier.fillMaxWidth(),
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Documentos (2/5)",
            style = MaterialTheme.typography.titleLarge,
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Envie seus documentos obrigatórios",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // CNH
        DocumentUploadCard(
            title = "CNH (Frente e Verso)",
            icon = Icons.Default.Description,
            isUploaded = viewModel.cnhUri != null,
            onClick = { cnhLauncher.launch("image/*") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // CRLV
        DocumentUploadCard(
            title = "CRLV (Documento do Veículo)",
            icon = Icons.Default.DirectionsCar,
            isUploaded = viewModel.crlvUri != null,
            onClick = { crlvLauncher.launch("image/*") }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Formatos aceitos: JPG, PNG, PDF (máx 5MB cada)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                shape = Shapes.large
            ) {
                Icon(Icons.Default.ArrowBack, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voltar")
            }
            
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                shape = Shapes.large,
                enabled = viewModel.cnhUri != null && viewModel.crlvUri != null
            ) {
                Text("Próximo")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun DocumentUploadCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isUploaded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isUploaded) Success.copy(alpha = 0.1f) 
                          else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isUploaded) Success 
                       else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                Text(
                    text = if (isUploaded) "Enviado ✓" else "Toque para enviar",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isUploaded) Success 
                           else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = if (isUploaded) Icons.Default.CheckCircle 
                             else Icons.Default.Upload,
                contentDescription = null,
                tint = if (isUploaded) Success 
                       else MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Função helper para ler bytes de uma URI
 */
private fun readBytesFromUri(context: android.content.Context, uri: Uri): ByteArray? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
