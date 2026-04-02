package com.cnhplus.presentation.instrutor.onboarding.step5_especialidades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cnhplus.domain.model.Especialidade
import com.cnhplus.domain.model.EstiloEnsino
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Shapes
import com.cnhplus.ui.theme.Success

/**
 * Step 5: Especialidades e Estilo de Ensino
 */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun EspecialidadesStep(
    especialidades: List<Especialidade>,
    onEspecialidadesChange: (List<Especialidade>) -> Unit,
    estilo: List<EstiloEnsino>,
    onEstiloChange: (List<EstiloEnsino>) -> Unit,
    onFinalizar: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress
        LinearProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxWidth(),
            color = Success
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Especialidades (5/5)",
            style = MaterialTheme.typography.titleLarge,
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Finalize seu perfil profissional",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Estilo de ensino
        Text(
            text = "Seu Estilo de Ensino *",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EstiloEnsino.entries.forEach { estiloItem ->
                val isSelected = estiloItem in estilo
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newEstilo = if (isSelected) {
                            estilo - estiloItem
                        } else {
                            estilo + estiloItem
                        }
                        onEstiloChange(newEstilo)
                    },
                    label = { 
                        Text(
                            when (estiloItem) {
                                EstiloEnsino.CALMO -> "Calmo"
                                EstiloEnsino.OBJETIVO -> "Objetivo"
                                EstiloEnsino.RIGOR -> "Rigoroso"
                                EstiloEnsino.MOTIVADOR -> "Motivador"
                            }
                        ) 
                    },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Especialidades
        Text(
            text = "Suas Especialidades *",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Especialidade.entries.forEach { especialidade ->
                val isSelected = especialidade in especialidades
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newEspecialidades = if (isSelected) {
                            especialidades - especialidade
                        } else {
                            especialidades + especialidade
                        }
                        onEspecialidadesChange(newEspecialidades)
                    },
                    label = { 
                        Text(
                            when (especialidade) {
                                Especialidade.INICIANTE -> "Iniciantes"
                                Especialidade.ANSIEDADE -> "Alunos Ansiosos"
                                Especialidade.REPROVADO -> "Reprovados"
                                Especialidade.BALIZA -> "Baliza"
                                Especialidade.PRE_PROVA -> "Pré-Prova"
                            }
                        ) 
                    },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Dica
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Escolha pelo menos 1 estilo e 1 especialidade para ajudar no match com candidatos",
                    style = MaterialTheme.typography.bodySmall
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
                onClick = onFinalizar,
                modifier = Modifier.weight(1f),
                shape = Shapes.large,
                enabled = estilo.isNotEmpty() && especialidades.isNotEmpty()
            ) {
                Icon(Icons.Default.CheckCircle, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Finalizar")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
