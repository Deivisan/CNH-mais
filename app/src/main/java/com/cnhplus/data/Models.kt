package com.cnhplus.data

/**
 * Data models matching the real Supabase database schema.
 * Uses kotlinx.serialization for JSON conversion.
 */

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import kotlinx.serialization.encodeToString
import com.cnhplus.core.di.JsonConfig

// ==================== PROFILE ====================

@Serializable
data class ProfileDto(
    val id: String = "",
    val email: String? = null,
    val role: String = "candidato", // "candidato" | "instrutor" | "admin"
    val nome: String? = null,
    val foto: String? = null,
    val telefone: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== CANDIDATO ====================

@Serializable
data class CandidatoDto(
    val id: String = "",
    val cpf: String? = null,
    val celular: String? = null,
    val cidade: String? = null,
    val bairro: String? = null,
    val renach: String? = null,
    
    @SerialName("perfil")
    val perfilJson: String? = null,
    
    @SerialName("pacote")
    val pacoteJson: String? = null,
    
    val instrutor_id: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
) {
    fun getPerfil(): PerfilCandidato {
        return if (perfilJson != null && perfilJson.isNotEmpty() && perfilJson != "{}") {
            try {
                val json = Json { ignoreUnknownKeys = true }
                json.decodeFromString<PerfilCandidato>(perfilJson)
            } catch (e: Exception) {
                PerfilCandidato()
            }
        } else {
            PerfilCandidato()
        }
    }
    
    fun getPacote(): PacoteCandidato {
        return if (pacoteJson != null && pacoteJson.isNotEmpty()) {
            try {
                val json = Json { ignoreUnknownKeys = true }
                json.decodeFromString<PacoteCandidato>(pacoteJson)
            } catch (e: Exception) {
                PacoteCandidato()
            }
        } else {
            PacoteCandidato()
        }
    }
    
    fun withPerfil(perfil: PerfilCandidato): CandidatoDto {
        val json = Json { encodeDefaults = true }
        return copy(perfilJson = json.encodeToString(perfil))
    }
    
    fun withPacote(pacote: PacoteCandidato): CandidatoDto {
        val json = Json { encodeDefaults = true }
        return copy(pacoteJson = json.encodeToString(pacote))
    }
}

@Serializable
data class PerfilCandidato(
    val abriuProcesso: Boolean = false,
    val passouTeorica: Boolean = false,
    val experiencia: String = "nunca",         // "nunca" | "poucas_vezes" | "frequente"
    val ansiedade: String = "baixa",           // "baixa" | "media" | "alta"
    val reprovou: Boolean = false,
    val maiorDificuldade: List<String> = emptyList(),
    val objetivo: String = "aprender_calma",   // "aprender_calma" | "passar_rapido" | "ficar_bom"
    val temCarro: String = "nao",              // "sim" | "nao" | "as_vezes"
    val disponibilidade: DiasTurnos = DiasTurnos()
)

@Serializable
data class DiasTurnos(
    val dias: List<String> = emptyList(),       // "segunda".."domingo"
    val turnos: List<String> = emptyList()      // "manha" | "tarde" | "noite"
)

@Serializable
data class PacoteCandidato(
    val aulasCompradas: Int = 0,
    val aulasRestantes: Int = 0,
    val aulasRecomendadas: Int = 0
)

// ==================== INSTRUTOR ====================

@Serializable
data class InstrutorDto(
    val id: String = "",
    val cpf: String? = null,
    val telefone: String? = null,
    val cidade: String? = null,
    val biografia: String? = null,
    val estilo: List<String> = emptyList(),
    val especialidades: List<String> = emptyList(),
    val regioes: List<String> = emptyList(),
    
    @SerialName("disponibilidade")
    val disponibilidadeJson: String? = null,
    
    @SerialName("veiculo")
    val veiculoJson: String? = null,
    
    val nota_media: Double? = 0.0,
    val pontualidade: Double? = 100.0,
    val cancelamentos: String? = "baixo",
    val horas_trabalhadas: Int? = 0,
    val alunos_atendidos: Int? = 0,
    val status: String? = "pendente", // "pendente" | "aprovado" | "ativo" | "suspenso" | "bloqueado"
    val verificado: Boolean? = false,
    val created_at: String? = null,
    val updated_at: String? = null
) {
    fun getDisponibilidade(): DiasTurnos {
        return if (disponibilidadeJson != null) {
            try {
                JsonConfig.default.decodeFromString<DiasTurnos>(disponibilidadeJson)
            } catch (e: Exception) {
                DiasTurnos()
            }
        } else DiasTurnos()
    }
    
    fun getVeiculo(): Veiculo {
        return if (veiculoJson != null && veiculoJson != "{}") {
            try {
                JsonConfig.default.decodeFromString<Veiculo>(veiculoJson)
            } catch (e: Exception) {
                Veiculo()
            }
        } else Veiculo()
    }
    
    fun withDisponibilidade(d: DiasTurnos): InstrutorDto {
        return copy(disponibilidadeJson = JsonConfig.default.encodeToString(d))
    }
    
    fun withVeiculo(v: Veiculo): InstrutorDto {
        return copy(veiculoJson = JsonConfig.default.encodeToString(v))
    }
}

@Serializable
data class Veiculo(
    val tipo: String = "carro_proprio", // "carro_proprio" | "carro_alugado" | "carro_aluno" | "moto"
    val modelo: String? = null,
    val ano: String? = null,
    val temPedal: Boolean? = true
)

// ==================== AULA ====================

@Serializable
data class AulaDto(
    val id: String? = null,
    val candidato_id: String? = null,
    val instrutor_id: String? = null,
    val data_hora: String? = null,
    val duracao: Int? = 50,
    val tipo_veiculo: String? = "carro_instrutor",
    val status: String? = "agendada", // "agendada" | "em_andamento" | "concluida" | "cancelada" | "em_disputa"
    val valor: Double? = 0.0,
    val tipo: String? = "pratica_urbana",
    val local_encontro: String? = null,
    val confirmacao_candidato: Boolean? = false,
    val confirmacao_instrutor: Boolean? = false,
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== PAGAMENTO ====================

@Serializable
data class PagamentoDto(
    val id: String? = null,
    val candidato_id: String? = null,
    val valor: Double? = 0.0,
    val status: String? = "pendente", // "pendente" | "aprovado" | "estornado" | "cancelado"
    val tipo: String? = "pacote",
    val pacotes: Int? = 0,
    val mercado_pago_id: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== AVALIACAO ====================

@Serializable
data class AvaliacaoDto(
    val id: String? = null,
    val aula_id: String? = null,
    val candidato_id: String? = null,
    val instrutor_id: String? = null,
    val nota: Int? = 5,
    val comentario: String? = null,
    val created_at: String? = null
)

// ==================== DISPUTA ====================

@Serializable
data class DisputaDto(
    val id: String? = null,
    val aula_id: String? = null,
    val candidato_id: String? = null,
    val instrutor_id: String? = null,
    val motivo: String? = null,
    val descricao: String? = null,
    val versao_candidato: String? = null,
    val versao_instrutor: String? = null,
    val decisao: String? = null,
    val status: String? = "pendente", // "pendente" | "em_analise" | "resolvida" | "rejeitada"
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== MENSAGEM ====================

@Serializable
data class MensagemDto(
    val id: String? = null,
    val aula_id: String? = null,
    val sender_id: String? = null,
    val receiver_id: String? = null,
    val texto: String? = null,
    val lida: Boolean? = false,
    val created_at: String? = null
)

// ==================== DADOS BANCARIOS ====================

@Serializable
data class DadosBancariosDto(
    val id: String? = null,
    val instrutor_id: String? = null,
    val tipo: String? = "pix",
    val titular: String? = null,
    val chave_pix: String? = null,
    val banco: String? = null,
    val agencia: String? = null,
    val conta: String? = null,
    val validado: Boolean? = false,
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== REPASSE ====================

@Serializable
data class RepasseDto(
    val id: String? = null,
    val instrutor_id: String? = null,
    val aula_id: String? = null,
    val valor: Double? = 0.0,
    val taxa_plataforma: Double? = 12.0,
    val status: String? = "pendente", // "pendente" | "processado" | "pago" | "estornado"
    val data_pagamento: String? = null,
    val created_at: String? = null
)

// ==================== BANNERS ====================

@Serializable
data class BannerDto(
    val id: String = "",
    val titulo: String = "",
    val descricao: String? = null,
    val imagem_url: String? = null,
    val link_destino: String? = null,
    val ativo: Boolean = true,
    val ordem: Int = 0,
    val data_inicio: String? = null,
    val data_fim: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== LOCALIDADES ====================

@Serializable
data class LocalidadeDto(
    val id: String = "",
    val aula_id: String = "",
    val endereco: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val referencia: String? = null,
    val confirmado_candidato: Boolean = false,
    val confirmado_instrutor: Boolean = false,
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== DENUNCIAS ====================

@Serializable
data class DenunciaDto(
    val id: String = "",
    val aula_id: String = "",
    val denunciante_id: String = "",
    val motivo: String = "",
    val descricao: String? = null,
    val midia_url: String? = null,
    val midia_tipo: String? = null, // "foto" | "video" | "audio"
    val status: String = "aberta", // "aberta" | "em_analise" | "resolvida" | "fechada"
    val resposta_admin: String? = null,
    val created_at: String? = null,
    val resolved_at: String? = null,
    val updated_at: String? = null
)

// ==================== AVATARES ====================

@Serializable
data class AvatarDto(
    val id: String = "",
    val user_id: String = "",
    val foto_url: String = "",
    val thumbnail_url: String? = null,
    val storage_path: String? = null,
    val tipo_upload: String? = null, // "google" | "custom"
    val created_at: String? = null,
    val updated_at: String? = null
)

// ==================== EMULATED DATA (Admin screens only) ====================

object EmulatedData {
  val candidatos = listOf(
    CandidatoDto(id = "1", cidade = "Feira de Santana"),
    CandidatoDto(id = "2", cidade = "Salvador"),
  )
  val instrutores = listOf(
    InstrutorDto(id = "1", cidade = "Feira de Santana", status = "ativo"),
  )
}
