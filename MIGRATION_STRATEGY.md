# Estratégia de Migração Candidato Screens

## Padrão de Migração

Cada screen será modificada assim:

1. **Remover**: `import com.cnhplus.data.EmulatedData`
2. **Adicionar**: `import com.cnhplus.ui.theme.LocalAppState`
3. **Inside Composable**:
   ```kotlin
   val app = LocalAppState.current
   var candidato by remember { mutableStateOf<CandidatoDto?>(null) }
   var isLoading by remember { mutableStateOf(true) }
   
   LaunchedEffect(Unit) {
       app.candidatoRepo.getCandidato(app.currentUser.value?.id ?: "").fold(
           onSuccess = {
               candidato = it
               isLoading = false
           },
           onFailure = {
               isLoading = false
           }
       )
   }
   ```
4. **Replace all**: `EmulatedData.candidatos[0]` → `candidato`
5. **Replace all**: `EmulatedData.instrutores[0]` → fetch via instrutorRepo

---

## Ordem de Migração (Wave 2-3)

### Wave 2 — Candidato (1.5h)
1. ✅ **CandidatoHomeScreen** (POC — mostra padrão)
2. **CandidatoAulasScreen** (usa aulaRepo)
3. **CandidatoMatchScreen** (usa instrutorRepo + algoritmo)
4. **CandidatoPagamentoScreen** (usa pagamentoRepo)
5. **CandidatoPerfilScreen** (usa candidatoRepo + profileRepo)

### Wave 3 — Instrutor (1.5h)
1. **InstrutorHomeScreen** (dashboard com stats)
2. **InstrutorAgendaScreen** (usa aulaRepo)
3. **InstrutorAulasScreen** (usa avaliacaoRepo)
4. **InstrutorPerfilScreen** (usa instrutorRepo + profileRepo)
5. **InstrutorFinanceiroScreen** (NEW — usa repasseRepo, Ganhos)

---

## Verificações Pós-Migração

- [ ] Nenhuma referência a `EmulatedData`
- [ ] `LocalAppState.current` presente
- [ ] Todos os repos importados corretamente
- [ ] LaunchedEffect para fetch de dados
- [ ] Try-catch ou error handling

---

## Antes de Commit

- [ ] Rodar `./gradlew build` completo
- [ ] Sem erros de compilação
- [ ] Verificar se todas as 16 screens foram atualizadas
- [ ] Commit atômico com mensagem descritiva

