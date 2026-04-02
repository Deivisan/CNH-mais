package com.cnhplus.presentation.instrutor.onboarding.step4_disponibilidade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cnhplus.domain.model.DiaSemana
import com.cnhplus.domain.model.Disponibilidade
import com.cnhplus.domain.model.Turno
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Shapes

/**
 * Step 4: Disponibilidade do Instrutor
 */
@Composable
fun DisponibilidadeStep(
    disponibilidade: Disponibilidade,
    onDisponibilidadeChange: (Disponibilidade) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val dias = DiaSemana.entries.toTypedArray()
    val turnos = Turno.entries.toTypedArray()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress
        LinearProgressIndicator(
            progress = { 0.8f },
            modifier = Modifier.fillMaxWidth(),
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Disponibilidade (4/5)",
            style = MaterialTheme.typography.titleLarge,
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Selecione seus dias e turnos disponíveis",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Dias da semana
        Text(
            text = "Dias da Semana *",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Column {
            dias.forEach { dia ->
                val isSelected = dia in disponibilidade.dias
                DayCheckbox(
                    day = dia,
                    isSelected = isSelected,
                    onToggle = {
                        val newDias = if (isSelected) {
                            disponibilidade.dias - dia
                        } else {
                            disponibilidade.dias + dia
                        }
                        onDisponibilidadeChange(disponibilidade.copy(dias = newDias))
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Turnos
        Text(
            text = "Turnos *",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            turnos.forEach { turno ->
                val isSelected = turno in disponibilidade.turnos
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newTurnos = if (isSelected) {
                            disponibilidade.turnos - turno
                        } else {
                            disponibilidade.turnos + turno
                        }
                        onDisponibilidadeChange(disponibilidade.copy(turnos = newTurnos))
                    },
                    label = { Text(turno.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }
        
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voltar")
            }
            
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                shape = Shapes.large,
                enabled = disponibilidade.dias.isNotEmpty() && disponibilidade.turnos.isNotEmpty()
            ) {
                Text("Próximo")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun DayCheckbox(
    day: DiaSemana,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val label = when (day) {
        DiaSemana.SEGUNDA -> "Segunda"
        DiaSemana.TERCA -> "Terça"
        DiaSemana.QUARTA -> "Quarta"
        DiaSemana.QUINTA -> "Quinta"
        DiaSemana.SEXTA -> "Sexta"
        DiaSemana.SABADO -> "Sábado"
        DiaSemana.DOMINGO -> "Domingo"
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = isSelected,
                onValueChange = { onToggle() },
                role = Role.Checkbox
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label)
    }
}
