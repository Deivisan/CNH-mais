# Sistema de Bonificações

## Visão Geral

Sistema de recompensas para candidatos e instrutores baseado em performance, indicações e engajamento.

---

## 3.1 - BONIFICAÇÃO DO CANDIDATO

### 1) Ganho por Indicação

**Regra:**
- A cada **10 indicações ativadas** (cadastro + pagamento):
  - → Ganha **1 aula grátis**

**Limite:**
- Até completar as aulas recomendadas originalmente

**Exemplo:**
- Pacote de 20 aulas
- Indica 10 amigos que pagam
- Ganha 10 aulas extras = 30 aulas no total

**Benefício máximo:**
- Pode ganhar até 10 aulas
- Pode zerar o custo total do curso
- Ou ganhar aulas pós-CNH

---

### 2) Ganha aulas pós-CNH

**Após cumprir seu pacote, pode ganhar:**
- [ ] Aula de direção defensiva
- [ ] Aula de baliza avançada
- [ ] Aula de estrada

**Condição:**
- Sempre com o mesmo instrutor vinculado

---

### 3) Ganha seguro grátis (Se Aplicável)

**Condição:**
- Se estiver usando carro algalugado do instrutor

**Benefício:**
- A cada 10 indicações → **1 mês de seguro grátis**

**Nota:**
- Não interfere no pacote do instrutor

---

### 4) Bônus por Engajamento

**Redes Sociais:**
- [ ] Se postar o app nas redes sociais
- [ ] → Pode ganhar **1 aula bônus**

**Influenciador (>10 mil seguidores):**
- [ ] Se tiver mais de 10 mil seguidores
- [ ] → Pode ganhar o **pacote inteiro grátis** (promo inicial do app)

---

## 3.2 - BONIFICAÇÃO DO INSTRUTOR

### 1) Seguro Grátis

**Para instrutores com performance alta:**
- [ ] 1 mês de seguro grátis
- [ ] 2 meses
- [ ] 3 meses

**Depende de:**
- Metas de performance
- Nota média
- Taxa de ocupação

---

### 2) Manutenção Grátis

**Possível parceria com seguradora/oficina:**
- [ ] Troca de óleo
- [ ] Revisão básica
- [ ] Alinhamento/balanceamento
- [ ] Pastilhas de freio

---

### 3) Mais Candidatos

**Instrutores top:**
- [ ] Sobem no ranking invisível
- [ ] Recebem mais alunos
- [ ] Aparecem mais no match
- [ ] Têm prioridade no algoritmo

---

### 4) Menor Taxa do App

**Bons instrutores podem ganhar:**
- [ ] 1% a menos de taxa
- [ ] 2% a menos de taxa

**Critérios:**
- Nota acima de 4.5
- Pontualidade >95%
- Cancelamentos <5%

---

### 5) Selos de Qualidade

**O app pode dar selos como:**

| Selo | Critério |
|------|----------|
| Instrutor Ouro | Nota >4.7 |
| Instrutor Diamante | Nota >4.9 + 100+ alunos |
| Especialista em Ansiedade | 50+ alunos ansiosos |
| Bolsista do Mês | Melhor performance |

---

## Tabela Resumo

### Candidato

| Bonificação | Requisito | Benefício |
|-------------|-----------|-----------|
| Aula grátis | 10 indicações | 1 aula |
| Aula pós-CNH | Finalizar pacote | Direção defensiva, baliza, estrada |
| Seguro grátis | 10 indicações + carro algalugado | 1 mês |
| Aula bônus | Postar nas redes | 1 aula |
| Pacote grátis | >10k seguidores | 100% do pacote |

---

### Instrutor

| Bonificação | Requisito | Benefício |
|-------------|-----------|-----------|
| Seguro grátis | Performance alta | 1-3 meses |
| Manutenção grátis | Parceria ativa | Óleo, revisão, etc |
| Mais candidatos | Top rating | +fluxo no match |
| Taxa reduzida | Nota >4.5, pontualidade >95% | -1% a -2% |
| Selos | Critérios específicos | Ouro, Diamante, Especialista |

---

## Regras Gerais

### Transparência
- [ ] Todas as regras visíveis no app
- [ ] Cálculos automáticos e auditáveis

### Limites
- [ ] Bonificações têm limites claros
- [ ] Não acumulam infinitamente

### Cancelamento
- [ ] Indicações canceladas não contam
- [ ] Fraude resulta em banimento

---

## Fluxo Técnico

```
Indicação realizada
       │
       ▼
C amigo se cadastra + paga
       │
       ▼
Sistema valida (CPF, pagamento)
       │
       ▼
Registra indicação
       │
       ▼
Contador++
       │
       ▼
Se % 10 == 0
       │
       ▼
Libera bonificação
       │
       ▼
Notifica usuário
```

---

## Resumo para TI

> "O app faz o match automático entre candidato e instrutor baseado no perfil psicológico e operacional do aluno, nas características e desempenho do instrutor, e nas regras internas de segurança, carro e pedal. O aluno paga antes, o instrutor recebe por aula feita, e ambos têm um sistema de bonificações baseado em indicações, performance e comportamento dentro do app"

---

**Última atualização:** 30/03/2026
**Projeto:** CNH+