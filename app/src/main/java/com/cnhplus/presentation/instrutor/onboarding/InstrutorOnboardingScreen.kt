package com.cnhplus.presentation.instrutor.onboarding

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cnhplus.presentation.instrutor.onboarding.step1_dados_pessoais.DadosPessoaisStep
import com.cnhplus.presentation.instrutor.onboarding.step2_documentos.DocumentosStep
import com.cnhplus.presentation.instrutor.onboarding.step3_veiculo.VeiculoStep
import com.cnhplus.presentation.instrutor.onboarding.step4_disponibilidade.DisponibilidadeStep
import com.cnhplus.presentation.instrutor.onboarding.step5_especialidades.EspecialidadesStep

/**
 * Tela principal do Wizard de Onboarding do Instrutor.
 * Integra os 5 steps com navegação e estado compartilhado.
 */
@Composable
fun InstrutorOnboardingScreen(
    onComplete: () -> Unit,
    viewModel: InstrutorWizardViewModel = hiltViewModel()
) {
    val currentStep = viewModel.currentStep
    val error = viewModel.error
    val isLoading = viewModel.isLoading

    // Mostrar erro se houver
    error?.let { errorMsg ->
        AlertDialog(
            onDismissRequest = { viewModel.error = null },
            title = { Text(text = "Erro") },
            text = { Text(text = errorMsg) },
            confirmButton = {
                TextButton(onClick = { viewModel.error = null }) {
                    Text("OK")
                }
            }
        )
    }

    // Mostrar loading
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Conteúdo baseado no step atual
    when (currentStep) {
        1 -> DadosPessoaisStep(
            nome = viewModel.nome,
            onNomeChange = { viewModel.nome = it },
            cpf = viewModel.cpf,
            onCpfChange = { viewModel.cpf = it },
            telefone = viewModel.telefone,
            onTelefoneChange = { viewModel.telefone = it },
            cidade = viewModel.cidade,
            onCidadeChange = { viewModel.cidade = it },
            biografia = viewModel.biografia,
            onBiografiaChange = { viewModel.biografia = it },
            fotoBytes = viewModel.fotoBytes,
            onFotoSelected = { viewModel.fotoBytes = it },
            onNext = { viewModel.nextStep() }
        )
        
        2 -> DocumentosStep(
            cnhUri = viewModel.cnhUri,
            onCnhSelected = { uri: Uri -> viewModel.cnhUri = uri },
            crlvUri = viewModel.crlvUri,
            onCrlvSelected = { uri: Uri -> viewModel.crlvUri = uri },
            onNext = { viewModel.nextStep() },
            onBack = { viewModel.previousStep() }
        )
        
        3 -> VeiculoStep(
            tipoVeiculo = viewModel.tipoVeiculo,
            onTipoVeiculoChange = { viewModel.tipoVeiculo = it },
            modelo = viewModel.modelo,
            onModeloChange = { viewModel.modelo = it },
            ano = viewModel.ano,
            onAnoChange = { viewModel.ano = it },
            temPedal = viewModel.temPedal,
            onTemPedalChange = { viewModel.temPedal = it },
            onNext = { viewModel.nextStep() },
            onBack = { viewModel.previousStep() }
        )
        
        4 -> DisponibilidadeStep(
            disponibilidade = viewModel.disponibilidade,
            onDisponibilidadeChange = { viewModel.disponibilidade = it },
            onNext = { viewModel.nextStep() },
            onBack = { viewModel.previousStep() }
        )
        
        5 -> {
            val appState = com.cnhplus.ui.theme.LocalAppState.current
            val userId = appState.currentUser.value?.id
            EspecialidadesStep(
                especialidades = viewModel.especialidades.toList(),
                onEspecialidadesChange = { newList ->
                    viewModel.especialidades.clear()
                    viewModel.especialidades.addAll(newList)
                },
                estilo = viewModel.estilo.toList(),
                onEstiloChange = { newList ->
                    viewModel.estilo.clear()
                    viewModel.estilo.addAll(newList)
                },
                onFinalizar = {
                    if (userId != null) {
                        viewModel.finalizarCadastro(userId, onComplete)
                    } else {
                        viewModel.error = "Usuário não autenticado"
                    }
                },
                onBack = { viewModel.previousStep() }
            )
        }
    }
}
