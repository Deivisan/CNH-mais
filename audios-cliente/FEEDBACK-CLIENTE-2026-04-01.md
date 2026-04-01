# Feedback do Cliente — 01/04/2026

> Transcrições de 8 áudios enviados pelo cliente via WhatsApp sobre o projeto CNH+.

---

## Parte 1 — Transcrições Completas (Raw)

### Áudio 1 — 10:57:46 AM

> Tanto bem amigo, já deu uma olhada aqui na estrutura, só num violão desejado é a localização, que isso aí você não vai colocar, né?
> O cliente é localizar onde vai ser o encontro, a pessoa que clica de geolocalização, já aparece o Google Maps, mostrando onde vai ser o encontro.
> Então eu e seria interessante de repente, a gente também tem as aulas gravada, tem um dispositivo para aqui, a água luna, toda vez que for da aula vai ter a câmera filmando a aula, para ela está lá o arquivo de cada luna, mas ela acho que ele tomou e como foi o desenvolvimento.

### Áudio 2 — 10:58:12 AM

> Tipo a gravação é feita dentro do carro, a gente acompanha a câmera e aí terminou de gravar já vai direto por banco de dados.
> Então eu já manda por banco de dados, fica na página do cliente — aula que ele tomou.

### Áudio 3 — 11:01:17 AM

> Como eu não falei, só ficou falado, só não via o, tem que ter o dispositivo para a geolocalização.
> Tipo marcar onde vai ser a aula, a pessoa clica e aí já o Google Maps, já mostra, já direciona a pessoa com endereço, chegando lá a pessoa vai, a aula vai ser gravada, toda a aula vai ser gravada, gravou a aula vai para a página do aluno e para a página do instrutor.

### Áudio 4 — 11:02:11 AM

> E não, pra ver, não vir também um dispositivo de ganhos, né?
> De ganhos, como é que...
> Toda a aula que o cliente fez, confirmou a aula, já vai pra página de ganhos, a aula que ele deu e o valor, que já estaria a estar disponível, que ele vai sacar a cada 8 dias, tipo, na segunda-feira, ou na sexta, vai ser o dia o que vai estar na conta dele.
> Então, tem que ter esse controle de aulas, as aulas que ele vai fazendo, e vai estar lá já falando, ou o dia, ou olhar, o que foi o aluno, e o valor da aula.

### Áudio 5 — 11:04:07 AM

> Ah, o perfeito, já está ótimo, você é um cara das 10, você já deu assim um passo gigante.
> Ah, o visual também pode melhorar o visual, o visual das páginas, pode tornar ela mais interessante, não fica tão estática, tipo, existe um banner em cima, em todas as páginas que entrarem um banner em cima.
> O banner, tipo, o banner, rola antes, estudos chão na onda, a gente vai botar alguns comunicados, qualquer pessoa que ainda é enquanto está vendo o que ele quer, vai estar passando algumas pelas paginas e algumas informações que a gente quer que tenha, então é importante que em cada página tenha essa estrutura de banner, entendeu?
> E é um cliente que a pessoa que entra como aluno, vai estar o banner um pouco abaixo do banner, o qual a posição, vai ter tipo um local com a foto da pessoa, se algo redondo, com a foto dele, identificando que ele está logado, a paz daquele navegar vai estar lá mostrando a foto dele, tipo assim, eu acho que também interessante, entendeu?
> Então, me desculpe.

### Áudio 6 — 11:04:44 AM

> Da mesma forma na página do aluno, é importante que tenham também ali o que depois faz do cadastro, formando o valor que ele pagou, o pacote que ele pegou, de quantas aulas são a opção da área de membro de contexto, mas a aula existe a ser de precisar o valor que vai ser a aula e está.

### Áudio 7 — 11:05:44 AM

> Que a gente vai ter o banner de cima e um banner também no rodapé, porque em cima é para informações mais importantes, mais embaixo dois botões, um para o aluno ter desconto em postos que vai estar cadastrado conosco dando desconto da gasolina, que é o benefício e também o banner da seguradora que ele pode assinar pelo aplicativo para o carro dele e também vai ter desconto.

