package com.cnhplus.data

// ==================== DATA MODELS ====================

data class Candidato(
    val id: String = "1",
    val nome: String = "João Silva",
    val email: String = "joao@email.com",
    val foto: String? = null,
    val cpf: String = "123.456.789-00",
    val celular: String = "(71) 99999-0000",
    val cidade: String = "Feira de Santana",
    val estado: String = "BA",
    val renach: String? = "RN123456789",
    val perfil: PerfilCandidato = PerfilCandidato(),
    val pacote: PacoteCandidato = PacoteCandidato(),
    val instrutorId: String? = "1"
)

data class PerfilCandidato(
    val abriuProcesso: Boolean = true,
    val passouTeorica: Boolean = true,
    val experiencia: String = "nunca",
    val ansiedade: String = "media",
    val reprovou: Boolean = false,
    val maiorDificuldade: List<String> = listOf("baliza"),
    val objetivo: String = "aprender_calma",
    val temCarro: String = "nao"
)

data class PacoteCandidato(
    val aulasCompradas: Int = 20,
    val aulasRestantes: Int = 15,
    val aulasRecomendadas: Int = 20
)

data class Instrutor(
    val id: String = "1",
    val nome: String = "Carlos Silva",
    val email: String = "carlos@email.com",
    val foto: String? = null,
    val cpf: String = "987.654.321-00",
    val telefone: String = "(71) 88888-0000",
    val cidade: String = "Cuiabá",
    val estado: String = "MT",
    val biografia: String = "Especialista em alunos iniciantes e pessoas com ansiedade. 8 anos de experiência.",
    val estilo: List<String> = listOf("calmo", "motivador"),
    val especialidades: List<String> = listOf("iniciante", "ansioso"),
    val regioes: List<String> = listOf("Centro", "Norte", "Sul"),
    val veiculo: Veiculo = Veiculo(),
    val horasTrabalhadas: Int = 1250,
    val alunosAtendidos: Int = 340,
    val pontualidade: Float = 98f,
    val cancelamentos: String = "baixo",
    val notaMedia: Float = 4.9f,
    val verificado: Boolean = true,
    val status: String = "ativo"
)

data class Veiculo(
    val tipo: String = "carro_proprio",
    val modelo: String = "Chevrolet Onix",
    val ano: String = "2023",
    val temPedal: Boolean = true
)

data class Aula(
    val id: String = "1",
    val candidatoId: String = "1",
    val instrutorId: String = "1",
    val dataHora: String = "2026-03-30T14:00:00",
    val duracao: Int = 50,
    val tipoVeiculo: String = "carro_instrutor",
    val status: String = "agendada",
    val valor: Double = 120.00,
    val confirmacaoCandidato: Boolean = false
)

// ==================== EMULATED DATA ====================

object EmulatedData {
    
    val candidatos = listOf(
        Candidato(
            id = "1",
            nome = "João Silva",
            cidade = "Feira de Santana",
            estado = "BA",
            perfil = PerfilCandidato(
                abriuProcesso = true,
                passouTeorica = true,
                experiencia = "nunca",
                ansiedade = "media",
                temCarro = "nao"
            ),
            pacote = PacoteCandidato(20, 15, 20)
        ),
        Candidato(
            id = "2",
            nome = "Maria Santos",
            cidade = "Salvador",
            estado = "BA",
            perfil = PerfilCandidato(
                abriuProcesso = true,
                passouTeorica = true,
                experiencia = "poucas_vezes",
                ansiedade = "alta",
                temCarro = "sim"
            ),
            pacote = PacoteCandidato(15, 12, 15)
        ),
        Candidato(
            id = "3",
            nome = "Pedro Oliveira",
            cidade = "Belo Horizonte",
            estado = "MG",
            perfil = PerfilCandidato(
                abriuProcesso = true,
                passouTeorica = false,
                experiencia = "nunca",
                ansiedade = "baixa",
                temCarro = "as_vezes"
            ),
            pacote = PacoteCandidato(25, 25, 25)
        )
    )
    
