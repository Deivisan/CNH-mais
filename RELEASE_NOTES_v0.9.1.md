🐛 **CORREÇÃO CRÍTICA v0.9.1**

## Problema Identificado e Corrigido

O APK v0.9 estava com um **BUG CRÍTICO** no fluxo de registro:

### ❌ Problema
No arquivo AppState.kt linha 125, o role estava HARDCODED como "candidato":

Isso fazia com que TODOS os novos usuários fossem criados como candidatos, ignorando a tela SelectRoleScreen onde o usuário escolhe entre ser Candidato ou Instrutor.

### ✅ Solução
- Alterado para criar perfil com role = "pendente" no registro inicial
- SelectRoleScreen agora funciona corretamente para ambos os perfis
- Adicionado tratamento de erro no callback de seleção de role

## 📦 Info Técnica
- Tamanho APK: 19MB
- SHA256: cf4736593acf391c54180776df51622b0278cb2e9ffe3b0e38c9ef48f06123ea
- Build: Clean build, cache totalmente limpo

## ⚠️ Estado
- ❌ Ainda NÃO TESTADO em dispositivo físico
- ⚠️ Versão PRE-ALPHA experimental
- 🔧 Fluxo de Instrutor agora deve aparecer corretamente

## 🎯 Como testar
1. Instale o APK
2. Faça registro com email novo
3. Na tela "Qual seu perfil?" escolha "Instrutor de Direção"
4. Deve ir para o Wizard de 5 steps do Instrutor

Download: cnhmais-v0.9.1-pre-alpha.apk
