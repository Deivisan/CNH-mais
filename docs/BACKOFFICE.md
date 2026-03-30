# Backoffice - Gerenciamento do Instrutor

## Visão Geral

Tela de Gerenciamento / Painel Administrativo

**Objetivo:** Centralizar TUDO sobre o instrutor em um único lugar.

**Permite:**
- [ ] Auditoria
- [ ] Tomada de decisão
- [ ] Resolução de disputas
- [ ] Proteção jurídica
- [ ] Controle de conduta

**Nota:** Esta tela não aparece para instrutor nem para candidato. É exclusivamente administrativa.

---

## Bloco 1 - Identificação do Instrutor

Campos fixos no topo.

| Campo | Descrição |
|-------|-----------|
| ID interno | Identificação única |
| Nome completo | Nome cadastrado |
| CPF | Mascarado |
| Data de nascimento | Data de nascimento |
| Cidade / UF | Localização |
| Data de cadastro | Quando se registrou |
| Data de aprovação | Quando foi aprovado |
| Status atual | ativo / suspenso / bloqueado |
| Motivo do status | Campo texto |
| Última atividade | Data/hora no app |

---

## Bloco 2 - Status Operacional

Controle rápido.

| Campo | Opções |
|-------|--------|
| Perfil público | visível / oculto |
| Agenda | liberada / bloqueada |
| Financeiro | normal / travado |
| Permissão para usar carro próprio | sim / não |
| Termo de responsabilidade do carro | aceito (data/hora) / não aceito |

---

## Bloco 3 - Documentos do Instrutor

Upload + histórico.

Para cada documento:

| Campo | Descrição |
|-------|-----------|
| Tipo do documento | RG, CNH, Comprovante, etc. |
| Arquivo atual | Upload atual |
| Data de envio | Quando foi enviado |
| Status | aprovado / pendente / reprovado |
| Motivo da reprovação | Se reprovado |
| Histórico de versões | Log de alterações |

### Documentos Listados
- [ ] RG ou CNH
- [ ] Selfie de validação
- [ ] Comprovação de instrutor
- [ ] Outros documentos (campo flexível)

---

## Bloco 4 - Perfil Editável (Visão Admin)

O que o instrutor preenchendo (visualização).

| Campo | Descrição |
|-------|-----------|
| Foto de perfil atual | Imagem atual |
| Biografia | Texto de apresentação |
| Estilo de ensino | Tags selecionadas |
| Especialidades | Tags selecionadas |
| Regiões atendidas | Áreas de atendimento |
| Tipo de atendimento | carro do instrutor / carro do aluno / ambos |

### Currículo (Autoescolas Anteriores)
- [ ] Nome da autoescola
- [ ] Cidade
- [ ] Período

**Nota:** Admin pode visualizar tudo, não edita direto, só solicita correção.

---

## Bloco 5 - Agenda (Visão Admin)

Resumo operacional.

| Campo | Descrição |
|-------|-----------|
| Total de horários abertos | Quantidade |
| Total de horários bloqueados | Quantidade |
| Taxa de ocupação | Porcentagem |

### Histórico de Alterações
- [ ] Data
- [ ] O que mudou

### Ações
- [ ] Bloquear agenda
- [ ] Liberar agenda

---

## Bloco 6 - Aulas

Lista completa de aulas.

Para cada aula:

| Campo | Descrição |
|-------|-----------|
| ID da aula | Identificação única |
| Data e horário | Quando ocorreu |
| Tipo | carro instrutor / carro aluno |
| Status | agendada / concluída / cancelada / em disputa |
| Valor da aula | Valor em R$ |
| Confirmação do aluno | sim / não |
| Observações | Se houver |

---

## Bloco 7 - Financeiro (Interno)

**Nota:** Não é o que o instrutor vê.

| Campo | Descrição |
|-------|-----------|
| Total faturado histórico | Todo valor já recebido |
| Total pago | Quanto já foi depositado |
| Total pendente | Valor ainda não liberado |
| Total estornado | Valor reembolsado |
| Saldo bloqueado | Valor retido |
| Frequência de repasse | diária / semanal / mensal |

### Dados Bancários Atuais
- [ ] Banco
- [ ] Tipo (Pix/conta)
- [ ] Titular

### Histórico de Alterações Bancárias
- [ ] Data
- [ ] Dado antigo
- [ ] Dado novo

---

## Bloco 8 - Avaliações & Conduta

Controle de risco.

| Campo | Descrição |
|-------|-----------|
| Nota média geral | Média de estrelas |
| Total de avaliações | Quantidade |

### Avaliações
- [ ] Lista de avaliações
- [ ] Avaliações sinalizadas (flags):
  - ofensa
  - conduta inadequada
  - atraso recorrente
- [ ] Denúncias recebidas
- [ ] Status de risco: baixo / médio / alto

---

## Bloco 9 - Disputas

Histórico jurídico-operacional.

Para cada disputa:

| Campo | Descrição |
|-------|-----------|
| ID da disputa | Identificação |
| Aula relacionada | ID da aula |
| Motivo | Por que abriu |
| Versão do aluno | O que o candidato disse |
| Versão do instrutor | O que o instrutor disse |
| Evidências | Se houver |
| Decisão | pagamento liberado / parcial / estorno |
| Responsável | Quem decidiu |
| Data da decisão | Quando foi decidido |

---

## Bloco 10 - Comunicação & Suporte

Registro formal.

| Campo | Descrição |
|-----|-----------|
| Chamados abertos | Quantos em aberto |
| Chamados encerrados | Quantos resolvidos |
| Mensagens trocadas | Histórico de chat |
| Avisos enviados ao instrutor | Notificações formais |
| Advertências formais | Registros de aviso |

---

## Bloco 11 - Log de Ações (Auditoria)

Registro automático e intocável.

### Campos Registrados
- [ ] Quem fez
- [ ] O que foi feito
- [ ] Quando
- [ ] De onde (opcional)

### Exemplos
- [ ] Alteração de status
- [ ] Bloqueio financeiro
- [ ] Exclusão de horário
- [ ] Decisão de disputa

---

## Bloco 12 - Ações Administrativas

Botões de ação.

| Ação | Descrição |
|------|-----------|
| [ ] Solicitar correção de documento | Pedir novo upload |
| [ ] Suspender instrutor | Temporariamente |
| [ ] Bloquear agenda | Impedir agendamentos |
| [ ] Travar repasse financeiro | Parar pagamentos |
| [ ] Encerrar conta | Definitivo |
| [ ] Registar advertência | Aviso formal |

---

## Resumo Essencial

> "Essa tela centraliza identificação, documentos, perfil, agenda, aulas, financeiro, avaliações, disputas e logs do instrutor, permitindo controle total da operação."

---

## Pensamento Final

Esta tela:
- [ ] NÃO aparece para instrutor
- [ ] NÃO aparece para candidato
- [ ] É obrigatória para escalar
- [ ] Pode ser feia no início, mas não pode ser cega

---

**Última atualização:** 30/03/2026
**Projeto:** CNH+