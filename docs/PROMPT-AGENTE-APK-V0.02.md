# prompt mestre — agente apk v0.02 (design + interfaces + sensores)

## contexto

você está atuando no repositório `CNH-mais`, branch dedicada ao apk android (não mexer na demo pwa web nesta tarefa).

objetivo: entregar a **versão 0.02** do app android com foco em design consistente, cobertura de telas faltantes e preparação de permissões/sensores para evolução funcional.

## princípios obrigatórios

1. não quebrar o build atual.
2. preservar identidade visual cnh+ (`#1E3A5F`, `#2E5A8F`, `#4A90D9`, `#87CEEB`).
3. evitar textos genéricos; usar copy clara e funcional.
4. manter separação entre:
   - dados calculados pelo sistema (não editáveis)
   - dados editáveis pelos usuários
5. registrar claramente no código/documentação o que está:
   - implementado real
   - emulação temporária

## tarefas (ordem sugerida)

### etapa 1 — auditoria rápida de telas
- mapear telas existentes no app compose.
- mapear lacunas vs especificações em `docs/` (perfil instrutor, agenda, backoffice, candidato).
- gerar lista objetiva: faltando / parcial / pronto.

### etapa 2 — design system v0.02
- consolidar tokens (cores, tipografia, espaçamentos, estados).
- criar componentes reutilizáveis (cards, chips, status badges, topbars, ctas).
- revisar contraste e hierarquia visual.

### etapa 3 — cobertura de interfaces faltantes
- completar telas faltantes prioritárias para:
  - candidato
  - instrutor (10 abas conforme docs)
  - admin (incluindo visão operacional necessária)
- implementar estados de tela: vazio, carregando, erro, sucesso.

### etapa 4 — permissões e sensores (preparação)
- adicionar no `AndroidManifest` permissões necessárias para roadmap (sem exagero):
  - localização aproximada/precisa
  - internet/rede
  - notificações (android 13+)
  - câmera (se houver fluxo de selfie/documento)
- estruturar camada de abstração para sensores/serviços:
  - localização
  - notificações push
  - câmera/upload (stub)
- não implementar lógica insegura; apenas preparação sólida e rastreável.

### etapa 5 — qualidade e entrega
- garantir navegação sem rotas quebradas.
- rodar build e validar telas principais.
- atualizar docs de status v0.02 com changelog real.

## definição de pronto (v0.02)

- build apk compila sem erro.
- fluxo principal navegável para os 3 perfis no app.
- design consistente e sem “telas órfãs”.
- permissões/sensores preparados para próxima iteração.
- documentação atualizada com transparência técnica.

## saída esperada do agente

1. resumo do que foi implementado.
2. lista de arquivos alterados.
3. evidências de validação (build/testes executados).
4. pendências restantes classificadas por prioridade.
