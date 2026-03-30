# Sistema de Agenda do Instrutor

## Visão Geral

A aba é apresentada em formato de calendário interativo, semelhante a um Google Calendar.

### Visualizações
- [ ] Diária
- [ ] Semanal
- [ ] Mensal

### Diferenciação Visual
- [ ] Horário disponível
- [ ] Horário bloqueado
- [ ] Aula agendada
- [ ] Aula concluída

---

## Configuração da Agenda

O instrutor é 100% responsável por abrir ou bloquear seus horários. O aplicativo não cria horários automaticamente.

### A. Abrir Horários Disponíveis

Ao clicar em um dia e horário, define:
- [ ] Horário de início e término
- [ ] Duração da aula
- [ ] **Modo de atendimento (VEÍCULO):**
  - [ ] Atender somente com o carro do instrutor
  - [ ] Atender somente com o carro do aluno
  - [ ] Atender com o carro do instrutor ou com o carro do aluno

**Nota:** Essa escolha fica visível para o candidato no momento do agendamento.

### B. Bloquear Horários NÃO Disponíveis

O instrutor pode marcar como bloqueado:
- [ ] Horários pessoais
- [ ] Folgas
- [ ] Compromissos
- [ ] Dias que não deseja trabalhar

**Nota:** Esses horários não aparecem para candidatos.

---

## Regra sobre Uso do Carro do Instrutor

Sempre que selecionar "Atender com o carro do instrutor" (exclusivo ou combinado):

### A. TERMO DE RESPONSABILIDADE

O instrutor precisa aceitar o termo para liberar esse tipo de atendimento.

### Conteúdo do Termo
O instrutor declara que:
- [ ] O veículo é de sua responsabilidade
- [ ] Questões de seguro, manutenção, danos, acidentes ou infrações são de sua inteira responsabilidade
- [ ] O aplicativo atua exclusivamente como intermediador da aula
- [ ] O aplicativo não se responsabiliza por quaisquer eventos decorrentes do uso do veículo

**Sem aceitar o termo, não é possível abrir horários com carro próprio.**

### Registro
- [ ] O aceite fica registrado no sistema

---

## Lógica do Veículo na Agenda

O instrutor pode:
- [ ] Trabalhar somente com o carro dele
- [ ] Trabalhar somente com o carro do aluno
- [ ] Trabalhar de forma mista, escolhendo isso a cada horário

**A escolha é feita horário por horário, não global.**

---

## Projeção Automática de Ganhos

Toda vez que abre um horário disponível, o sistema recalcula:

### A. Projeção Diária
- [ ] Quantidade de aulas abertas no dia
- [ ] Valor estimado se todas forem preenchidas

### B. Projeção Semanal
- [ ] Total de aulas abertas na semana
- [ ] Ganho estimado da semana cheia

### C. Projeção Mensal
- [ ] Total de aulas abertas no mês
- [ ] Ganho estimado do mês cheio

### Exemplo Exibido
```
Hoje: R$ XXX
Semana: R$ XXXX
Mês: R$ XXXXX
```

### Mensagem Fixa
> "Os valores exibidos são projeções. O ganho real depende das aulas agendadas e concluídas."

---

## Integração com Status Financeiro

| Status | Efeito Financeiro |
|--------|------------------|
| Horário aberto | Entra na projeção |
| Aula agendada | Valor previsto |
| Aula concluída + confirmada | Saldo disponível |
| Aula cancelada | Sai da projeção |

---

## Incentivos Inteligentes

O sistema pode exibir avisos automáticos:
- [ ] "Você tem horários livres amanhã."
- [ ] "Se abrir mais 2 horários nesta semana, sua projeção sobe para R$ X."
- [ ] "Sua taxa de ocupação está alta. Considere abrir novos horários."

**Nota:** São estímulos visuais, não obrigações.

---

## Regras e Limites

O instrutor não pode:
- [ ] Criar aulas retroativas
- [ ] Alterar valores das aulas
- [ ] Forçar conclusão sem confirmação do aluno
- [ ] Burlar o termo de uso do veículo

---

## Benefício Psicológico

Essa estrutura:
- [ ] Dá autonomia real ao instrutor
- [ ] Organiza a rotina de trabalho
- [ ] Cria motivação financeira clara
- [ ] Reduz conflitos e cancelamentos
- [ ] Protege juridicamente o aplicativo

---

## Resumo Técnico

> "O instrutor controla sua agenda em calendário, define por horário se atende com carro próprio ou do aluno, aceita termo obrigatório quando usa seu veículo, e visualiza em tempo real a projeção de ganhos diária, semanal e mensal."

---

**Última atualização:** 30/03/2026
**Projeto:** CNH+