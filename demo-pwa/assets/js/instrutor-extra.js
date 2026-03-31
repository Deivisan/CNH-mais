// ============================================
// INSTRUTOR-EXTRA.JS - Telas Avançadas Instrutor
// ============================================

// Extende o módulo instrutor com funcionalidades extras
(function() {
  
  // Guarda navegação original
  const originalNavigate = instrutor.navigate;
  const originalRender = instrutor.render;
  
  // Novas telas disponíveis
  const extraScreens = ['chat', 'mapa', 'estatisticas', 'configuracoes', 'conquistas'];
  
  // Sobrescreve navegação para incluir extras
  instrutor.navigate = function(screen) {
    if (extraScreens.includes(screen)) {
      this.currentScreen = screen;
      
      const titles = {
        chat: '💬 Chat com Alunos',
        mapa: '🗺️ Rotas das Aulas',
        estatisticas: '📊 Estatísticas Detalhadas',
        configuracoes: '⚙️ Configurações',
        conquistas: '🏆 Conquistas & Metas'
      };
      
      app.setTitle(titles[screen] || 'CNH+');
      
      // Atualiza drawer
      document.querySelectorAll('#instrutor-drawer .drawer-item').forEach(item => {
        item.classList.remove('active');
        if (item.dataset.screen === screen) {
          item.classList.add('active');
        }
      });
      
      this.renderExtra(screen);
    } else {
      originalNavigate.call(this, screen);
    }
  };
  
  // Renderizar telas extras
  instrutor.renderExtra = function(screen) {
    const content = document.getElementById('instrutor-content');
    
    const screens = {
      chat: this.renderChat(),
      mapa: this.renderMapa(),
      estatisticas: this.renderEstatisticas(),
      configuracoes: this.renderConfiguracoes(),
      conquistas: this.renderConquistas()
    };
    
    content.innerHTML = screens[screen] || '';
    content.classList.add('fade-in');
    
    // Inicializar funcionalidades específicas
    if (screen === 'chat') this.initChat();
    if (screen === 'mapa') this.initMapa();
    if (screen === 'estatisticas') this.initEstatisticas();
  };
  
  // ==================== CHAT COM ALUNOS ====================
  
  instrutor.renderChat = function() {
    const conversas = [
      {
        id: 'chat_joao',
        aluno: 'João Silva',
        foto: 'assets/img/candidato-joao.jpg',
        ultimaMensagem: 'Perfeito! Até lá 👍',
        timestamp: '10:32',
        naoLidas: 0
      },
      {
        id: 'chat_ana',
        aluno: 'Ana Costa',
        foto: 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2248%22 height=%2248%22%3E%3Ccircle fill=%22%23E91E63%22 cx=%2224%22 cy=%2224%22 r=%2224%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2220%22 fill=%22white%22%3EAC%3C/text%3E%3C/svg%3E',
        ultimaMensagem: 'Qual endereço para pegar amanhã?',
        timestamp: '09:15',
        naoLidas: 2
      },
      {
        id: 'chat_pedro',
        aluno: 'Pedro Lima',
        foto: 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2248%22 height=%2248%22%3E%3Ccircle fill=%22%239C27B0%22 cx=%2224%22 cy=%2224%22 r=%2224%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2220%22 fill=%22white%22%3EPL%3C/text%3E%3C/svg%3E',
        ultimaMensagem: 'Obrigado pela aula! Aprendi muito',
        timestamp: 'Ontem',
        naoLidas: 0
      }
    ];
    
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">Conversas (${conversas.length})</div>
        ${conversas.map(conv => `
          <div class="list-item" onclick="instrutor.abrirConversa('${conv.id}', '${conv.aluno}')" style="cursor: pointer; position: relative;">
            <img src="${conv.foto}" 
                 style="width: 48px; height: 48px; border-radius: 50%; object-fit: cover;"
                 onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2248%22 height=%2248%22%3E%3Ccircle fill=%22%234A90D9%22 cx=%2224%22 cy=%2224%22 r=%2224%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2220%22 fill=%22white%22%3E${conv.aluno.split(' ').map(n => n[0]).join('')}%3C/text%3E%3C/svg%3E'">
            <div class="list-item-content">
              <div class="list-item-title">${conv.aluno}</div>
              <div class="list-item-subtitle" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                ${conv.ultimaMensagem}
              </div>
            </div>
            <div style="display: flex; flex-direction: column; align-items: flex-end; gap: 4px;">
              <div style="font-size: 11px; color: var(--text-secondary);">${conv.timestamp}</div>
              ${conv.naoLidas > 0 ? `
                <div style="background: var(--secondary); color: white; border-radius: 10px; padding: 2px 6px; font-size: 11px; font-weight: 700;">
                  ${conv.naoLidas}
                </div>
              ` : ''}
            </div>
          </div>
        `).join('')}
      </div>
      
      <div class="card">
        <div style="padding: 32px 16px; text-align: center; color: var(--text-secondary);">
          <span class="material-symbols-rounded" style="font-size: 64px; opacity: 0.3; margin-bottom: 16px;">chat</span>
          <p>Responda rapidamente para manter a satisfação dos seus alunos!</p>
        </div>
      </div>
      
      <!-- Modal de Chat (escondido inicialmente) -->
      <div id="chat-modal" style="display: none; position: fixed; inset: 0; background: var(--background); z-index: 1000; flex-direction: column;">
        <div style="background: var(--primary); color: white; padding: 16px; display: flex; align-items: center; gap: 12px;">
          <button class="icon-btn" onclick="instrutor.fecharChat()" style="color: white;">
            <span class="material-symbols-rounded">arrow_back</span>
          </button>
          <img id="chat-aluno-foto" src="" style="width: 40px; height: 40px; border-radius: 50%;">
          <div style="flex: 1;">
            <div id="chat-aluno-nome" style="font-weight: 500;"></div>
            <div style="font-size: 12px; opacity: 0.8;">Online</div>
          </div>
        </div>
        
        <div id="chat-messages" style="flex: 1; overflow-y: auto; padding: 16px; display: flex; flex-direction: column; gap: 12px;">
          <!-- Mensagens serão inseridas aqui -->
        </div>
        
        <div style="padding: 12px; background: var(--surface); border-top: 1px solid var(--divider); display: flex; gap: 8px;">
          <input id="chat-input" type="text" placeholder="Digite sua mensagem..." 
                 style="flex: 1; padding: 12px; border: 1px solid var(--divider); border-radius: 20px; font-family: inherit;"
                 onkeypress="if(event.key === 'Enter') instrutor.enviarMensagem()">
          <button class="icon-btn" onclick="instrutor.enviarMensagem()" style="background: var(--secondary); color: white;">
            <span class="material-symbols-rounded">send</span>
          </button>
        </div>
      </div>
    `;
  };
  
  instrutor.initChat = function() {
    // Chat já inicializado no render
  };
  
  instrutor.abrirConversa = function(chatId, alunoNome) {
    const modal = document.getElementById('chat-modal');
    const foto = document.getElementById('chat-aluno-foto');
    const nome = document.getElementById('chat-aluno-nome');
    const messages = document.getElementById('chat-messages');
    
    // Encontrar aluno
    const alunos = {
      'chat_joao': {
        nome: 'João Silva',
        foto: 'assets/img/candidato-joao.jpg',
        mensagens: [
          { de: 'aluno', texto: 'Oi Maria! Bom dia', hora: '09:20' },
          { de: 'instrutor', texto: 'Bom dia João! Tudo bem?', hora: '09:22' },
          { de: 'aluno', texto: 'Sim! Confirmando nossa aula de amanhã às 9h', hora: '10:30' },
          { de: 'instrutor', texto: 'Confirmado! Te pego em casa às 9h pontualmente 👍', hora: '10:31' },
          { de: 'aluno', texto: 'Perfeito! Até lá 👍', hora: '10:32' }
        ]
      },
      'chat_ana': {
        nome: 'Ana Costa',
        foto: 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2248%22 height=%2248%22%3E%3Ccircle fill=%22%23E91E63%22 cx=%2224%22 cy=%2224%22 r=%2224%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2220%22 fill=%22white%22%3EAC%3C/text%3E%3C/svg%3E',
        mensagens: [
          { de: 'aluno', texto: 'Olá! Tudo bem?', hora: '09:10' },
          { de: 'instrutor', texto: 'Oi Ana! Tudo ótimo e você?', hora: '09:12' },
          { de: 'aluno', texto: 'Qual endereço para pegar amanhã?', hora: '09:15' }
        ]
      },
      'chat_pedro': {
        nome: 'Pedro Lima',
        foto: 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2248%22 height=%2248%22%3E%3Ccircle fill=%22%239C27B0%22 cx=%2224%22 cy=%2224%22 r=%2224%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2220%22 fill=%22white%22%3EPL%3C/text%3E%3C/svg%3E',
        mensagens: [
          { de: 'instrutor', texto: 'Ótima aula hoje Pedro! Você evoluiu muito na baliza', hora: '16:45' },
          { de: 'aluno', texto: 'Obrigado pela aula! Aprendi muito', hora: '17:02' }
        ]
      }
    };
    
    const aluno = alunos[chatId];
    if (!aluno) return;
    
    // Preencher dados
    foto.src = aluno.foto;
    nome.textContent = aluno.nome;
    
    // Renderizar mensagens
    messages.innerHTML = aluno.mensagens.map(msg => `
      <div style="display: flex; ${msg.de === 'instrutor' ? 'justify-content: flex-end;' : ''}">
        <div style="max-width: 70%; background: ${msg.de === 'instrutor' ? 'var(--secondary)' : 'var(--surface)'}; 
                    color: ${msg.de === 'instrutor' ? 'white' : 'var(--text-primary)'}; 
                    padding: 8px 12px; border-radius: 12px; ${msg.de === 'instrutor' ? 'border-bottom-right-radius: 4px;' : 'border-bottom-left-radius: 4px;'}">
          <div>${msg.texto}</div>
          <div style="font-size: 10px; opacity: 0.7; text-align: right; margin-top: 4px;">${msg.hora}</div>
        </div>
      </div>
    `).join('');
    
    // Scroll para última mensagem
    messages.scrollTop = messages.scrollHeight;
    
    // Guardar chat atual
    this.chatAtual = chatId;
    
    // Mostrar modal
    modal.style.display = 'flex';
    document.getElementById('chat-input').focus();
  };
  
  instrutor.fecharChat = function() {
    document.getElementById('chat-modal').style.display = 'none';
    this.chatAtual = null;
  };
  
  instrutor.enviarMensagem = function() {
    const input = document.getElementById('chat-input');
    const texto = input.value.trim();
    
    if (!texto) return;
    
    const messages = document.getElementById('chat-messages');
    const agora = new Date();
    const hora = `${agora.getHours().toString().padStart(2, '0')}:${agora.getMinutes().toString().padStart(2, '0')}`;
    
    // Adicionar mensagem
    const novaMensagem = document.createElement('div');
    novaMensagem.style.cssText = 'display: flex; justify-content: flex-end;';
    novaMensagem.innerHTML = `
      <div style="max-width: 70%; background: var(--secondary); color: white; padding: 8px 12px; border-radius: 12px; border-bottom-right-radius: 4px;">
        <div>${texto}</div>
        <div style="font-size: 10px; opacity: 0.7; text-align: right; margin-top: 4px;">${hora}</div>
      </div>
    `;
    
    messages.appendChild(novaMensagem);
    messages.scrollTop = messages.scrollHeight;
    
    input.value = '';
    input.focus();
    
    // Simular resposta automática (só para demo)
    setTimeout(() => {
      const resposta = document.createElement('div');
      resposta.style.cssText = 'display: flex;';
      resposta.innerHTML = `
        <div style="max-width: 70%; background: var(--surface); color: var(--text-primary); padding: 8px 12px; border-radius: 12px; border-bottom-left-radius: 4px;">
          <div>Obrigado! Entendido 👍</div>
          <div style="font-size: 10px; opacity: 0.7; text-align: right; margin-top: 4px;">${hora}</div>
        </div>
      `;
      messages.appendChild(resposta);
      messages.scrollTop = messages.scrollHeight;
    }, 1500);
  };
  
  // ==================== MAPA DE ROTAS ====================
  
  instrutor.renderMapa = function() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">📍 Próximas Aulas (Hoje)</div>
        <div class="list-item">
          <div class="list-item-icon" style="background: var(--secondary); color: white; border-radius: 50%; width: 32px; height: 32px; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 14px;">
            1
          </div>
          <div class="list-item-content">
            <div class="list-item-title">João Silva - 09:00</div>
            <div class="list-item-subtitle">Rua das Flores, 123 - Centro</div>
          </div>
        </div>
        <div class="list-item">
          <div class="list-item-icon" style="background: var(--secondary); color: white; border-radius: 50%; width: 32px; height: 32px; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 14px;">
            2
          </div>
          <div class="list-item-content">
            <div class="list-item-title">Ana Costa - 15:00</div>
            <div class="list-item-subtitle">Av. Getúlio Vargas, 456 - Kalilândia</div>
          </div>
        </div>
      </div>
      
      <div class="card">
        <div id="instrutor-map" style="width: 100%; height: 350px; border-radius: 8px; background: var(--background);"></div>
      </div>
      
      <div class="card">
        <div class="card-title">🚗 Estatísticas de Rota</div>
        <div class="list-item">
          <span>Distância Total</span>
          <span style="font-weight: 700;">24 km</span>
        </div>
        <div class="list-item">
          <span>Tempo Estimado</span>
          <span style="font-weight: 700;">35 min</span>
        </div>
        <div class="list-item">
          <span>Custo Combustível</span>
          <span style="font-weight: 700;">R$ 12,50</span>
        </div>
      </div>
    `;
  };
  
  instrutor.initMapa = function() {
    const mapDiv = document.getElementById('instrutor-map');
    if (!mapDiv || typeof L === 'undefined') return;
    
    // Coordenadas Feira de Santana
    const center = [-12.2664, -38.9663];
    
    const map = L.map('instrutor-map').setView(center, 13);
    
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(map);
    
    // Marcadores das aulas
    const aulas = [
      { pos: [-12.2600, -38.9600], aluno: 'João Silva', hora: '09:00' },
      { pos: [-12.2720, -38.9720], aluno: 'Ana Costa', hora: '15:00' }
    ];
    
    aulas.forEach((aula, i) => {
      const icon = L.divIcon({
        html: `<div style="background: var(--secondary); color: white; width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: 700; border: 3px solid white; box-shadow: 0 2px 8px rgba(0,0,0,0.3);">${i + 1}</div>`,
        className: '',
        iconSize: [32, 32]
      });
      
      L.marker(aula.pos, { icon })
        .addTo(map)
        .bindPopup(`<b>${aula.aluno}</b><br>${aula.hora}`);
    });
    
    // Linha de rota entre aulas
    L.polyline([aulas[0].pos, aulas[1].pos], {
      color: '#4A90D9',
      weight: 3,
      opacity: 0.7,
      dashArray: '10, 5'
    }).addTo(map);
  };
  
  // ==================== ESTATÍSTICAS DETALHADAS ====================
  
  instrutor.renderEstatisticas = function() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">📊 Visão Geral (Março 2026)</div>
        <div class="stats-grid" style="grid-template-columns: repeat(2, 1fr);">
          <div class="stat-card">
            <div class="stat-value">89</div>
            <div class="stat-label">Aulas Dadas</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">R$ 8.900</div>
            <div class="stat-label">Faturamento</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">4.8 ⭐</div>
            <div class="stat-label">Nota Média</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">96%</div>
            <div class="stat-label">Pontualidade</div>
          </div>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">📈 Evolução de Aulas (6 meses)</div>
        <canvas id="chart-aulas" style="max-height: 200px;"></canvas>
      </div>
      
      <div class="card">
        <div class="card-title">💰 Evolução de Faturamento</div>
        <canvas id="chart-faturamento" style="max-height: 200px;"></canvas>
      </div>
      
      <div class="card">
        <div class="card-title">⭐ Distribuição de Notas</div>
        <div style="display: flex; flex-direction: column; gap: 8px;">
          ${[5, 4, 3, 2, 1].map(nota => {
            const percent = nota === 5 ? 78 : nota === 4 ? 18 : nota === 3 ? 3 : 1;
            return `
              <div style="display: flex; align-items: center; gap: 8px;">
                <div style="width: 60px; font-size: 14px; font-weight: 500;">${nota} ${'⭐'.repeat(nota)}</div>
                <div style="flex: 1; background: var(--divider); height: 24px; border-radius: 4px; overflow: hidden;">
                  <div style="background: var(--success); height: 100%; width: ${percent}%; transition: width 0.3s ease;"></div>
                </div>
                <div style="width: 50px; text-align: right; font-size: 14px; font-weight: 700; color: var(--text-secondary);">${percent}%</div>
              </div>
            `;
          }).join('')}
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">🎯 Performance vs Meta</div>
        <div class="list-item">
          <span>Meta de Aulas/Mês</span>
          <span style="font-weight: 700; color: var(--success);">89/80 ✅</span>
        </div>
        <div class="list-item">
          <span>Meta de Faturamento</span>
          <span style="font-weight: 700; color: var(--success);">R$ 8.900/8.000 ✅</span>
        </div>
        <div class="list-item">
          <span>Meta de Nota</span>
          <span style="font-weight: 700; color: var(--success);">4.8/4.5 ✅</span>
        </div>
      </div>
    `;
  };
  
  instrutor.initEstatisticas = function() {
    // Gráficos mockados com Canvas 2D (sem bibliotecas externas)
    
    // Gráfico de Aulas
    const canvasAulas = document.getElementById('chart-aulas');
    if (canvasAulas) {
      const ctx = canvasAulas.getContext('2d');
      const dados = [56, 62, 71, 78, 84, 89];
      const labels = ['Out', 'Nov', 'Dez', 'Jan', 'Fev', 'Mar'];
      
      canvasAulas.width = canvasAulas.offsetWidth;
      canvasAulas.height = 200;
      
      this.desenharGraficoLinha(ctx, dados, labels, '#4A90D9');
    }
    
    // Gráfico de Faturamento
    const canvasFat = document.getElementById('chart-faturamento');
    if (canvasFat) {
      const ctx = canvasFat.getContext('2d');
      const dados = [5600, 6200, 7100, 7800, 8400, 8900];
      const labels = ['Out', 'Nov', 'Dez', 'Jan', 'Fev', 'Mar'];
      
      canvasFat.width = canvasFat.offsetWidth;
      canvasFat.height = 200;
      
      this.desenharGraficoLinha(ctx, dados, labels, '#2E7D32', 'R$ ');
    }
  };
  
  instrutor.desenharGraficoLinha = function(ctx, dados, labels, cor, prefix = '') {
    const width = ctx.canvas.width;
    const height = ctx.canvas.height;
    const padding = 40;
    const max = Math.max(...dados);
    const min = Math.min(...dados);
    const range = max - min;
    
    const chartWidth = width - padding * 2;
    const chartHeight = height - padding * 2;
    
    // Limpar
    ctx.clearRect(0, 0, width, height);
    
    // Eixos
    ctx.strokeStyle = '#e0e0e0';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(padding, padding);
    ctx.lineTo(padding, height - padding);
    ctx.lineTo(width - padding, height - padding);
    ctx.stroke();
    
    // Pontos e linha
    ctx.strokeStyle = cor;
    ctx.fillStyle = cor;
    ctx.lineWidth = 2;
    ctx.beginPath();
    
    dados.forEach((valor, i) => {
      const x = padding + (chartWidth / (dados.length - 1)) * i;
      const y = height - padding - ((valor - min) / range) * chartHeight;
      
      if (i === 0) {
        ctx.moveTo(x, y);
      } else {
        ctx.lineTo(x, y);
      }
    });
    
    ctx.stroke();
    
    // Pontos
    dados.forEach((valor, i) => {
      const x = padding + (chartWidth / (dados.length - 1)) * i;
      const y = height - padding - ((valor - min) / range) * chartHeight;
      
      ctx.beginPath();
      ctx.arc(x, y, 4, 0, Math.PI * 2);
      ctx.fill();
      
      // Label
      ctx.fillStyle = '#666';
      ctx.font = '11px sans-serif';
      ctx.textAlign = 'center';
      ctx.fillText(labels[i], x, height - padding + 20);
    });
  };
  
  // ==================== CONFIGURAÇÕES ====================
  
  instrutor.renderConfiguracoes = function() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">⚙️ Preferências</div>
        
        <div class="list-item" style="cursor: pointer;">
          <div class="list-item-content">
            <div class="list-item-title">Notificações Push</div>
            <div class="list-item-subtitle">Receber alertas de novas aulas e mensagens</div>
          </div>
          <label class="switch">
            <input type="checkbox" checked>
            <span class="slider"></span>
          </label>
        </div>
        
        <div class="list-item" style="cursor: pointer;">
          <div class="list-item-content">
            <div class="list-item-title">Aceitar Aulas Automaticamente</div>
            <div class="list-item-subtitle">Aceitar aulas dentro da sua disponibilidade</div>
          </div>
          <label class="switch">
            <input type="checkbox">
            <span class="slider"></span>
          </label>
        </div>
        
        <div class="list-item" style="cursor: pointer;">
          <div class="list-item-content">
            <div class="list-item-title">Modo Offline</div>
            <div class="list-item-subtitle">Pausar recebimento de novas aulas</div>
          </div>
          <label class="switch">
            <input type="checkbox">
            <span class="slider"></span>
          </label>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">🔔 Notificações</div>
        
        <div class="list-item">
          <span>Novas Aulas</span>
          <select style="padding: 8px; border: 1px solid var(--divider); border-radius: 4px;">
            <option>Sempre</option>
            <option>Apenas Urgentes</option>
            <option>Nunca</option>
          </select>
        </div>
        
        <div class="list-item">
          <span>Mensagens de Alunos</span>
          <select style="padding: 8px; border: 1px solid var(--divider); border-radius: 4px;">
            <option>Sempre</option>
            <option>Nunca</option>
          </select>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">💰 Preferências Financeiras</div>
        
        <div class="list-item">
          <span>Frequência de Repasse</span>
          <select style="padding: 8px; border: 1px solid var(--divider); border-radius: 4px;">
            <option>Diário</option>
            <option selected>Semanal</option>
            <option>Mensal</option>
          </select>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">🔒 Privacidade & Segurança</div>
        
        <div class="list-item" onclick="alert('Alterar senha')">
          <span>Alterar Senha</span>
          <span class="material-symbols-rounded">chevron_right</span>
        </div>
        
        <div class="list-item" onclick="alert('Autenticação de 2 fatores')">
          <span>Autenticação de 2 Fatores</span>
          <span class="badge badge-success">Ativo</span>
        </div>
      </div>
      
      <div class="card">
        <button class="btn btn-error" style="width: 100%;" onclick="app.logout()">
          Sair da Conta
        </button>
      </div>
      
      <style>
        .switch {
          position: relative;
          display: inline-block;
          width: 48px;
          height: 24px;
        }
        
        .switch input {
          opacity: 0;
          width: 0;
          height: 0;
        }
        
        .slider {
          position: absolute;
          cursor: pointer;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background-color: var(--divider);
          transition: .3s;
          border-radius: 24px;
        }
        
        .slider:before {
          position: absolute;
          content: "";
          height: 18px;
          width: 18px;
          left: 3px;
          bottom: 3px;
          background-color: white;
          transition: .3s;
          border-radius: 50%;
        }
        
        input:checked + .slider {
          background-color: var(--secondary);
        }
        
        input:checked + .slider:before {
          transform: translateX(24px);
        }
      </style>
    `;
  };
  
  // ==================== CONQUISTAS & METAS ====================
  
  instrutor.renderConquistas = function() {
    const conquistas = [
      { titulo: '🥇 Top 5% Instrutores', descricao: 'Entre os melhores de Feira de Santana', desbloqueado: true },
      { titulo: '⭐ Nota Máxima', descricao: 'Manter nota >4.5 por 6 meses', desbloqueado: true },
      { titulo: '🎯 100 Aulas', descricao: 'Completar 100 aulas no total', progresso: 89, total: 100 },
      { titulo: '💰 R$ 10k Faturamento', descricao: 'Atingir R$ 10.000 em um mês', progresso: 8900, total: 10000 },
      { titulo: '🏆 Mestre da Baliza', descricao: 'Aprovar 50 alunos na baliza', progresso: 42, total: 50 },
      { titulo: '⚡ Pontualidade Perfeita', descricao: '30 dias sem atrasos', progresso: 18, total: 30 }
    ];
    
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">🏆 Suas Conquistas</div>
        <p class="card-subtitle">Desbloqueie benefícios e bonificações especiais!</p>
      </div>
      
      ${conquistas.map(c => {
        if (c.desbloqueado) {
          return `
            <div class="card" style="background: linear-gradient(135deg, rgba(74, 144, 217, 0.1), rgba(135, 206, 235, 0.1)); border: 2px solid var(--secondary);">
              <div style="display: flex; gap: 16px; align-items: flex-start;">
                <div style="font-size: 48px;">${c.titulo.split(' ')[0]}</div>
                <div style="flex: 1;">
                  <div style="font-weight: 700; font-size: 16px; margin-bottom: 4px;">
                    ${c.titulo.substring(3)}
                  </div>
                  <div style="font-size: 14px; color: var(--text-secondary);">${c.descricao}</div>
                  <div style="margin-top: 8px; padding: 6px 12px; background: var(--secondary); color: white; border-radius: 20px; display: inline-block; font-size: 12px; font-weight: 500;">
                    ✓ Desbloqueado
                  </div>
                </div>
              </div>
            </div>
          `;
        } else {
          const percent = Math.round((c.progresso / c.total) * 100);
          return `
            <div class="card">
              <div style="display: flex; gap: 16px; align-items: flex-start;">
                <div style="font-size: 48px; opacity: 0.3;">${c.titulo.split(' ')[0]}</div>
                <div style="flex: 1;">
                  <div style="font-weight: 700; font-size: 16px; margin-bottom: 4px;">
                    ${c.titulo.substring(3)}
                  </div>
                  <div style="font-size: 14px; color: var(--text-secondary); margin-bottom: 12px;">${c.descricao}</div>
                  
                  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 4px;">
                    <div style="flex: 1; background: var(--divider); height: 8px; border-radius: 4px; overflow: hidden;">
                      <div style="background: var(--secondary); height: 100%; width: ${percent}%; transition: width 0.3s ease;"></div>
                    </div>
                    <div style="font-weight: 700; font-size: 14px; color: var(--text-secondary);">${percent}%</div>
                  </div>
                  
                  <div style="font-size: 12px; color: var(--text-secondary);">
                    ${c.progresso.toLocaleString('pt-BR')} / ${c.total.toLocaleString('pt-BR')}
                  </div>
                </div>
              </div>
            </div>
          `;
        }
      }).join('')}
      
      <div class="card">
        <div class="card-title">🎁 Benefícios Ativos</div>
        <div class="list-item">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--success);">verified</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">Taxa Reduzida</div>
            <div class="list-item-subtitle">18% ao invés de 20% (economia de 2%)</div>
          </div>
        </div>
        <div class="list-item">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--secondary);">star</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">Prioridade no Match</div>
            <div class="list-item-subtitle">Aparece primeiro para novos alunos</div>
          </div>
        </div>
      </div>
    `;
  };
  
})();
