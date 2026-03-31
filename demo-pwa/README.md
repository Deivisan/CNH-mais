# CNH+ Demo PWA (GitHub Pages)

protótipo navegável da cnh+ para validação de produto com fluxo completo **emulado**.

## objetivo

- manter a landing principal focada no apk
- entregar um ambiente "appficado" para navegar todas as funcionalidades
- demonstrar os 3 perfis (candidato, instrutor, admin) sem backend real

## o que já está emulado (modo sistema)

- autenticação com seleção de perfil e troca de contexto no app
- shell contínuo de sistema (sidebar + runtime + eventos)
- jornadas operacionais para candidato, instrutor e admin
- chat com interpretação semântica de rota (ia opcional + fallback local)
- aba dedicada **mapa full** com controles de navegação
- indicadores e projeções atualizadas por ações do usuário (emulação de tempo real)

## assistente ia (opcional)

- tela: `candidato > assistente ia`
- endpoint padrão: `https://integrate.api.nvidia.com/v1/chat/completions`
- modelo padrão: `minimaxai/minimax-m2.5`
- a chave é salva **somente no localStorage do navegador** (não versionada)
- fallback local continua funcionando sem ia remota (bairro → mapa)

## arquitetura de interação

- `view-auth`: entrada no sistema
- `view-app`: operação contínua estilo app android
- `panel-mapas-full`: aba inteira de mapa dentro do sistema
- `panel-runtime`: monitor de estado/eventos da sessão emulada

## limitações do github pages (estático)

- sem api segura no servidor (não guardar segredo/token)
- sem banco real no backend
- sem websocket real (chat é emulação local)
- sem autenticação real (apenas mock visual)

## como rodar

abra `demo-pwa/index.html` ou publique no pages.

## fluxo de publicação sugerido

- branch principal: landing atual
- branch pages-pwa: experiência pwa navegável
- depois de aprovado: integrar links e storytelling entre as duas
