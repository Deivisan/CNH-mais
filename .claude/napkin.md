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

4. **[2026-04-01] `LocalAppState.current` é ÚNICA fonte de verdade**
   Zero `LocalAppSession`. Zero ViewModels. `val app = LocalAppState.current` em TODOS composables.

5. **[2026-04-01] SHA-1 debug keystore: `77:91:24:95:44:76:40:F7:98:13:E0:3A:25:23:52:15:7E:42:9B:5B`**
   Se Auth falhar com 401, verificar se este SHA-1 está registrado no Supabase.

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

## 🎯 GSD Execution Workflow

### Waves Sequenciais (CADA wave DEVE compilar antes de avançar)
1. **Wave 1**: infra/models/datastore
2. **Wave 2**: repos/network (SupabaseClient)
3. **Wave 3**: screens/UI/auth flow
4. **Wave 4**: verify/compile/APK → release

### Regras de Execução
1. **Fail fast**: `./gradlew compileDebugKotlin` ANTES de `assembleDebug`. Zero erros → procede.
2. **Atomic commits**: 1 batch de fixes = 1 commit descritivo.
3. **Congelado**: `demo-pwa/` e `index.html` — zero alterações até auth + nav estarem sólidos.
4. **Escopo cirúrgico**: Focar em auth + state management antes de telas/features.

---

## 🏗️ Architecture Patterns

1. **SupabaseClient**: inline reified + `@PublishedApi internal` fields. HTTP inline em cada método.
2. **AppState**: SINGULAR instance → CompositionLocalProvider → todos composables.
3. **NavHost**: 2 blocos (Candidato 5 tabs ≠ Instrutor 5 tabs). Role switch → SelectRole.
4. **Auth flow**: SignUp → delay(1s) → getProfile() → se null, fallback create → saveSession → Authenticated.
5. **Repos**: return `Result<T>` — caller handles errors, never throw.

---

## 🚨 Known Gotchas & Fix History

1. **Gradle cache servia APK com placeholder** → "401 Invalid API key" → rebuild com `--no-build-cache`
2. **Email confirmation ativada** → signup sem token → "registro falhou" → desativar no dashboard
3. **Profile create duplicado** → trigger + app criavam mesmo profile → remover create após signup
4. **Supabase API key NO build.gradle.kt debug** → campo `buildConfigField` com placeholder → ignorado, MainActivity.kt sobrescreve

---

## 📋 Próximos Passos (Prioridade)

- [x] **AppState.kt register() fix** — retry 3x com backoff, fallback create profile
- [x] **WelcomeScreen.kt** — 3 slides com Compose Foundation Pager nativo
- [x] **RegisterSuccessScreen** — animação + feedback visual de sucesso
- [x] **Navigation** — Welcome → Register → Success → SelectRole → PerfilCompleto
- [x] **PerfilCompletoScreen** — upload foto (camera + galeria) + form (nome, CPF, celular, cidade)
- [x] **Permissões** — CAMERA + READ_MEDIA_IMAGES + FileProvider no AndroidManifest
- [x] **SupabaseClient.uploadFile()** — upload + delete + getPublicUrl para Storage
- [x] **ImagePicker.kt** — componente reutilizável (camera + galeria, compressão JPEG)
- [x] **PermissionsHandler.kt** — hooks declarativos para runtime permissions
- [x] **RLS policies** — profiles, candidatos, instrutores, avatares, storage
- [x] **handle_new_user search_path** — fixado com SET search_path = public, pg_temp
- [x] **Deprecated fixes** — HorizontalDivider, Icons.Filled.Close
- [ ] **OnboardingCandidatoScreen** — perfil comportamental real após PerfilCompleto
- [ ] **InstrutorPerfilScreen** — upload de documentos (CRLV, CNH)
- [ ] **InstrutorProfileComplete** — onboarding para instrutores
- [ ] **Real API testing** — testar fluxo completo no dispositivo físico

---

**Last updated:** 2026-04-01 — v0.07 com PerfilCompleto + Storage + RLS
**Author:** DevSan (@deivisan)
**SHA1 debug:** `77:91:24:95:44:76:40:F7:98:13:E0:3A:25:23:52:15:7E:42:9B:5B`
**Debug tip:** `strings classes.dex | grep -i "ibyngfq"` confirma key no APK
