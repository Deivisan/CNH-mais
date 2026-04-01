// ============================================
// ADMIN-EXTRA.JS - Dashboard Analítico Admin
// ============================================

// Extende o módulo admin com funcionalidades extras
(function() {
  
  // Guarda navegação original
  const originalNavigate = admin.navigate;
  
  // Novas tabs disponíveis
  const extraTabs = ['analytics', 'configuracoes', 'logs'];
  
  // Sobrescreve navegação para incluir extras
  admin.navigate = function(tab) {
    if (extraTabs.includes(tab)) {
      this.currentTab = tab;
      
      // Atualiza tabs ativas
      document.querySelectorAll('#admin-tabs .tab').forEach(tabEl => {
        tabEl.classList.remove('active');
        if (tabEl.dataset.tab === tab) {
          tabEl.classList.add('active');
        }
      });
      
      this.renderExtra(tab);
    } else {
      originalNavigate.call(this, tab);
    }
  };
  
  // Renderizar tabs extras
  admin.renderExtra = function(tab) {
    const content = document.getElementById('admin-content');
    
    const tabs = {
      analytics: this.renderAnalytics(),
      configuracoes: this.renderConfiguracoes(),
      logs: this.renderLogs()
    };
    
    content.innerHTML = tabs[tab] || '';
    content.classList.add('fade-in');
    
    // Inicializar funcionalidades específicas
    if (tab === 'analytics') this.initAnalytics();
  };
  
  // ==================== ANALYTICS AVANÇADO ====================
  
  admin.renderAnalytics = function() {
    return `
      <!-- KPIs Principais -->
      <div class="stats-grid" style="margin-top: 0;">
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.instrutoresAtivos}</div>
          <div class="stat-label">Instrutores</div>
          <div style="font-size: 11px; color: var(--success); margin-top: 4px;">↑ 12% vs mês anterior</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.candidatosAtivos}</div>
          <div class="stat-label">Candidatos</div>
          <div style="font-size: 11px; color: var(--success); margin-top: 4px;">↑ 8% vs mês anterior</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.aulasMes}</div>
          <div class="stat-label">Aulas (Mês)</div>
          <div style="font-size: 11px; color: var(--success); margin-top: 4px;">↑ 15% vs mês anterior</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.nps}</div>
          <div class="stat-label">NPS Score</div>
          <div style="font-size: 11px; color: var(--success); margin-top: 4px;">↑ 3 pontos</div>
        </div>
      </div>
      
      <!-- Gráficos de Crescimento -->
      <div class="card">
        <div class="card-title">📈 Crescimento de Usuários (12 meses)</div>
        <canvas id="chart-usuarios" style="max-height: 250px;"></canvas>
      </div>
      
      <div class="card">
        <div class="card-title">💰 Evolução Financeira (12 meses)</div>
        <canvas id="chart-financeiro-anual" style="max-height: 250px;"></canvas>
      </div>
      
      <!-- Métricas de Qualidade -->
      <div class="card">
        <div class="card-title">⭐ Métricas de Qualidade</div>
        
        <div style="margin-bottom: 16px;">
          <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
            <span style="font-size: 14px; font-weight: 500;">NPS Score</span>
            <span style="font-size: 14px; font-weight: 700; color: var(--success);">${this.data.overview.nps}/100</span>
          </div>
          <div style="background: var(--divider); height: 8px; border-radius: 4px; overflow: hidden;">
            <div style="background: var(--success); height: 100%; width: ${this.data.overview.nps}%; transition: width 0.3s ease;"></div>
          </div>
        </div>
        
        <div style="margin-bottom: 16px;">
          <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
            <span style="font-size: 14px; font-weight: 500;">Taxa de Aprovação</span>
            <span style="font-size: 14px; font-weight: 700; color: var(--success);">${this.data.overview.taxaAprovacao}%</span>
          </div>
          <div style="background: var(--divider); height: 8px; border-radius: 4px; overflow: hidden;">
            <div style="background: var(--success); height: 100%; width: ${this.data.overview.taxaAprovacao}%; transition: width 0.3s ease;"></div>
          </div>
        </div>
        
        <div style="margin-bottom: 16px;">
          <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
            <span style="font-size: 14px; font-weight: 500;">Taxa de Retenção</span>
            <span style="font-size: 14px; font-weight: 700; color: var(--success);">82%</span>
          </div>
          <div style="background: var(--divider); height: 8px; border-radius: 4px; overflow: hidden;">
            <div style="background: var(--secondary); height: 100%; width: 82%; transition: width 0.3s ease;"></div>
          </div>
        </div>
        
        <div>
          <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
            <span style="font-size: 14px; font-weight: 500;">Taxa de Conclusão</span>
            <span style="font-size: 14px; font-weight: 700; color: var(--success);">94%</span>
          </div>
          <div style="background: var(--divider); height: 8px; border-radius: 4px; overflow: hidden;">
            <div style="background: var(--secondary); height: 100%; width: 94%; transition: width 0.3s ease;"></div>
          </div>
        </div>
      </div>
      
      <!-- Funil de Conversão -->
      <div class="card">
        <div class="card-title">🎯 Funil de Conversão (Março)</div>
        
        <div style="display: flex; flex-direction: column; gap: 8px;">
          ${[
            { etapa: 'Cadastros', valor: 1240, percent: 100, cor: '#4A90D9' },
            { etapa: 'Completaram Perfil', valor: 1054, percent: 85, cor: '#4A90D9' },
            { etapa: 'Escolheram Pacote', valor: 892, percent: 72, cor: '#2E7D32' },
            { etapa: 'Pagaram', valor: 756, percent: 61, cor: '#2E7D32' },
            { etapa: 'Primeira Aula', valor: 702, percent: 57, cor: '#2E7D32' }
          ].map(item => `
            <div>
              <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
                <span style="font-size: 13px; font-weight: 500;">${item.etapa}</span>
                <span style="font-size: 13px; font-weight: 700;">${item.valor} (${item.percent}%)</span>
              </div>
              <div style="background: var(--divider); height: 6px; border-radius: 3px; overflow: hidden;">
                <div style="background: ${item.cor}; height: 100%; width: ${item.percent}%; transition: width 0.3s ease;"></div>
              </div>
            </div>
          `).join('')}
        </div>
      </div>
      
      <!-- Distribuição Geográfica -->
      <div class="card">
        <div class="card-title">🗺️ Distribuição por Cidade (Top 10)</div>
        ${[
          { cidade: 'Salvador', instrutores: 156, candidatos: 3240, aulas: 2890 },
          { cidade: 'Feira de Santana', instrutores: 89, candidatos: 1456, aulas: 1289 },
          { cidade: 'Vitória da Conquista', instrutores: 67, candidatos: 987, aulas: 876 },
          { cidade: 'Camaçari', instrutores: 45, candidatos: 654, aulas: 587 },
          { cidade: 'Itabuna', instrutores: 34, candidatos: 456, aulas: 402 }
        ].map(cidade => `
          <div class="list-item">
            <div class="list-item-content">
              <div class="list-item-title">${cidade.cidade}</div>
              <div class="list-item-subtitle">
                ${cidade.instrutores} instrutores • ${cidade.candidatos} candidatos • ${cidade.aulas} aulas/mês
              </div>
            </div>
          </div>
        `).join('')}
      </div>
      
      <!-- Horários de Pico -->
      <div class="card">
        <div class="card-title">⏰ Horários de Pico (Aulas/Hora)</div>
        <div style="display: flex; align-items: flex-end; justify-content: space-between; gap: 4px; height: 150px; padding: 8px 0;">
          ${[
            { hora: '8h', aulas: 45 },
            { hora: '9h', aulas: 78 },
            { hora: '10h', aulas: 92 },
            { hora: '11h', aulas: 67 },
            { hora: '14h', aulas: 85 },
            { hora: '15h', aulas: 98 },
            { hora: '16h', aulas: 112 },
            { hora: '17h', aulas: 89 },
            { hora: '18h', aulas: 65 }
          ].map(item => {
            const maxAulas = 112;
            const heightPercent = (item.aulas / maxAulas) * 100;
            return `
              <div style="flex: 1; display: flex; flex-direction: column; align-items: center; gap: 4px;">
                <div style="font-size: 10px; font-weight: 500; color: var(--text-secondary);">${item.aulas}</div>
                <div style="width: 100%; height: ${heightPercent}%; background: ${item.aulas >= 90 ? 'var(--success)' : 'var(--secondary)'}; border-radius: 4px 4px 0 0;"></div>
                <div style="font-size: 10px; color: var(--text-secondary);">${item.hora}</div>
              </div>
            `;
          }).join('')}
        </div>
      </div>
      
      <!-- Performance por Dia da Semana -->
      <div class="card">
        <div class="card-title">📅 Performance por Dia da Semana</div>
        ${[
          { dia: 'Segunda', aulas: 1234, faturamento: 123400 },
          { dia: 'Terça', aulas: 1189, faturamento: 118900 },
          { dia: 'Quarta', aulas: 1267, faturamento: 126700 },
          { dia: 'Quinta', aulas: 1345, faturamento: 134500 },
          { dia: 'Sexta', aulas: 1423, faturamento: 142300 },
          { dia: 'Sábado', aulas: 998, faturamento: 99800 }
        ].map(item => {
          const maxAulas = 1423;
          const widthPercent = (item.aulas / maxAulas) * 100;
          return `
            <div style="margin-bottom: 12px;">
              <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
                <span style="font-size: 13px; font-weight: 500;">${item.dia}</span>
                <span style="font-size: 13px; font-weight: 700;">${item.aulas} aulas • ${helpers.formatCurrency(item.faturamento)}</span>
              </div>
              <div style="background: var(--divider); height: 6px; border-radius: 3px; overflow: hidden;">
                <div style="background: var(--secondary); height: 100%; width: ${widthPercent}%; transition: width 0.3s ease;"></div>
              </div>
            </div>
          `;
        }).join('')}
      </div>
    `;
  };
  
  admin.initAnalytics = function() {
    // Gráfico de Usuários
    const canvasUsuarios = document.getElementById('chart-usuarios');
    if (canvasUsuarios) {
      const ctx = canvasUsuarios.getContext('2d');
      const instrutores = [320, 345, 378, 402, 435, 456, 478, 489, 501, 512, 520, 512];
      const candidatos = [4200, 4580, 5120, 5678, 6234, 6789, 7123, 7456, 7890, 8123, 8234, 8340];
      const labels = ['Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez', 'Jan', 'Fev', 'Mar'];
      
      canvasUsuarios.width = canvasUsuarios.offsetWidth;
      canvasUsuarios.height = 250;
      
      this.desenharGraficoMultiLinha(ctx, [instrutores, candidatos], labels, ['#4A90D9', '#2E7D32'], ['Instrutores', 'Candidatos']);
    }
    
    // Gráfico Financeiro Anual
    const canvasFinanceiro = document.getElementById('chart-financeiro-anual');
    if (canvasFinanceiro) {
      const ctx = canvasFinanceiro.getContext('2d');
      const faturamento = [420000, 458000, 512000, 567800, 623400, 678900, 712300, 745600, 789000, 812300, 823400, 834000];
      const lucro = [84000, 91600, 102400, 113560, 124680, 135780, 142460, 149120, 157800, 162460, 164680, 166800];
      const labels = ['Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez', 'Jan', 'Fev', 'Mar'];
      
      canvasFinanceiro.width = canvasFinanceiro.offsetWidth;
      canvasFinanceiro.height = 250;
      
      this.desenharGraficoMultiLinha(ctx, [faturamento, lucro], labels, ['#4A90D9', '#2E7D32'], ['Faturamento', 'Lucro Líquido'], true);
    }
  };
  
  admin.desenharGraficoMultiLinha = function(ctx, datasets, labels, cores, nomes, isFinanceiro = false) {
    const width = ctx.canvas.width;
    const height = ctx.canvas.height;
    const padding = 50;
    const legendHeight = 30;
    
    // Encontrar min/max de todos os datasets
    const allValues = datasets.flat();
    const max = Math.max(...allValues);
    const min = Math.min(...allValues);
    const range = max - min;
    
    const chartWidth = width - padding * 2;
    const chartHeight = height - padding * 2 - legendHeight;
    
    // Limpar
    ctx.clearRect(0, 0, width, height);
    
    // Legenda
    let legendX = padding;
    nomes.forEach((nome, i) => {
      ctx.fillStyle = cores[i];
      ctx.fillRect(legendX, 10, 12, 12);
      ctx.fillStyle = '#666';
      ctx.font = '11px sans-serif';
      ctx.fillText(nome, legendX + 16, 20);
      legendX += ctx.measureText(nome).width + 30;
    });
    
    // Eixos
    ctx.strokeStyle = '#e0e0e0';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(padding, padding + legendHeight);
    ctx.lineTo(padding, height - padding);
    ctx.lineTo(width - padding, height - padding);
    ctx.stroke();
    
    // Linhas de grid horizontais
    for (let i = 0; i <= 5; i++) {
      const y = (padding + legendHeight) + (chartHeight / 5) * i;
      ctx.strokeStyle = '#f0f0f0';
      ctx.beginPath();
      ctx.moveTo(padding, y);
      ctx.lineTo(width - padding, y);
      ctx.stroke();
      
      // Labels do eixo Y
      const valor = max - (range / 5) * i;
      ctx.fillStyle = '#999';
      ctx.font = '10px sans-serif';
      ctx.textAlign = 'right';
      const texto = isFinanceiro ? helpers.formatCurrency(valor).replace('R$', '').trim() : Math.round(valor).toString();
      ctx.fillText(texto, padding - 8, y + 3);
    }
    
    // Desenhar cada dataset
    datasets.forEach((dados, datasetIndex) => {
      const cor = cores[datasetIndex];
      
      // Linha
      ctx.strokeStyle = cor;
      ctx.lineWidth = 2;
      ctx.beginPath();
      
      dados.forEach((valor, i) => {
        const x = padding + (chartWidth / (dados.length - 1)) * i;
        const y = (height - padding) - ((valor - min) / range) * chartHeight;
        
        if (i === 0) {
          ctx.moveTo(x, y);
        } else {
          ctx.lineTo(x, y);
        }
      });
      
      ctx.stroke();
      
      // Pontos
      ctx.fillStyle = cor;
      dados.forEach((valor, i) => {
        const x = padding + (chartWidth / (dados.length - 1)) * i;
        const y = (height - padding) - ((valor - min) / range) * chartHeight;
        
        ctx.beginPath();
        ctx.arc(x, y, 3, 0, Math.PI * 2);
        ctx.fill();
      });
    });
    
    // Labels do eixo X
    ctx.fillStyle = '#666';
    ctx.font = '11px sans-serif';
    ctx.textAlign = 'center';
    labels.forEach((label, i) => {
      const x = padding + (chartWidth / (labels.length - 1)) * i;
      ctx.fillText(label, x, height - padding + 20);
    });
  };
  
  // ==================== CONFIGURAÇÕES ====================
  
  admin.renderConfiguracoes = function() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">⚙️ Configurações da Plataforma</div>
      </div>
      
      <div class="card">
        <div class="card-title">💰 Configurações Financeiras</div>
        
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Comissão da Plataforma</div>
            <div class="list-item-subtitle">Porcentagem cobrada por aula</div>
          </div>
          <input type="number" value="20" min="0" max="100" 
                 style="width: 70px; padding: 8px; border: 1px solid var(--divider); border-radius: 4px; text-align: right;">
          <span style="margin-left: 4px;">%</span>
        </div>
        
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Valor Mínimo por Aula</div>
            <div class="list-item-subtitle">Preço mínimo cobrado dos candidatos</div>
          </div>
          <div style="display: flex; align-items: center; gap: 4px;">
            <span>R$</span>
            <input type="number" value="80" min="0" 
                   style="width: 80px; padding: 8px; border: 1px solid var(--divider); border-radius: 4px; text-align: right;">
          </div>
        </div>
        
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Taxa de Performance (Instrutores Top)</div>
            <div class="list-item-subtitle">Desconto na comissão para nota >4.5</div>
          </div>
          <input type="number" value="2" min="0" max="10" 
                 style="width: 70px; padding: 8px; border: 1px solid var(--divider); border-radius: 4px; text-align: right;">
          <span style="margin-left: 4px;">%</span>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">🎁 Sistema de Indicações</div>
        
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Indicações para Aula Grátis</div>
            <div class="list-item-subtitle">Quantidade de indicações válidas necessárias</div>
          </div>
          <input type="number" value="10" min="1" 
                 style="width: 80px; padding: 8px; border: 1px solid var(--divider); border-radius: 4px; text-align: right;">
        </div>
        
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Validade do Código</div>
            <div class="list-item-subtitle">Dias até o código de indicação expirar</div>
          </div>
          <input type="number" value="365" min="30" 
                 style="width: 80px; padding: 8px; border: 1px solid var(--divider); border-radius: 4px; text-align: right;">
          <span style="margin-left: 4px;">dias</span>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">⚖️ Disputas</div>
        
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Prazo para Abrir Disputa</div>
            <div class="list-item-subtitle">Dias após a aula para candidato abrir disputa</div>
          </div>
          <input type="number" value="3" min="1" max="30" 
                 style="width: 80px; padding: 8px; border: 1px solid var(--divider); border-radius: 4px; text-align: right;">
          <span style="margin-left: 4px;">dias</span>
        </div>
        
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Prazo para Resposta</div>
            <div class="list-item-subtitle">Dias para instrutor responder disputa</div>
          </div>
          <input type="number" value="2" min="1" max="10" 
                 style="width: 80px; padding: 8px; border: 1px solid var(--divider); border-radius: 4px; text-align: right;">
          <span style="margin-left: 4px;">dias</span>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">🔔 Notificações</div>
        
        <div class="list-item" style="cursor: pointer;">
          <div class="list-item-content">
            <div class="list-item-title">Email de Boas-vindas</div>
            <div class="list-item-subtitle">Enviar email ao cadastrar novo usuário</div>
          </div>
          <label class="switch">
            <input type="checkbox" checked>
            <span class="slider"></span>
          </label>
        </div>
        
        <div class="list-item" style="cursor: pointer;">
          <div class="list-item-content">
            <div class="list-item-title">Lembrete de Aula</div>
            <div class="list-item-subtitle">Notificar 1h antes da aula</div>
          </div>
          <label class="switch">
            <input type="checkbox" checked>
            <span class="slider"></span>
          </label>
        </div>
        
        <div class="list-item" style="cursor: pointer;">
          <div class="list-item-content">
            <div class="list-item-title">Notificação de Pagamento</div>
            <div class="list-item-subtitle">Alertar sobre pagamentos pendentes/recebidos</div>
          </div>
          <label class="switch">
            <input type="checkbox" checked>
            <span class="slider"></span>
          </label>
        </div>
      </div>
      
      <div class="card">
        <button class="btn btn-primary" style="width: 100%;" onclick="alert('Configurações salvas com sucesso!')">
          Salvar Todas as Configurações
        </button>
      </div>
    `;
  };
  
  // ==================== LOGS & AUDITORIA ====================
  
  admin.renderLogs = function() {
    const logs = [
      { tipo: 'aprovacao', usuario: 'Admin', acao: 'Aprovou instrutor Carlos Pereira', timestamp: '2026-03-30 14:32:10' },
      { tipo: 'disputa', usuario: 'Admin', acao: 'Resolveu disputa #001 - Favorável ao instrutor', timestamp: '2026-03-30 13:15:45' },
      { tipo: 'config', usuario: 'Admin', acao: 'Alterou comissão da plataforma de 18% para 20%', timestamp: '2026-03-30 10:22:33' },
      { tipo: 'suspensao', usuario: 'Admin', acao: 'Suspendeu instrutor João Carlos por 7 dias', timestamp: '2026-03-29 16:45:12' },
      { tipo: 'cadastro', usuario: 'Sistema', acao: 'Novo instrutor cadastrado: Fernanda Lima', timestamp: '2026-03-29 11:20:05' },
      { tipo: 'cadastro', usuario: 'Sistema', acao: 'Novo candidato cadastrado: Pedro Santos', timestamp: '2026-03-29 09:33:22' },
      { tipo: 'financeiro', usuario: 'Sistema', acao: 'Repasse processado para 45 instrutores - R$ 45.200', timestamp: '2026-03-28 00:05:00' }
    ];
    
    const tipoIcons = {
      aprovacao: { icon: 'check_circle', cor: 'var(--success)' },
      disputa: { icon: 'gavel', cor: 'var(--warning)' },
      config: { icon: 'settings', cor: 'var(--secondary)' },
      suspensao: { icon: 'block', cor: 'var(--error)' },
      cadastro: { icon: 'person_add', cor: 'var(--secondary)' },
      financeiro: { icon: 'payments', cor: 'var(--success)' }
    };
    
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">📋 Logs de Auditoria</div>
        <p class="card-subtitle">Histórico de ações administrativas e do sistema</p>
      </div>
      
      <div class="card">
        <div style="display: flex; gap: 8px; margin-bottom: 16px; overflow-x: auto;">
          <button class="btn btn-primary" style="white-space: nowrap; font-size: 13px;">Todos</button>
          <button class="btn btn-outlined" style="white-space: nowrap; font-size: 13px;">Aprovações</button>
          <button class="btn btn-outlined" style="white-space: nowrap; font-size: 13px;">Disputas</button>
          <button class="btn btn-outlined" style="white-space: nowrap; font-size: 13px;">Financeiro</button>
          <button class="btn btn-outlined" style="white-space: nowrap; font-size: 13px;">Suspensões</button>
        </div>
        
        <input type="search" placeholder="Buscar nos logs..." 
               style="width: 100%; padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit; margin-bottom: 16px;">
        
        ${logs.map(log => {
          const { icon, cor } = tipoIcons[log.tipo];
          return `
            <div class="list-item" style="align-items: flex-start;">
              <div style="width: 40px; height: 40px; border-radius: 50%; background: ${cor}20; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                <span class="material-symbols-rounded" style="color: ${cor}; font-size: 20px;">${icon}</span>
              </div>
              <div class="list-item-content">
                <div class="list-item-title">${log.acao}</div>
                <div class="list-item-subtitle">${log.usuario} • ${log.timestamp}</div>
              </div>
            </div>
          `;
        }).join('')}
      </div>
      
      <div class="card">
        <button class="btn btn-outlined" style="width: 100%;">
          <span class="material-symbols-rounded" style="font-size: 18px;">download</span>
          <span>Exportar Logs (CSV)</span>
        </button>
      </div>
    `;
  };
  
})();
