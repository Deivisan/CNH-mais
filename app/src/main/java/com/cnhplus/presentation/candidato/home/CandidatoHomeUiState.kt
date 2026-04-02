package com.cnhplus.presentation.candidato.home

import com.cnhplus.domain.model.Candidato
import com.cnhplus.domain.model.Instrutor
import com.cnhplus.presentation.common.base.UiStateWithStatus

/**
 * Estados da UI para CandidatoHomeScreen.
 */
data class CandidatoHomeUiState(
    override val isLoading: Boolean = false,
    val candidato: Candidato? = null,
    val instrutor: Instrutor? = null,
    val aulasHoje: Int = 0,
    val aulasRestantes: Int = 0,
    val aulasFeitas: Int = 0,
    val progresso: Float = 0f,
    override val error: String? = null
) : UiStateWithStatus

/**
 * Eventos de UI para CandidatoHomeScreen.
 */
sealed class CandidatoHomeEvent {
    data object LoadData : CandidatoHomeEvent()
    data object Refresh : CandidatoHomeEvent()
    data object NavigateToPerfil : CandidatoHomeEvent()
    data object NavigateToAulas : CandidatoHomeEvent()
    data object NavigateToInstrutor : CandidatoHomeEvent()
    data object NavigateToComprar : CandidatoHomeEvent()
}
