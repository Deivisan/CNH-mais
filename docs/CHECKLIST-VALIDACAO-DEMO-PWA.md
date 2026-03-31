# checklist de validação - demo pwa

> status separado por **implementado em código** e **teste manual executado**.

## 1) navegação

| item | implementado | teste manual |
|------|--------------|--------------|
| menu lateral troca painéis | ✅ | ⏳ pendente |
| atalhos rápidos funcionam | ✅ | ⏳ pendente |
| botões de ação exibem feedback visual | ✅ | ⏳ pendente |

## 2) autenticação e cadastros (emulação)

| item | implementado | teste manual |
|------|--------------|--------------|
| login com seleção de perfil redireciona | ✅ | ⏳ pendente |
| cadastro candidato com envio emulado | ✅ | ⏳ pendente |
| cadastro instrutor com envio emulado | ✅ | ⏳ pendente |
| cadastro admin com envio emulado | ✅ | ⏳ pendente |

## 3) fluxo candidato

| item | implementado | teste manual |
|------|--------------|--------------|
| onboarding completo visível | ✅ | ⏳ pendente |
| match com seleção de instrutor | ✅ | ⏳ pendente |
| pagamentos e status emulados | ✅ | ⏳ pendente |
| chat com resposta automática | ✅ | ⏳ pendente |
| chat com assistente ia opcional (nvidia nim / minimax) | ✅ | ⏳ pendente |
| mapas com troca de ponto | ✅ | ⏳ pendente |

## 4) fluxo instrutor

| item | implementado | teste manual |
|------|--------------|--------------|
| dashboard | ✅ | ⏳ pendente |
| perfil público (visão candidato) | ✅ | ⏳ pendente |
| agenda | ✅ | ⏳ pendente |
| aulas e financeiro | ✅ | ⏳ pendente |
| dados bancários | ✅ | ⏳ pendente |
| disputas | ✅ | ⏳ pendente |
| carro/aluguel | ✅ | ⏳ pendente |
| comunidade | ✅ | ⏳ pendente |
| suporte | ✅ | ⏳ pendente |

## 5) fluxo admin

| item | implementado | teste manual |
|------|--------------|--------------|
| dashboard | ✅ | ⏳ pendente |
| gestão instrutores/alunos/aulas/financeiro/config | ✅ | ⏳ pendente |
| tela 4 gerenciamento instrutor (12 blocos) | ✅ | ⏳ pendente |
| suporte/chat | ✅ | ⏳ pendente |

## 6) pwa/github pages

| item | implementado | validado |
|------|--------------|----------|
| manifest configurado | ✅ | ✅ |
| service worker registrado | ✅ | ⏳ pendente |
| rota `/demo-pwa/` responde http 200 local | ✅ | ✅ |
| rota `/` (landing) responde http 200 local | ✅ | ✅ |

## pendências conhecidas

- [ ] agenda avançada com configuração por slot (veículo + projeção dinâmica detalhada)
- [ ] refinamento de estados de erro (cenários negativos detalhados)
- [ ] etapa de polimento final de ui