### Áudio 8 — 11:06:46 AM

> Se dispor esses dois banners em baixo com desconto na gasolina, pode ser em baixo e pode ser em cima, logo embaixo do banner maior, tem uma linha mais fina com esses dois, essas duas opções que vai ser fixa, vai estar lá o desconto — ganha testmônio sem desconto na sua gasolina, confira aqui. A pessoa vai clicar ali e vai acessar com a rede de postos de gasolina que está dando desconto.
> Da mesma forma vai ser a seguradora, vai ter lá o nome da seguradora credenciada que a eles que a gente está pelo aplicativo ele ganha desconto na assinatura dele da seguradora.

---

## Parte 2 — Ideias e Pendências Extraídas

> Lista organizada de tudo que o cliente pediu para ser incorporado no projeto CNH+, categorizada por área.

### 📍 Geolocalização / Localização da Aula
- [ ] Implementar funcionalidade de geolocalização para marcar o local de encontro da aula
- [ ] Ao clicar no endereço, abrir Google Maps com a rota/direções
- [ ] O aluno consegue selecionar onde será o encontro direto no app
- [ ] A localização é fixada para a aula registrada

### 🎥 Gravação das Aulas
- [ ] Sistema para gravar as aulas práticas em vídeo dentro do carro
- [ ] Após a aula, o vídeo é enviado automaticamente ao banco de dados/storage
- [ ] Vídeo da aula fica disponível na página do aluno (histórico pessoal)
- [ ] Vídeo da aula também fica disponível na página do instrutor
- [ ] Controle de quais vídeos foram consumidos e como foi o desenvolvimento

### 💰 Sistema de Ganhos (Instrutor)
- [ ] Criar página "Ganhos" para o instrutor
- [ ] Toda aula confirmada aparece na lista de ganhos com:
  - [ ] Data da aula
  - [ ] Nome do aluno
  - [ ] Valor da aula
- [ ] Saldo fica disponível para saque
- [ ] Ciclo de saque: a cada 8 dias (ex: toda segunda ou toda sexta)
- [ ] Controle completo de aulas realizadas pelo instrutor

### 🎨 Visual e UX
- [ ] Melhorar visual das páginas — evitar layout estático
- [ ] Tornar a interface mais viva e interessante
- [ ] Manter identidade visual CNH+ (azul degradê)

### 📢 Sistema de Banners
- [ ] Banner superior (topo) em todas as páginas — para comunicados e informações importantes
- [ ] Banner deve ser um carrossel rotativo (slider)
- [ ] Banner inferior (rodapé) fixo com duas opções:
  - [ ] Desconto em postos de gasolina (parceiros cadastrados)
  - [ ] Desconto em seguradora (parceira credenciada)
- [ ] Alternativa: barra fina fixa logo abaixo do banner principal com os dois banners de parceria
- [ ] Banner de combustível: texto tipo "Ganhe desconto na sua gasolina! Confira aqui" → leva à lista de postos parceiros
- [ ] Banner de seguro: mostra nome da seguradora credenciada → leva à contratação com desconto

### 👤 Perfil / Identificação
- [ ] Foto do usuário circular exibida em todas as páginas (header/navbar)
- [ ] Indicador visual de que o usuário está logado
- [ ] Avatar redondo no topo da navegação

### 📋 Página do Aluno — Resumo
- [ ] Exibir informações do cadastro do aluno
- [ ] Exibir valor pago pelo pacote
- [ ] Exibir quantidade de aulas do pacote contratado
- [ ] Exibir aulas concluídas vs. aulas restantes
- [ ] Acesso à área de membro
- [ ] Previsão de valor por aula individual
- [ ] Contexto completo do histórico do aluno

---

**Fonte:** 8 áudios WhatsApp — 01/04/2026, das 10:57 às 11:06
**Transcrito por:** Whisper (OpenAI) — modelo base, idioma PT-BR
**Refatorado por:** DevSan — 01/04/2026
