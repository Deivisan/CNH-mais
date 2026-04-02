# Napkin Runbook — CNH+ Android App 🔴 CRITICAL

## Curation Rules
- Re-prioritize on every read (highest-value first)
- Keep only recurring, reusable guidance + critical gotchas
- Max 10 items per category
- Each item includes date + actionable "Do instead"

---

## 🔴 NUNCA MAIS ERRAR ISSO (PRIORIDADE MÁXIMA)

1. **[2026-04-01] Gradle Build Cache = INIMIGO #1 — APK placeholder persiste!**
   `clean` NAO limpa .dex caches. O placeholder PERMANECE no APK mesmo após source fix.
   **Do instead:** `rm -rf app/build && ./gradlew --no-build-cache --rerun-tasks assembleDebug`
   **Verificar APK:** `unzip -p app-debug.apk classes.dex | strings | grep -i "ibyngfq\|sb_publishable"` — SE encontrar "REPLACE" ou "placeholder", rebuild forçado.

2. **[2026-04-01] Public inline functions CANNOT access private members**
   **Do instead:** `@PublishedApi internal val baseUrl: String`. NUNCA delegar inline para private helpers. Inline TODO o código HTTP dentro da função.

3. **[2026-04-01] Compose brace matching: Row/Card precisam de `) {`**
   `Row(...) { content }` — NUNCA `Row(...)` seguido de conteúdo sem `{`. Erro cascata.

4. **[2026-04-01] SHA-1 debug keystore: `77:91:24:95:44:76:40:F7:98:13:E0:3A:25:23:52:15:7E:42:9B:5B`**
   Se Auth falhar com 401, verificar se este SHA-1 está registrado no Supabase.

5. **[2026-04-02] Kotlin 2.1.20 + Hilt — ordem de plugins MANDATÓRIA**
   `kotlin-android` deve vir ANTES de `com.google.dagger.hilt.android` no plugins block.
   **Do instead:**
   ```
   plugins {
       id("com.android.application")
       id("org.jetbrains.kotlin.android")
       id("com.google.dagger.hilt.android")  // DEPOIS do kotlin
   }
   ```

6. **[2026-04-02] Screens > 250 linhas = ACÚMULO DE DÍVIDA TÉCNICA**
   `PerfilInstrutorScreen.kt` (533 linhas) viola SRP. Cada screen deve ter 1 responsabilidade.
   **Do instead:** Split em wizard steps ou extrair componentes. Máximo 250 linhas por arquivo Composable.

7. **[2026-04-02] Json instances = Singleton obrigatório**
   Criar `Json { }` múltiplas vezes gera warnings e overhead.
   **Do instead:** `JsonConfig.default` singleton em `core/di/JsonConfig.kt`.

---

## 🔐 SUPABASE CONFIG (REAL)

| Item | Valor |
|------|-------|
| Projeto | `ibyngfqddoefatqtojfj` (DeiviTech, sa-east-1) |
| Anon Key | `sb_publishable_SvWV-VYW4WbqLVoPL_VTTg_SZGF3e2P` |
| URL | `https://ibyngfqddoefatqtojfj.supabase.co` |
| Email confirm | **DESATIVADA** para dev |
| Trigger | `handle_new_user` — auto-cria profiles (verificar search_path) |

---

## 📱 Test Accounts

| Email | Senha | Role | Status |
|-------|-------|------|--------|
| `deivilsantana@outlook.com` | `33484@Cnh.` | candidato | ✅ Criado, email verified, profile OK |

---

## 🎯 PLANO DE EVOLUÇÃO 2026 (Fases 1-5)

**Plano completo:** `.kilo/PLANO-EVOLUCAO-CNH-v1.0.md`

### Fase 1: Foundation (Week 1-2) — EM ANDAMENTO
- [ ] **Kotlin 2.1.20** — upgrade da toolchain
- [ ] **Gradle 8.12** — performance build
- [ ] **Hilt DI** — setup inicial com `@HiltAndroidApp`
- [ ] **JsonConfig** — singleton para eliminar warnings
- [ ] **Estrutura de pacotes** — core/data/domain/presentation

### Fase 2: Architecture (Week 3-4)
- [ ] **BaseViewModel** — pattern unidirecional de estado
- [ ] **Use Cases** — extração da lógica de negócio
- [ ] **Repository Interfaces** — contratos claros
- [ ] **Migração `LocalAppState`** → HiltViewModels
- [ ] **SessionManager** — substituir DataStore direto

