package com.cnhplus.presentation.instrutor.onboarding.step1_dados_pessoais

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.components.ImagePickerButton
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Shapes

/**
 * Step 1: Dados Pessoais do Instrutor
 */
@Composable
fun DadosPessoaisStep(
    nome: String,
    onNomeChange: (String) -> Unit,
    cpf: String,
    onCpfChange: (String) -> Unit,
    telefone: String,
    onTelefoneChange: (String) -> Unit,
    cidade: String,
    onCidadeChange: (String) -> Unit,
    biografia: String,
    onBiografiaChange: (String) -> Unit,
    fotoBytes: ByteArray?,
    onFotoSelected: (ByteArray) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress indicator
        LinearProgressIndicator(
            progress = 0.2f,
            modifier = Modifier.fillMaxWidth(),
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Dados Pessoais (1/5)",
            style = MaterialTheme.typography.titleLarge,
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Preencha suas informações básicas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Foto
        ImagePickerButton(
            currentImageUrl = null,
            onImageSelected = onFotoSelected,
            size = 120
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (fotoBytes != null) "Foto selecionada ✓" else "Toque para adicionar foto",
            style = MaterialTheme.typography.bodySmall,
            color = if (fotoBytes != null) MaterialTheme.colorScheme.primary 
                   else MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Error
        error?.let {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Nome
        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            label = { Text("Nome Completo *") },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes.medium,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // CPF
        OutlinedTextField(
            value = cpf,
            onValueChange = { 
                if (it.length <= 14) onCpfChange(formatCPF(it)) 
            },
            label = { Text("CPF *") },
            leadingIcon = { Icon(Icons.Default.Badge, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("000.000.000-00") }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Telefone
        OutlinedTextField(
            value = telefone,
            onValueChange = { 
                if (it.length <= 15) onTelefoneChange(formatPhone(it)) 
            },
            label = { Text("Telefone *") },
            leadingIcon = { Icon(Icons.Default.Phone, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            placeholder = { Text("(00) 00000-0000") }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Cidade
        OutlinedTextField(
            value = cidade,
            onValueChange = onCidadeChange,
            label = { Text("Cidade *") },
            leadingIcon = { Icon(Icons.Default.LocationOn, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes.medium,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Biografia
        OutlinedTextField(
            value = biografia,
            onValueChange = { 
                if (it.length <= 300) onBiografiaChange(it) 
            },
            label = { Text("Biografia (máx 300 caracteres)") },
            leadingIcon = { Icon(Icons.Default.Description, null) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = Shapes.medium,
            placeholder = { Text("Conte sua experiência como instrutor...") }
        )
        
        Text(
            text = "${biografia.length}/300",
            style = MaterialTheme.typography.bodySmall,
            color = if (biografia.length > 280) MaterialTheme.colorScheme.error 
                   else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Botão Próximo
        Button(
            onClick = {
                when {
                    nome.isBlank() -> error = "Nome é obrigatório"
                    cpf.filter { it.isDigit() }.length != 11 -> error = "CPF inválido"
                    telefone.filter { it.isDigit() }.length < 10 -> error = "Telefone inválido"
                    cidade.isBlank() -> error = "Cidade é obrigatória"
                    else -> {
                        error = null
                        onNext()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = Shapes.large
        ) {
            Text("Próximo")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, null)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Helper functions
private fun formatCPF(raw: String): String {
    val digits = raw.filter { it.isDigit() }
    return when {
        digits.length <= 3 -> digits
        digits.length <= 6 -> "${digits.take(3)}.${digits.drop(3)}"
        digits.length <= 9 -> "${digits.take(3)}.${digits.drop(3).take(3)}.${digits.drop(6)}"
        else -> "${digits.take(3)}.${digits.drop(3).take(3)}.${digits.drop(6).take(3)}-${digits.drop(9).take(2)}"
    }
}

private fun formatPhone(raw: String): String {
    val digits = raw.filter { it.isDigit() }
    return when {
        digits.length <= 2 -> digits
        digits.length <= 7 -> "(${digits.take(2)}) ${digits.drop(2)}"
        digits.length <= 11 -> "(${digits.take(2)}) ${digits.drop(2).take(5)}-${digits.drop(7)}"
        else -> "(${digits.take(2)}) ${digits.drop(2).take(5)}-${digits.drop(7).take(4)}"
    }
}