    val instrutores = listOf(
        Instrutor(
            id = "1",
            nome = "Carlos Silva",
            cidade = "Cuiabá",
            estado = "MT",
            biografia = "Especialista em alunos iniciantes e pessoas com ansiedade. 8 anos de experiência.",
            estilo = listOf("calmo", "motivador"),
            especialidades = listOf("iniciante", "ansioso"),
            horasTrabalhadas = 1250,
            alunosAtendidos = 340,
            pontualidade = 98f,
            notaMedia = 4.9f,
            verificado = true,
            status = "ativo"
        ),
        Instrutor(
            id = "2",
            nome = "Maria Santos",
            cidade = "Feira de Santana",
            estado = "BA",
            biografia = "Expert em baliza e provas práticas. Metodologia focada na aprovação.",
            estilo = listOf("objetivo", "rigor"),
            especialidades = listOf("baliza", "prova_pratica"),
            horasTrabalhadas = 2100,
            alunosAtendidos = 520,
            pontualidade = 95f,
            notaMedia = 4.8f,
            verificado = true,
            status = "ativo"
        ),
        Instrutor(
            id = "3",
            nome = "Pedro Oliveira",
            cidade = "Belo Horizonte",
            estado = "MG",
            biografia = "Atende alunos com carro próprio. Flexibilidade de horários e regiões.",
            estilo = listOf("calmo"),
            especialidades = listOf("carro_proprio"),
            horasTrabalhadas = 980,
            alunosAtendidos = 215,
            pontualidade = 92f,
            notaMedia = 4.7f,
            verificado = true,
            status = "ativo"
        ),
        Instrutor(
            id = "4",
            nome = "Ana Costa",
            cidade = "São Paulo",
            estado = "SP",
            biografia = "Instrutora especializada em direção defensiva e estradas.",
            estilo = listOf("motivador"),
            especialidades = listOf("estrada", "defensiva"),
            horasTrabalhadas = 3200,
            alunosAtendidos = 890,
            pontualidade = 99f,
            notaMedia = 4.9f,
            verificado = true,
            status = "ativo"
        ),
        Instrutor(
            id = "5",
            nome = "José Santos",
            cidade = "Rio de Janeiro",
            estado = "RJ",
            biografia = "Especialista em alunos que reprovaram. Metodologia paciente.",
            estilo = listOf("calmo", "paciente"),
            especialidades = listOf("reprovado", "iniciante"),
            horasTrabalhadas = 4500,
            alunosAtendidos = 1200,
            pontualidade = 97f,
            notaMedia = 4.8f,
            verificado = true,
            status = "ativo"
        )
    )
    
    val aulas = listOf(
        Aula(
            id = "1",
            candidatoId = "1",
            instrutorId = "1",
            dataHora = "2026-03-30T14:00:00",
            duracao = 50,
            tipoVeiculo = "carro_instrutor",
            status = "agendada",
            valor = 120.00,
            confirmacaoCandidato = false
        ),
        Aula(
            id = "2",
            candidatoId = "1",
            instrutorId = "1",
            dataHora = "2026-03-28T10:00:00",
            duracao = 50,
            tipoVeiculo = "carro_instrutor",
            status = "concluida",
            valor = 120.00,
            confirmacaoCandidato = true
        ),
        Aula(
            id = "3",
            candidatoId = "2",
            instrutorId = "2",
            dataHora = "2026-03-31T09:00:00",
            duracao = 50,
            tipoVeiculo = "carro_aluno",
            status = "agendada",
            valor = 100.00,
            confirmacaoCandidato = true
        )
    )
    
    val estatisticasAdmin = mapOf(
        "totalInstrutores" to 500,
        "instrutoresAtivos" to 435,
        "taxaAprovacao" to 87,
        "totalAlunos" to 10000,
        "alunosFormados" to 9200,
        "taxaPrimeiraTentativa" to 92,
        "notaMedia" to 4.8f,
        "totalAvaliacoes" to 2500,
        "aulasRealizadas" to 45000,
        "receitaTotal" to 5400000.0
    )
}
