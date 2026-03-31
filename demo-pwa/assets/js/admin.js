// ============================================
// ADMIN.JS - Módulo Administrativo
// ============================================

const admin = {
  
  currentTab: 'overview',
  data: null,
  
  // Inicialização
  init() {
    this.data = mockData.admin;
    this.navigate('overview');
  },
  
  // Navegação entre tabs
  navigate(tab) {
    this.currentTab = tab;
    
    // Atualiza tabs ativas
    document.querySelectorAll('#admin-tabs .tab').forEach(tabEl => {
      tabEl.classList.remove('active');
      if (tabEl.dataset.tab === tab) {
        tabEl.classList.add('active');
      }
    });
    
    // Renderiza conteúdo
    this.render(tab);
  },
  
  // Renderizar conteúdo
  render(tab) {
    const content = document.getElementById('admin-content');
    
    const tabs = {
      overview: this.renderOverview(),
      instrutores: this.renderInstrutores(),
      candidatos: this.renderCandidatos(),
      financeiro: this.renderFinanceiro(),
      disputas: this.renderDisputas()
    };
    
    content.innerHTML = tabs[tab] || '';
    content.classList.add('fade-in');
  },
  
  // ==================== TELAS ====================
  
  renderOverview() {
    return `
      <!-- KPIs Principais -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.instrutoresAtivos}</div>
          <div class="stat-label">Instrutores Ativos</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.candidatosAtivos}</div>
          <div class="stat-label">Candidatos Ativos</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.aulasHoje}</div>
          <div class="stat-label">Aulas Hoje</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.nps}</div>
          <div class="stat-label">NPS Score</div>
        </div>
      </div>
      
      <!-- Faturamento -->
      <div class="card">
        <div class="card-title">💰 Faturamento do Mês</div>
        <div style="font-size: 32px; font-weight: 700; color: var(--success); margin-bottom: 16px;">
          ${this.data.overview.faturamentoMes}
        </div>
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px;">
          <div>
            <div style="font-size: 12px; color: var(--text-secondary);">Ticket Médio</div>
            <div style="font-size: 20px; font-weight: 700;">${this.data.overview.ticketMedio}</div>
          </div>
          <div>
            <div style="font-size: 12px; color: var(--text-secondary);">Taxa Aprovação</div>
            <div style="font-size: 20px; font-weight: 700; color: var(--success);">${this.data.overview.taxaAprovacao}%</div>
          </div>
        </div>
      </div>
      
      <!-- Aulas -->
      <div class="card">
        <div class="card-title">📊 Aulas</div>
        <div class="list-item">
          <span>Hoje</span>
          <span style="font-weight: 700;">${this.data.overview.aulasHoje} aulas</span>
        </div>
        <div class="list-item">
          <span>Esta Semana</span>
          <span style="font-weight: 700;">${this.data.overview.aulasSemana} aulas</span>
        </div>
        <div class="list-item">
          <span>Este Mês</span>
          <span style="font-weight: 700;">${this.data.overview.aulasMes} aulas</span>
        </div>
      </div>
      
      <!-- Top Instrutores -->
      <div class="card">
        <div class="card-title">🏆 Top Instrutores do Mês</div>
        ${this.data.topInstrutores.slice(0, 5).map((inst, i) => `
          <div class="list-item">
            <span style="font-weight: 700; color: var(--text-secondary); width: 24px;">#${i + 1}</span>
            <div class="list-item-content">
              <div class="list-item-title">${inst.nome}</div>
              <div class="list-item-subtitle">${inst.cidade} • ${inst.aulas} aulas • ${helpers.getStars(inst.nota)}</div>
            </div>
            <span style="font-weight: 700; color: var(--success);">${helpers.formatCurrency(inst.faturamento)}</span>
          </div>
        `).join('')}
      </div>
    `;
  },
  
  renderInstrutores() {
    return `
      <!-- Filtros -->
      <div class="card" style="margin-top: 0;">
        <div style="display: flex; gap: 8px; overflow-x: auto;">
          <button class="btn btn-primary" style="white-space: nowrap;">Todos (${this.data.overview.instrutoresAtivos})</button>
          <button class="btn btn-outlined" style="white-space: nowrap;">Pendentes (${this.data.instrutoresPendentes.length})</button>
          <button class="btn btn-outlined" style="white-space: nowrap;">Suspensos (0)</button>
        </div>
      </div>
      
      <!-- Instrutores Pendentes -->
      <div class="card">
        <div class="card-title">⏳ Aguardando Aprovação</div>
        ${this.data.instrutoresPendentes.map(inst => `
          <div class="list-item">
            <div class="list-item-content">
              <div class="list-item-title">${inst.nome}</div>
              <div class="list-item-subtitle">${inst.cidade} • ${helpers.formatDate(inst.data)}</div>
            </div>
            <div class="list-item-action">
              <span class="badge badge-warning">${inst.status === 'aguardando_documentos' ? 'Docs Pendentes' : 'Em Análise'}</span>
            </div>
          </div>
        `).join('')}
      </div>
      
      <!-- Busca -->
      <div class="card">
        <input type="search" placeholder="Buscar instrutor..." 
               style="width: 100%; padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit;">
      </div>
      
      <!-- Lista Instrutores Ativos (top 10) -->
      <div class="card">
        <div class="card-title">✅ Instrutores Ativos</div>
        ${this.data.topInstrutores.map(inst => `
          <div class="list-item" onclick="admin.verInstrutor('${inst.nome}')">
            <div class="list-item-content">
              <div class="list-item-title">${inst.nome}</div>
              <div class="list-item-subtitle">${inst.cidade} • Nota ${inst.nota} • ${inst.aulas} aulas/mês</div>
            </div>
            <span class="material-symbols-rounded" style="color: var(--text-secondary);">chevron_right</span>
          </div>
        `).join('')}
      </div>
    `;
  },
  
  renderCandidatos() {
    return `
      <!-- Stats -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.candidatosAtivos}</div>
          <div class="stat-label">Total Ativos</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">234</div>
          <div class="stat-label">Novos (Mês)</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">187</div>
          <div class="stat-label">Concluíram</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.taxaAprovacao}%</div>
          <div class="stat-label">Taxa Aprovação</div>
        </div>
      </div>
      
      <!-- Busca -->
      <div class="card">
        <input type="search" placeholder="Buscar candidato por nome, CPF ou email..." 
               style="width: 100%; padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit;">
      </div>
      
      <!-- Candidatos Recentes -->
      <div class="card">
        <div class="card-title">🆕 Cadastros Recentes</div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">João Silva</div>
            <div class="list-item-subtitle">Feira de Santana - BA • Cadastro em 28/03/2026</div>
          </div>
          <span class="badge badge-success">Ativo</span>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Maria Oliveira</div>
            <div class="list-item-subtitle">Salvador - BA • Cadastro em 29/03/2026</div>
          </div>
          <span class="badge badge-info">Aguardando 1ª aula</span>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Pedro Santos</div>
            <div class="list-item-subtitle">Vitória da Conquista - BA • Cadastro em 30/03/2026</div>
          </div>
          <span class="badge badge-success">Ativo</span>
        </div>
      </div>
      
      <!-- Candidatos em Risco -->
      <div class="card">
        <div class="card-title">⚠️ Atenção Necessária</div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Ana Paula Silva</div>
            <div class="list-item-subtitle">3 aulas canceladas consecutivas</div>
          </div>
          <span class="badge badge-warning">Risco</span>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Carlos Mendes</div>
            <div class="list-item-subtitle">Sem agendar aula há 15 dias</div>
          </div>
          <span class="badge badge-warning">Inativo</span>
        </div>
      </div>
    `;
  },
  
  renderFinanceiro() {
    const fin = this.data.financeiroMes;
    
    return `
      <!-- Resumo Financeiro -->
      <div class="card" style="margin-top: 0; background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%); color: white;">
        <div style="font-size: 14px; opacity: 0.9; margin-bottom: 4px;">Lucro Líquido (Março)</div>
        <div style="font-size: 36px; font-weight: 700; margin-bottom: 8px;">${helpers.formatCurrency(fin.lucroLiquido)}</div>
        <div style="font-size: 13px; opacity: 0.8;">
          Faturamento: ${helpers.formatCurrency(fin.totalRecebido)} • Margem: ${Math.round((fin.lucroLiquido / fin.totalRecebido) * 100)}%
        </div>
      </div>
      
      <!-- Breakdown Receitas -->
      <div class="card">
        <div class="card-title">💵 Receitas</div>
        <div class="list-item">
          <span>Total Recebido</span>
          <span style="font-weight: 700; color: var(--success);">${helpers.formatCurrency(fin.totalRecebido)}</span>
        </div>
        <div class="list-item">
          <span>Comissão Plataforma (20%)</span>
          <span style="font-weight: 700;">${helpers.formatCurrency(fin.comissaoPlataforma)}</span>
        </div>
      </div>
      
      <!-- Breakdown Despesas -->
      <div class="card">
        <div class="card-title">💸 Despesas</div>
        <div class="list-item">
          <span>Repasses Instrutores (80%)</span>
          <span style="font-weight: 700; color: var(--error);">- ${helpers.formatCurrency(fin.totalPago)}</span>
        </div>
        <div class="list-item">
          <span>Taxas Cartão (3%)</span>
          <span style="font-weight: 700; color: var(--error);">- ${helpers.formatCurrency(fin.taxasCartao)}</span>
        </div>
        <div class="list-item">
          <span>Taxas PIX (0.5%)</span>
          <span style="font-weight: 700; color: var(--error);">- ${helpers.formatCurrency(fin.taxasPix)}</span>
        </div>
      </div>
      
      <!-- Gráfico Simulado -->
      <div class="card">
        <div class="card-title">📈 Evolução (Últimos 6 meses)</div>
        <div style="height: 200px; display: flex; align-items: flex-end; justify-content: space-around; gap: 8px; padding: 16px 0;">
          ${['Out', 'Nov', 'Dez', 'Jan', 'Fev', 'Mar'].map((mes, i) => {
            const height = 60 + (i * 15);
            return `
              <div style="flex: 1; display: flex; flex-direction: column; align-items: center; gap: 8px;">
                <div style="width: 100%; height: ${height}%; background: linear-gradient(180deg, var(--secondary), var(--accent)); border-radius: 4px;"></div>
                <div style="font-size: 11px; color: var(--text-secondary);">${mes}</div>
              </div>
            `;
          }).join('')}
        </div>
      </div>
      
      <!-- Métricas -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.aulasMes}</div>
          <div class="stat-label">Aulas no Mês</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.overview.ticketMedio}</div>
          <div class="stat-label">Ticket Médio</div>
        </div>
      </div>
    `;
  },
  
  renderDisputas() {
    return `
      <!-- Stats -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.disputasAbertas.length}</div>
          <div class="stat-label">Abertas</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">12</div>
          <div class="stat-label">Resolvidas (Mês)</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">0.8%</div>
          <div class="stat-label">Taxa Disputa</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">2.1</div>
          <div class="stat-label">Tempo Médio (dias)</div>
        </div>
      </div>
      
      <!-- Disputas Abertas -->
      <div class="card">
        <div class="card-title">⚖️ Aguardando Análise</div>
        ${this.data.disputasAbertas.map(disp => `
          <div class="list-item" onclick="admin.verDisputa('${disp.id}')">
            <div class="list-item-content">
              <div class="list-item-title">${disp.candidato} vs ${disp.instrutor}</div>
              <div class="list-item-subtitle">${disp.motivo} • ${helpers.formatDate(disp.data)}</div>
            </div>
            <div class="list-item-action">
              <span class="badge badge-warning">${disp.status === 'em_analise' ? 'Em Análise' : 'Aguardando Resposta'}</span>
            </div>
          </div>
        `).join('')}
      </div>
      
      <!-- Histórico Recente -->
      <div class="card">
        <div class="card-title">✅ Resolvidas Recentemente</div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Caso #456 - Pagamento liberado</div>
            <div class="list-item-subtitle">Resolvido em 27/03/2026 • Decisão: Favorável ao instrutor</div>
          </div>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Caso #455 - Reembolso parcial</div>
            <div class="list-item-subtitle">Resolvido em 26/03/2026 • Decisão: 50% reembolso</div>
          </div>
        </div>
      </div>
    `;
  },
  
  // Placeholder para ações
  verInstrutor(nome) {
    alert(`🔍 Detalhes do Instrutor: ${nome}\n\nEm breve você terá acesso aos 12 blocos de gerenciamento detalhado conforme especificação do BACKOFFICE.md`);
  },
  
  verDisputa(id) {
    alert(`⚖️ Análise de Disputa #${id}\n\nEm breve você poderá analisar evidências, ler versões das partes e tomar decisões.`);
  }
  
};
