# Napkin Runbook — CNH+ Android App v0.0.5

## Curation Rules
- Re-prioritize on every read (highest-value first)
- Keep only recurring, reusable guidance + critical gotchas
- Max 10 items per category
- Each item includes date + actionable "Do instead"

---

## 🔴 CRITICAL NEVER-AGAIN RULES

1. **[2026-04-01] Public inline functions CANNOT access private members**
   Do instead: Use `@PublishedApi internal` on any field accessed by public inline methods. Pattern: `@PublishedApi internal val baseUrl: String`. Do NOT make the whole class `internal` — it breaks public API exposure across constructors. Do NOT use private inline helpers — they still fail.

2. **[2026-04-01] `inline` + reified generics require all code in-method if accessing class fields**
   Do instead: Inline ALL HTTP logic directly in the method body. No delegation to private helpers. If you need sharing, use non-inline private functions that DON'T use reified types.

3. **[2026-04-01] Compose brace matching: Row/Card MUST have `) {` not just `)`**
   Do instead: Always write `Row(...) { /* content */ }` — never `Row(...)` then content on next line without the `{`. The compiler will cascade errors.

4. **[2026-04-01] NO `LocalAppSession` — it does not exist**
   Do instead: Always use `val app = LocalAppState.current`. There is ONE CompositionLocal: `LocalAppState`. Check `app.sessionState`, `app.currentRole`, `app.selectRole()`.

5. **[2026-04-01] No duplicate `val` declarations in same scope**
   Do instead: Check for duplicate variable names before adding new ones. `val app = LocalAppState.current` must appear ONCE per composable.

---

## 🎯 Execution & Validation (GSD Workflow)

1. **[2026-04-01] GSD: Waves sequential, each MUST compile before next starts**
   Do instead: Execute in waves: Wave 1 = infra/models, Wave 2 = repos/network, Wave 3 = screens/UI, Wave 4 = verify/ship. After EACH wave: `./gradlew compileDebugKotlin`. Zero errors → proceed. Errors → fix in same wave.

2. **[2026-04-01] NO build APK until compile passes clean**
   Do instead: `./gradlew compileDebugKotlin` first (fast, ~1min). Only when BUILD SUCCESSFUL → `./gradlew assembleDebug`. Building APK with compile errors wastes 2+ minutes.

3. **[2026-04-01] `./gradlew compileDebugKotlin` is the single source of truth**
   Do instead: Run this after every batch of edits. It catches 99% of issues before APK build. Do NOT trust IDE highlighting — run the actual compiler.

4. **[2026-04-01] GSD atomic commits per fix batch**
   Do instead: Group related fixes (e.g., "Fix SupabaseClient inline visibility") → git add → commit with descriptive message → compile → proceed. Each commit should be independently testable.

5. **[2026-04-01] LocalAppState.current mandatory in ALL composables**
   Do instead: Every screen must access `val app = LocalAppState.current` from CompositionLocal. Never use ViewModels, never access AppState via constructor. Pattern: `val app = LocalAppState.current; LaunchedEffect(Unit) { ... }`

6. **[2026-04-01] LaunchedEffect for ALL data fetch, no init blocks**
   Do instead: Move all network calls from `init {}` to `LaunchedEffect(Unit) { ... }`. This ensures recomposition safety and proper state management.

7. **[2026-04-01] Match algorithm follows SPEC.md exactly**
   Do instead: Score = (especialidade_match * 30) + (nota_media * 20) + (pontualidade * 20) + (1 - cancelamentos_normalizado * 10) + (horas_experiencia_normalizado * 10) + (distancia_normalizado * 10). Max ~100pts.

8. **[2026-04-01] Color scheme: Azul #1E3A5F → Celeste #87CEEB degradê**
   Do instead: Primary (#1E3A5F) for headers/bold. Secondary (#4A90D9) for buttons/accents. Accent (#87CEEB) for lighter elements. Never use hardcoded hex—use Theme.kt exports.

9. **[2026-04-01] SUPABASE_ANON_KEY in MainActivity is PLACEHOLDER**
   Do instead: Before ANY runtime test, replace `s0_placeholder_XXXXXXXX...` with real value from Supabase dashboard → project → Settings → API keys. This is BLOCKING runtime auth.

10. **[2026-04-01] Admin screens deprioritized — keep as stubs**
    Do instead: 5 admin files kept as functional stubs with minimal UI. Not blocking MVP. Replace when Wave 8 starts.

---

## 🏗️ Architecture & Code Patterns

1. **[2026-04-01] SupabaseClient: inline reified with @PublishedApi internal fields**
   Do instead: `class SupabaseClient(@PublishedApi internal val baseUrl, @PublishedApi internal val anonKey)`. All inline methods (get, getById, insert, etc.) use these fields directly. HTTP logic inlined — no private helpers for inline methods.

2. **[2026-04-01] SupabaseClient raw OkHttp (no Retrofit)**
   Do instead: All HTTP via inline methods: `client.get<T>(table)`, `client.getById<T>(table, id)`, `client.insert(table, item)`, `client.update(table, column, value, fields)`. No Retrofit.

3. **[2026-04-01] Repositories return Result<T>**
   Do instead: All repo methods return `Result<T>`. Error handling is caller's responsibility. Never throw exceptions in repos. Pattern: `when (val result = app.repo.fetch()) { ... }`.

4. **[2026-04-01] All repositories use the SAME SupabaseClient instance**
   Do instead: AppState creates ONE SupabaseClient and passes it to all repos. Don't create multiple clients.

5. **[2026-04-01] Models.kt helper functions: getPerfil(), getPacote(), getVeiculo()**
   Do instead: Use `candidato.getPerfil()` to extract nested objects, `candidato.withPerfil()` to update. Avoids null-safety boilerplate.

---

## 📱 Screen Patterns

1. **[2026-04-01] Standard screen: Scaffold + LaunchedEffect + mutable states**
   Do instead: `val app = LocalAppState.current` → `var loading by remember { mutableStateOf(false) }` → `LaunchedEffect(Unit) { ... }` → `Scaffold(...) { ... }`.

2. **[2026-04-01] NavHost: 2 separate blocks per role (Candidato ≠ Instrutor)**
   Do instead: 2 separate NavHost blocks — Candidato (5 tabs), Instrutor (5 tabs). Each has bottom navigation. Role switch → route to `Screen.SelectRole`.

3. **[2026-04-01] CompositionLocalProvider wraps NavHost in MainActivity**
   Do instead: `CompositionLocalProvider(LocalAppState provides app) { NavHost(...) }`. This ensures all child composables access same AppState.

4. **[2026-04-01] SurfaceColor alias needed (Material3 clash with Surface)**
   Do instead: Import `SurfaceColor` from `ui/theme/Colors.kt` instead of `Surface` color which conflicts with Material3 Surface composable.

---

## 🚨 Known Gotchas

1. Video recording requires Camera + storage permissions → Add to AndroidManifest.xml + runtime check.
2. Zero EmulatedData in production screens → Admin screens exempt (intentional, deprioritized).
3. `kotlinx.serialization.encodeToString` needs `Json.encodeToString(obj)` when called outside inline context.

---

**Last updated:** 2026-04-01 — Release v0.0.5, ALL compilation errors fixed
**Author:** DevSan (@deivisan)
**Version:** 0.0.5
