package com.cnhplus.domain.model

/**
 * Domain model para Candidato.
 * Diferente do DTO, não tem annotations de serialização.
 */
data class Candidato(
    val id: String,
    val nome: String? = null,
    val email: String? = null,
    val cpf: String? = null,
    val celular: String? = null,
    val cidade: String? = null,
    val bairro: String? = null,
    val renach: String? = null,
    val perfil: PerfilCandidato = PerfilCandidato(),
    val pacote: PacoteCandidato = PacoteCandidato(),
    val instrutorId: String? = null,
    val fotoUrl: String? = null
) {
    val aulasFeitas: Int
        get() = pacote.aulasCompradas - pacote.aulasRestantes
    
    val progresso: Float
        get() = if (pacote.aulasCompradas > 0) {
            aulasFeitas.toFloat() / pacote.aulasCompradas
        } else 0f
}

data class PerfilCandidato(
    val abriuProcesso: Boolean = false,
    val passouTeorica: Boolean = false,
    val experiencia: Experiencia = Experiencia.NUNCA,
    val ansiedade: Ansiedade = Ansiedade.BAIXA,
    val reprovou: Boolean = false,
    val maiorDificuldade: List<Dificuldade> = emptyList(),
    val objetivo: Objetivo = Objetivo.APRENDER_CALMA,
    val temCarro: TemCarro = TemCarro.NAO,
    val disponibilidade: Disponibilidade = Disponibilidade()
)

data class PacoteCandidato(
    val aulasCompradas: Int = 0,
    val aulasRestantes: Int = 0,
    val aulasRecomendadas: Int = 0
)

data class Disponibilidade(
    val dias: List<DiaSemana> = emptyList(),
    val turnos: List<Turno> = emptyList()
)

enum class Experiencia { NUNCA, POUCAS_VEZES, FREQUENTE }
enum class Ansiedade { BAIXA, MEDIA, ALTA }
enum class Dificuldade { BALIZA, CONTROLE, VELOCIDADE, ROTATORIAS }
enum class Objetivo { APRENDER_CALMA, PASSAR_RAPIDO, FICAR_BOM }
enum class TemCarro { SIM, NAO, AS_VEZES }
enum class DiaSemana { SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO, DOMINGO }
enum class Turno { MANHA, TARDE, NOITE }
