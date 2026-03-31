# CNH+ Landing Page

Esta é a landing page estática do projeto CNH+.

## Como Usar

Para visualizar a landing page localmente:

1. Abra o arquivo `index.html` em seu navegador:
```bash
# Linux
xdg-open landing/index.html

# ou simplesmente arraste o arquivo para o navegador
```

## GitHub Pages

Para hospedar no GitHub Pages:

1. Vá para Settings > Pages do repositório
2. Em "Build and deployment" > "Source", selecione **Deploy from a branch**
3. Em "Branch", selecione `master` (ou `main`) e pasta `/ (root)`
4. Clique em Save

A landing page estará disponível em: `https://deivisan.github.io/CNH-mais/`

### Demo PWA (fluxo completo emulado)

Também há uma demo navegável em `./demo-pwa/` para apresentar o produto em modo app:

- URL esperada (quando publicado): `https://deivisan.github.io/CNH-mais/demo-pwa/`
- Inclui login/cadastro dos 3 perfis, chat emulado, mapas e telas funcionais "entre aspas"
- Ideal para validação do fluxo antes do backend/apk final

Documentos de gestão da Demo PWA:

- `docs/ROADMAP-DEMO-PWA-20DIAS.md`
- `docs/CHECKLIST-VALIDACAO-DEMO-PWA.md`
- `docs/ESPEC-COBERTURA-DEMO-PWA.md`

## Estrutura

```
landing/
└── index.html    # Landing page completa
```

## Características

- ✅ Design responsivo (mobile, tablet, desktop)
- ✅ Cores CNH+ (azul #1E3A5F + celeste #87CEEB)
- ✅ Seções: Hero, Sobre, Como Funciona, Diferenciais, Instrutores, CTA, Footer
- ✅ Animações CSS (float, hover effects)
- ✅ Sem dependências externas (exceto Google Fonts)
- ✅ Totalmente estática

## Status atual

- ⏸️ Landing principal do APK: **em pausa** aguardando aprovação de visual e designer
- 🚀 Demo PWA: pronta para evolução e apresentação de fluxo completo
