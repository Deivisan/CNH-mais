package com.cnhplus.data.remote.mappers

import com.cnhplus.data.DiasTurnos
import com.cnhplus.data.InstrutorDto
import com.cnhplus.data.Veiculo as VeiculoDto
import com.cnhplus.domain.model.Disponibilidade
import com.cnhplus.domain.model.DiaSemana
import com.cnhplus.domain.model.Especialidade
import com.cnhplus.domain.model.EstiloEnsino
import com.cnhplus.domain.model.Instrutor
import com.cnhplus.domain.model.NivelCancelamento
import com.cnhplus.domain.model.StatusInstrutor
import com.cnhplus.domain.model.TipoVeiculo
import com.cnhplus.domain.model.Turno
import com.cnhplus.domain.model.Veiculo

/**
 * Mapper para converter InstrutorDTO em Domain Model.
 */
object InstrutorMapper {

    fun toDomain(dto: InstrutorDto): Instrutor {
        return Instrutor(
            id = dto.id ?: "",
            nome = null, // Vem do profile
            email = null, // Vem do profile
            cpf = dto.cpf,
            telefone = dto.telefone,
            cidade = dto.cidade,
            biografia = dto.biografia,
            estilo = dto.estilo?.mapNotNull { estilo ->
                try {
                    EstiloEnsino.valueOf(estilo.uppercase())
                } catch (e: IllegalArgumentException) {
                    null
                }
            } ?: emptyList(),
            especialidades = dto.especialidades?.mapNotNull { esp ->
                try {
                    Especialidade.valueOf(esp.uppercase())
                } catch (e: IllegalArgumentException) {
                    null
                }
            } ?: emptyList(),
            regioes = dto.regioes ?: emptyList(),
            disponibilidade = dto.getDisponibilidade().let { toDomainDisponibilidade(it) },
            veiculo = dto.getVeiculo().let { toDomainVeiculo(it) },
            notaMedia = dto.nota_media ?: 0.0,
            pontualidade = dto.pontualidade ?: 100.0,
            cancelamentos = dto.cancelamentos?.let {
                try {
                    NivelCancelamento.valueOf(it.uppercase())
                } catch (e: IllegalArgumentException) {
                    NivelCancelamento.BAIXO
                }
            } ?: NivelCancelamento.BAIXO,
            horasTrabalhadas = dto.horas_trabalhadas ?: 0,
            alunosAtendidos = dto.alunos_atendidos ?: 0,
            status = dto.status?.let {
                try {
                    StatusInstrutor.valueOf(it.uppercase())
                } catch (e: IllegalArgumentException) {
                    StatusInstrutor.PENDENTE
                }
            } ?: StatusInstrutor.PENDENTE,
            verificado = dto.verificado ?: false,
            fotoUrl = null
        )
    }

    private fun toDomainDisponibilidade(dto: DiasTurnos): Disponibilidade {
        return Disponibilidade(
            dias = dto.dias.map { dia ->
                when(dia.lowercase()) {
                    "segunda" -> DiaSemana.SEGUNDA
                    "terca" -> DiaSemana.TERCA
                    "quarta" -> DiaSemana.QUARTA
                    "quinta" -> DiaSemana.QUINTA
                    "sexta" -> DiaSemana.SEXTA
                    "sabado" -> DiaSemana.SABADO
                    "domingo" -> DiaSemana.DOMINGO
                    else -> DiaSemana.SEGUNDA
                }
            },
            turnos = dto.turnos.map { turno ->
                when(turno.lowercase()) {
                    "manha" -> Turno.MANHA
                    "tarde" -> Turno.TARDE
                    "noite" -> Turno.NOITE
                    else -> Turno.MANHA
                }
            }
        )
    }

    private fun toDomainVeiculo(dto: VeiculoDto): Veiculo {
        return Veiculo(
            tipo = try {
                TipoVeiculo.valueOf(dto.tipo.uppercase())
            } catch (e: IllegalArgumentException) {
                TipoVeiculo.CARRO_PROPRIO
            },
            modelo = dto.modelo,
            ano = dto.ano,
            temPedal = dto.temPedal ?: true
        )
    }
}
