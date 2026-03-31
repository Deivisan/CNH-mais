# CNH+ Demo PWA (GitHub Pages)

protótipo navegável da cnh+ para validação de produto com fluxo completo **emulado**.

## objetivo

- manter a landing principal focada no apk
- entregar um ambiente "appficado" para navegar todas as funcionalidades
- demonstrar os 3 perfis (candidato, instrutor, admin) sem backend real

## o que já está emulado

- login e cadastro dos 3 perfis
- telas principais de candidato/instrutor/admin
- chat simulado com resposta automática
- chat com modo assistente ia opcional (nvidia nim / openai-compatible)
- mapas com openstreetmap embed e troca de pontos
- ações de match, agenda, financeiro e suporte (com feedback visual)

## assistente ia (opcional)

- tela: `candidato > assistente ia`
- endpoint padrão: `https://integrate.api.nvidia.com/v1/chat/completions`
- modelo padrão: `minimaxai/minimax-m2.5`
- a chave é salva **somente no localStorage do navegador** (não versionada)
- fallback local continua funcionando mesmo sem ia remota (rota por bairros conhecidos)

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
