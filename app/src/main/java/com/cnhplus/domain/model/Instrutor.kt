package com.cnhplus.domain.model

/**
 * Domain model para Instrutor.
 */
data class Instrutor(
    val id: String,
    val nome: String? = null,
    val email: String? = null,
    val cpf: String? = null,
    val telefone: String? = null,
    val cidade: String? = null,
    val biografia: String? = null,
    val estilo: List<EstiloEnsino> = emptyList(),
    val especialidades: List<Especialidade> = emptyList(),
    val regioes: List<String> = emptyList(),
    val disponibilidade: Disponibilidade = Disponibilidade(),
    val veiculo: Veiculo = Veiculo(),
    val notaMedia: Double = 0.0,
    val pontualidade: Double = 100.0,
    val cancelamentos: NivelCancelamento = NivelCancelamento.BAIXO,
    val horasTrabalhadas: Int = 0,
    val alunosAtendidos: Int = 0,
    val status: StatusInstrutor = StatusInstrutor.PENDENTE,
    val verificado: Boolean = false,
    val fotoUrl: String? = null
)

enum class EstiloEnsino { CALMO, OBJETIVO, RIGOR, MOTIVADOR }
enum class Especialidade { INICIANTE, ANSIEDADE, REPROVADO, BALIZA, PRE_PROVA }
enum class NivelCancelamento { BAIXO, MEDIO, ALTO }
enum class StatusInstrutor { PENDENTE, APROVADO, ATIVO, SUSPENSO, BLOQUEADO }

data class Veiculo(
    val tipo: TipoVeiculo = TipoVeiculo.CARRO_PROPRIO,
    val modelo: String? = null,
    val ano: String? = null,
    val temPedal: Boolean = true
)

enum class TipoVeiculo { CARRO_PROPRIO, CARRO_ALUGADO, CARRO_ALUNO, MOTO }
