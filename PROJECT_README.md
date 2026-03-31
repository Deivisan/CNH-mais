# 🚗 CNH+

> Marketplace Inteligente de Aulas Práticas de Direção - Conectando candidatos à CNH com instrutores qualificados.

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Kotlin-blue)
![Status](https://img.shields.io/badge/Status-Pre--Alpha-orange)
![Version](https://img.shields.io/badge/Version-0.01--Pre--Alpha-blue)

## 📱 Sobre o Projeto

**CNH+** é uma plataforma mobile que conecta candidatos à CNH (Carteira Nacional de Habilitação) com instrutores de direção de forma segura, transparente e inteligente.

### 🎯 Missão
**Conectar** candidatos à CNH com instrutores qualificados através de um **match inteligente** baseado em perfil comportamental e operacional.

### 🚀 Diferenciais
- 🤖 Algoritmo de Match Inteligente
- 🔒 Segurança Total (pagamento retido, documentos validados)
- ⭐ Transparência (avaliações reais)
- 🎁 Sistema de Bonificações

---

## ⬇️ Download APK

**Versão atual:** [v0.01 Pre-Alpha](https://github.com/Deivisan/CNH-mais/releases/tag/v0.01-pre-alpha)

```
📦 CNH-mais-debug.apk (16.8 MB)
📅 Data: 30/03/2026
🔧 Build: Kotlin + Jetpack Compose
```

> ⏸️ **Status da landing principal (APK):** aguardando aprovação de visual e designer.
> Para demonstração de fluxo completo, use a **Demo PWA emulada** em `./demo-pwa/`.

---

## 🎨 Identidade Visual

### Paleta de Cores
| Cor | Hex |
|-----|-----|
| Primary | #1E3A5F |
| Primary Light | #2E5A8F |
| Secondary | #4A90D9 |
| Accent | #87CEEB |

### Degradê
- **Topo:** Azul Escuro (#1E3A5F)
- **Base:** Celeste (#87CEEB)

---

## 📊 Telas do App (16 total)

### 👑 Admin (6 telas)
| Tela | Descrição |
|------|-----------|
| Home | Dashboard com estatísticas |
| Instrutores | Lista de instrutores cadastrados |
| Alunos | Gerenciamento de candidatos |
| Aulas | Visualizar todas as aulas |
| Financeiro | Receitas e estatísticas |
| Configurações | Configurações do sistema |

### 🎓 Candidato (5 telas)
| Tela | Descrição |
|------|-----------|
| Home | Progresso + instrutor assigned |
| Minhas Aulas | Aulas agendadas e históricas |
| Encontrar Instrutor | Buscar novo instrutor |
| Comprar Aulas | Adquirir pacote |
| Meu Perfil | Informações pessoais |

### 🚗 Instrutor (5 telas)
| Tela | Descrição |
|------|-----------|
| Home | Resumo hoje + próxima aula |
| Agenda | Gerenciar disponibilidade |
| Aulas | Ver aulas agendadas |
| Financeiro | Ver ganhos e estatísticas |
| Meu Perfil | Editar informações |

---

## 🛠️ Tech Stack

| Tecnologia | Uso |
|------------|-----|
| Kotlin | Linguagem |
| Jetpack Compose | UI Framework |
| Material 3 | Design System |
| Navigation Compose | Navegação |
| Gradle 8.10 | Build System |
| Android SDK 34 | Target SDK |

---

## 📋 Documentação

| Documento | Descrição |
|-----------|-----------|
| [SPEC.md](./docs/SPEC.md) | Visão geral do projeto |
| [PERFIL-INSTRUTOR.md](./docs/PERFIL-INSTRUTOR.md) | Perfil do instrutor (10 seções) |
| [PAINEL-INSTRUTOR.md](./docs/PAINEL-INSTRUTOR.md) | Área do instrutor (10 abas) |
| [AGENDA-INSTRUTOR.md](./docs/AGENDA-INSTRUTOR.md) | Sistema de agenda |
| [BACKOFFICE.md](./docs/BACKOFFICE.md) | Painel administrativo (12 blocos) |
| [VISAO-CANDIDATO.md](./docs/VISAO-CANDIDATO.md) | Fluxo do candidato |
| [SISTEMA-MATCH.md](./docs/SISTEMA-MATCH.md) | Algoritmo de matching |
| [BONIFICACOES.md](./docs/BONIFICACOES.md) | Sistema de recompensas |
| [AGENTS.md](./AGENTS.md) | Contexto para agentes IA |
| [demo-pwa/README.md](./demo-pwa/README.md) | Guia da Demo PWA emulada |
| [ROADMAP-DEMO-PWA-20DIAS.md](./docs/ROADMAP-DEMO-PWA-20DIAS.md) | Roadmap de execução (20 dias) |
| [CHECKLIST-VALIDACAO-DEMO-PWA.md](./docs/CHECKLIST-VALIDACAO-DEMO-PWA.md) | Checklist de validação real |
| [ESPEC-COBERTURA-DEMO-PWA.md](./docs/ESPEC-COBERTURA-DEMO-PWA.md) | Matriz: especificação x cobertura |

---

## 📂 Estrutura

```
CNH-mais/
├── app/                        # Android (Kotlin + Compose)
│   └── src/main/
│       └── java/com/cnhplus/
│           ├── MainActivity.kt
│           ├── navigation/     # NavHost + Screen
│           ├── screens/       # 16 telas
│           ├── data/          # Models + EmulatedData
│           └── ui/theme/      # Theme CNH+
├── docs/                       # 8 documentos
├── CNH-mais-debug.apk         # APK release
├── AGENTS.md                   # Contexto IA
└── PROJECT_README.md
```

---

## 🔄 Changelog

### v0.01 Pre-Alpha (30/03/2026)
- ✅ APK debug compilado (16.8MB)
- ✅ 16 telas implementadas
- ✅ Navigation completa
- ✅ Theme CNH+ aplicado
- ✅ Dados emulados

---

## 📌 Próximos Passos

- [ ] Rebuild APK (fix build issues)
- [ ] ViewModels com estado real
- [ ] Backend (Bun + Supabase)
- [ ] Autenticação
- [ ] FCM Notificações
- [ ] Mercado Pago integração

---

## 👤 Autor

**Deivison Santana** (@deivisan) | **Versão:** 0.01 Pre-Alpha