### Fase 3: UI/UX Premium (Week 5-6)
- [ ] **Design System** — elevações, gradientes, animações
- [ ] **Shimmer Loading** — substituir spinners
- [ ] **Transições animadas** — navigation-compose animations
- [ ] **Screens refatoradas** — < 250 linhas cada
- [ ] **Componentes removidos** — recriar GeolocalizacaoPicker, DenunciaDialog

### Fase 4: Features (Week 7-8)
- [ ] **Wizard Instrutor** — 5 steps separados
- [ ] **Agenda interativa** — calendário visual
- [ ] **Match screen** — card stack com swipe
- [ ] **Onboarding animado** — shared element transitions

### Fase 5: Quality (Week 9-10)
- [ ] **Testes unitários** > 60% cobertura
- [ ] **CI/CD** — GitHub Actions
- [ ] **Performance** — profiling e otimização
- [ ] **APK otimizado** < 25MB

---

## 🏗️ Architecture Patterns (NEW — Fase 2+)

1. **Clean Architecture**: core → data → domain → presentation
2. **MVVM**: ViewModel injetado por Hilt, UI state unidirecional
3. **Use Cases**: cada operação de negócio = 1 use case
4. **Repository Pattern**: interfaces + implementações separadas
5. **Dependency Injection**: Hilt para tudo (ViewModels, Repos, UseCases)

---

## 🚨 Known Gotchas & Fix History

1. **Gradle cache servia APK com placeholder** → "401 Invalid API key" → rebuild com `--no-build-cache`
2. **Email confirmation ativada** → signup sem token → "registro falhou" → desativar no dashboard
3. **Profile create duplicado** → trigger + app criavam mesmo profile → remover create após signup
4. **Supabase API key NO build.gradle.kt debug** → campo `buildConfigField` com placeholder → ignorado, MainActivity.kt sobrescreve
5. **v0.05 e v0.07a = MESMO APK compilado 2x** → sem verificação de hash, não detectou duplicação
6. **PerfilInstrutorScreen.kt 533 linhas** → violação SRP, precisa split em wizard
7. **4 warnings Json redundantes** → Models.kt e repositories criam instâncias múltiplas

---

## 🔒 Regras de Build — NUNCA mais duplicar APK (CRÍTICO)

**Antes de cada BUILD novo:**

```bash
# 1. Hash do APK existente
LAST_APK=$(ls -t releases/cnhmais-*.apk 2>/dev/null | head -1)
[ -n "$LAST_APK" ] && echo "SHA256 atual: $(sha256sum "$LAST_APK" | awk '{print $1}')"

# 2. Build limpo
./gradlew compileDebugKotlin && ./gradlew --no-build-cache --rerun-tasks assembleDebug

# 3. Hash do novo
NEW_APK="app/build/outputs/apk/debug/app-debug.apk"
[ -f "$NEW_APK" ] && echo "SHA256 novo: $(sha256sum "$NEW_APK" | awk '{print $1}')"
```

**Checklist release:**
- [ ] `git diff --stat origin/master` → mudanças reais em `.kt`?
- [ ] `unzip -p app-debug.apk classes.dex | strings | grep -c "ibyngfq"` > 0?
- [ ] SHA256 novo ≠ SHA256 anterior
- [ ] Asset release: `cnhmais.apk` (sem versão)

---

## 🛠️ Comandos Rápidos

```bash
# Build limpo completo
rm -rf app/build && ./gradlew clean compileDebugKotlin --rerun-tasks

# Build APK debug
./gradlew --no-build-cache --rerun-tasks assembleDebug

# Verificar chave no APK
unzip -p app/build/outputs/apk/debug/app-debug.apk classes.dex | strings | grep -i "ibyngfq"

# SHA256 do APK
sha256sum app/build/outputs/apk/debug/app-debug.apk
```

---

**Last updated:** 2026-04-02 — Plano Evolução v1.0 iniciado, Fase 1 em andamento
**Author:** DevSan (@deivisan)
**SHA1 debug:** `77:91:24:95:44:76:40:F7:98:13:E0:3A:25:23:52:15:7E:42:9B:5B`
**Plano completo:** `.kilo/PLANO-EVOLUCAO-CNH-v1.0.md`
