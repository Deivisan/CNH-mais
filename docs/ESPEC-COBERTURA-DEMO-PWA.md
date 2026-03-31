# especificação x cobertura da demo pwa

## escopo desta matriz

mapear o que já está representado na demo pwa em relação à especificação funcional e operacional do produto.

## visão do candidato

| item | status | observação |
|------|--------|------------|
| login google (emulado) | ✅ | painel login com ação emulada |
| cadastro rápido (cpf/celular/cidade/renach) | ✅ | onboarding + cadastro candidato |
| perfil comportamental | ✅ | painel onboarding completo |
| recomendação de aulas | ✅ | botão e ação emulada |
| pagamento retido | ✅ | painel pagamentos com status retido |
| match automático | ✅ | painel match com ranking e seleção |
| primeira aula/home | ✅ | painel home + aulas |
| chat e mapas | ✅ | painéis dedicados |
| assistente ia opcional no chat | ✅ | configuração local + fallback de rota |

## perfil público do instrutor (visão candidato)

| seção | status | observação |
|-------|--------|------------|
| identidade/confiança | ✅ | foto/nome/status/avaliação/ações |
| indicadores objetivos | ✅ | horas/alunos/pontualidade/cancelamentos |
| biografia | ✅ | bloco dedicado |
| estilo/especialidades | ✅ | tags representadas |
| logística | ✅ | veículo/regiões/turnos |
| avaliações | ✅ | nota, comentários, visão consolidada |
| regras/políticas | ✅ | bloco padronizado |
| currículo histórico | ✅ | autoescolas como histórico |

## painel do instrutor (visão dele)

| aba | status | observação |
|-----|--------|------------|
| visão geral | ✅ | dashboard inicial |
| meu perfil | ✅ | edição de campos permitidos |
| mídia | ✅ | limite e intenção representados |
| desempenho | ✅ | métricas não editáveis |
| financeiro | ✅ | saldo disponível/pendente + histórico |
| dados bancários | ✅ | com aviso de validação |
| aulas & disputas | ✅ | fluxo de disputa emulado |
| carro/aluguel | ✅ | termo e configuração |
| educação/comunidade | ✅ | área entre instrutores |
| suporte | ✅ | acesso rápido |

## agenda do instrutor

| requisito | status | observação |
|-----------|--------|------------|
| visualização de agenda | ✅ | tabela funcional atual |
| abrir/bloquear horários | ✅ | ações emuladas |
| seleção de veículo por horário | 🟡 | representado em nível geral; falta granularidade por slot |
| termo obrigatório carro próprio | ✅ | painel carro/aluguel |
| projeção diária/semanal/mensal | 🟡 | representação parcial; ainda sem cálculo dinâmico detalhado |

## backoffice admin - tela 4 gerenciamento instrutor

| bloco | status |
|-------|--------|
| bloco 1 identificação | ✅ |
| bloco 2 status operacional | ✅ |
| bloco 3 documentos | ✅ |
| bloco 4 perfil editável (visão admin) | ✅ |
| bloco 5 agenda | ✅ |
| bloco 6 aulas | ✅ |
| bloco 7 financeiro interno | ✅ |
| bloco 8 avaliações & conduta | ✅ |
| bloco 9 disputas | ✅ |
| bloco 10 comunicação & suporte | ✅ |
| bloco 11 log de ações | ✅ |
| bloco 12 ações administrativas | ✅ |

## classificação de maturidade atual

- cobertura funcional visual: **alta**
- fidelidade operacional: **média-alta**
- pendências para fechamento de ciclo: agenda avançada por slot + polimento de estados de erro e apresentação
