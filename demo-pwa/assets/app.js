const screens = Array.from(document.querySelectorAll('.screen'));
const menuButtons = Array.from(document.querySelectorAll('.menu-btn'));
const panels = Array.from(document.querySelectorAll('.panel'));
const viewButtons = Array.from(document.querySelectorAll('[data-go-view]'));
const panelJumpButtons = Array.from(document.querySelectorAll('[data-go-panel]'));
const forms = Array.from(document.querySelectorAll('.emulated-form'));
const actionButtons = Array.from(document.querySelectorAll('[data-action]'));
const toast = document.getElementById('toast');

const roleLabel = document.getElementById('active-role-label');
const rtRole = document.getElementById('rt-role');
const rtDestination = document.getElementById('rt-destination');
const rtAI = document.getElementById('rt-ai');
const runtimeEvents = document.getElementById('runtime-events');

const pillAI = document.getElementById('pill-ai');
const pillMap = document.getElementById('pill-map');
const systemClock = document.getElementById('system-clock');

const formLogin = document.getElementById('form-login-system');
const formRegister = document.getElementById('form-register-system');
const btnLogout = document.getElementById('btn-logout');

const candChatSend = document.getElementById('cand-chat-send');
const candChatInput = document.getElementById('cand-chat-input');
const candChatThread = document.getElementById('cand-chat-thread');

const mapCanvasFull = document.getElementById('map-canvas-full');
const mapSearch = document.getElementById('map-search');
const mapRouteText = document.getElementById('map-route-text');
const btnMapStart = document.getElementById('btn-map-start');
const btnMapArrived = document.getElementById('btn-map-arrived');

const btnSaveAI = document.getElementById('save-ai-config');
const btnClearAI = document.getElementById('clear-ai-config');
const btnTestAI = document.getElementById('test-ai-config');

const aiEndpointEl = document.getElementById('ai-endpoint');
const aiModelEl = document.getElementById('ai-model');
const aiKeyEl = document.getElementById('ai-api-key');
const aiTempEl = document.getElementById('ai-temperature');

const onbExp = document.getElementById('onb-exp');
const onbAnx = document.getElementById('onb-anx');
const onbCar = document.getElementById('onb-car');
const onbObj = document.getElementById('onb-obj');
const btnRecompute = document.getElementById('btn-recompute');

const kpiProgress = document.getElementById('kpi-progress');
const kpiInstrutor = document.getElementById('kpi-instrutor');
const kpiNext = document.getElementById('kpi-next');

const projDia = document.getElementById('proj-dia');
const projSemana = document.getElementById('proj-semana');
const projMes = document.getElementById('proj-mes');

const AI_STORAGE_KEY = 'cnhplus_demo_pwa_ai_config';

const state = {
  role: 'candidato',
  activeScreen: 'view-auth',
  activePanel: 'panel-cand-home',
  destination: null,
  aiMode: 'fallback local'
};

const roleDefaultPanel = {
  candidato: 'panel-cand-home',
  instrutor: 'panel-inst-home',
  admin: 'panel-admin-home'
};

const bairroToPoint = {
  centro: { lat: -12.2664, lon: -38.9663, label: 'centro' },
  'fraga maia': { lat: -12.2525, lon: -38.9553, label: 'fraga maia' },
  tomba: { lat: -12.2924, lon: -38.9575, label: 'tomba' },
  kalilandia: { lat: -12.2572, lon: -38.9498, label: 'kalilândia' },
  'kalilândia': { lat: -12.2572, lon: -38.9498, label: 'kalilândia' },
  baliza: { lat: -12.2292, lon: -38.9751, label: 'baliza' },
  'feira de santana': { lat: -12.2664, lon: -38.9663, label: 'feira de santana' }
};

function showToast(message) {
  if (!toast) return;
  toast.textContent = message;
  toast.classList.add('show');
  clearTimeout(showToast._timer);
  showToast._timer = setTimeout(() => toast.classList.remove('show'), 2200);
}

function addRuntimeEvent(message) {
  if (!runtimeEvents) return;
  const li = document.createElement('li');
  li.textContent = `${new Date().toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })} • ${message}`;
  runtimeEvents.prepend(li);
}

function syncRuntimeUI() {
  if (roleLabel) roleLabel.textContent = `perfil: ${state.role}`;
  if (rtRole) rtRole.textContent = state.role;
  if (rtDestination) rtDestination.textContent = state.destination || '--';
  if (rtAI) rtAI.textContent = state.aiMode;
  if (pillAI) pillAI.textContent = `ia: ${state.aiMode}`;
  if (pillMap) pillMap.textContent = `mapa: ${state.destination || 'pronto'}`;
}

function setScreen(screenId) {
  state.activeScreen = screenId;
  screens.forEach((screen) => {
    screen.classList.toggle('active', screen.id === screenId);
  });
}

function isPanelAllowedForRole(panel, role) {
  const panelRole = panel.dataset.role || 'all';
  return panelRole === 'all' || panelRole === role;
}

function setPanel(panelId) {
  const panel = document.getElementById(panelId);
  if (!panel) return;

  if (!isPanelAllowedForRole(panel, state.role)) {
    showToast('painel não disponível para este perfil');
    return;
  }

  state.activePanel = panelId;
  panels.forEach((p) => p.classList.toggle('active', p.id === panelId));
  menuButtons.forEach((btn) => btn.classList.toggle('active', btn.dataset.panel === panelId));
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function updateRoleGroups() {
  const groups = Array.from(document.querySelectorAll('[data-role-group]'));
  groups.forEach((g) => {
    g.classList.toggle('hidden', g.dataset.roleGroup !== state.role);
  });
}

function setRole(role) {
  state.role = role;
  updateRoleGroups();
  setPanel(roleDefaultPanel[role] || 'panel-cand-home');
  syncRuntimeUI();
  addRuntimeEvent(`perfil alterado para ${role}`);
}

function buildMapUrl(lat, lon) {
  const bbox = `${lon - 0.03}%2C${lat - 0.02}%2C${lon + 0.03}%2C${lat + 0.02}`;
  return `https://www.openstreetmap.org/export/embed.html?bbox=${bbox}&layer=mapnik&marker=${lat}%2C${lon}`;
}

function updateMap(point) {
  if (!point || !mapCanvasFull) return;
  mapCanvasFull.src = buildMapUrl(point.lat, point.lon);
  state.destination = point.label;
  if (mapRouteText) mapRouteText.textContent = `rota ajustada para ${point.label}`;
  syncRuntimeUI();
  addRuntimeEvent(`mapa reposicionado para ${point.label}`);
}

function findPointByText(text) {
  const normalized = text
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '');
  for (const [bairro, point] of Object.entries(bairroToPoint)) {
    const key = bairro
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
    if (normalized.includes(key)) return point;
  }
  return null;
}

function appendChat(text, side = 'bot') {
  if (!candChatThread) return;
  const bubble = document.createElement('div');
  bubble.className = `bubble ${side}`;
  bubble.textContent = text;
  candChatThread.appendChild(bubble);
  candChatThread.scrollTop = candChatThread.scrollHeight;
}

function getAIConfig() {
  try {
    const raw = localStorage.getItem(AI_STORAGE_KEY);
    if (!raw) return null;
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

function setAIConfig(config) {
  localStorage.setItem(AI_STORAGE_KEY, JSON.stringify(config));
}

function clearAIConfig() {
  localStorage.removeItem(AI_STORAGE_KEY);
}

function loadAIConfigToForm() {
  const cfg = getAIConfig();
  if (!cfg) return;
  if (aiEndpointEl && cfg.endpoint) aiEndpointEl.value = cfg.endpoint;
  if (aiModelEl && cfg.model) aiModelEl.value = cfg.model;
  if (aiTempEl && typeof cfg.temperature === 'number') aiTempEl.value = String(cfg.temperature);
}

async function requestAI(message) {
  const cfg = getAIConfig();
  if (!cfg?.endpoint || !cfg?.model || !cfg?.apiKey) return null;

  const body = {
    model: cfg.model,
    temperature: Number(cfg.temperature ?? 0.2),
    messages: [
      {
        role: 'system',
        content:
          'Você é um assistente de navegação da CNH+ Demo PWA. Responda sempre em JSON válido com chaves: action, destination (opcional), message. action pode ser move_map ou chat. Se usuário disser cheguei, responda com humor leve e inclua no fim: "ah ops, eu sou só uma IA, programada por Deivison."'
      },
      { role: 'user', content: message }
    ]
  };

  const res = await fetch(cfg.endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${cfg.apiKey}`
    },
    body: JSON.stringify(body)
  });

  if (!res.ok) throw new Error(`http ${res.status}`);
  const data = await res.json();
  return data?.choices?.[0]?.message?.content || null;
}

function parseAIResult(raw) {
  if (!raw) return null;
  const clean = String(raw).replace(/^```json\s*/i, '').replace(/^```/i, '').replace(/```$/i, '').trim();
  try {
    return JSON.parse(clean);
  } catch {
    return { action: 'chat', message: clean };
  }
}

function fallbackAI(userText) {
  const low = userText.toLowerCase();
  if (low.includes('cheguei')) {
    return { action: 'chat', message: 'cheguei no ponto ✅ ah ops, eu sou só uma IA, programada por Deivison. 😄' };
  }
  const p = findPointByText(userText);
  if (p) return { action: 'move_map', destination: p.label, message: `indo para ${p.label}` };
  return { action: 'chat', message: 'me diga um bairro de feira de santana para ajustar a rota.' };
}

async function handleCandidateMessage(text) {
  appendChat(text, 'me');

  let action = null;
  try {
    const raw = await requestAI(text);
    if (raw) {
      action = parseAIResult(raw);
      state.aiMode = 'nvidia nim ativo';
    } else {
      action = fallbackAI(text);
      state.aiMode = 'fallback local';
    }
  } catch {
    action = fallbackAI(text);
    state.aiMode = 'fallback local';
  }

  syncRuntimeUI();

  if ((action?.action || '').toLowerCase() === 'move_map') {
    const p = findPointByText(action.destination || text);
    if (p) {
      updateMap(p);
      setPanel('panel-mapas-full');
      appendChat(`${action.message || `rota ajustada para ${p.label}`}.`, 'bot');
      return;
    }
  }

  appendChat(action?.message || 'ok', 'bot');
}

function recomputeSimulation() {
  const exp = onbExp?.value || 'nunca';
  const anx = onbAnx?.value || 'alta';
  const car = onbCar?.value || 'não';
  const obj = onbObj?.value || 'aprender com calma';

  let base = 72;
  if (exp === 'frequente') base += 8;
  if (anx === 'baixa') base += 7;
  if (car === 'sim') base += 5;
  if (obj === 'passar rápido') base += 4;
  if (base > 95) base = 95;

  if (kpiProgress) kpiProgress.textContent = `${base}%`;
  if (kpiNext) kpiNext.textContent = base > 80 ? 'hoje 16:00' : 'amanhã 08:00';

  if (projDia) projDia.textContent = `R$ ${Math.round(420 + base * 0.8)}`;
  if (projSemana) projSemana.textContent = `R$ ${Math.round(2500 + base * 6)}`;
  if (projMes) projMes.textContent = `R$ ${Math.round(9800 + base * 22)}`;

  addRuntimeEvent(`recomendação recalculada com base em perfil (${base}%)`);
  showToast('plano e projeções atualizados (emulado)');
}

function bindBasicEvents() {
  viewButtons.forEach((btn) => {
    btn.addEventListener('click', () => {
      setScreen(btn.dataset.goView);
      if (btn.dataset.goView === 'view-app') setPanel(state.activePanel);
    });
  });

  panelJumpButtons.forEach((btn) => {
    btn.addEventListener('click', () => {
      setScreen('view-app');
      setPanel(btn.dataset.goPanel);
    });
  });

  menuButtons.forEach((btn) => {
    btn.addEventListener('click', () => {
      setPanel(btn.dataset.panel);
    });
  });

  forms.forEach((form) => {
    form.addEventListener('submit', (e) => {
      e.preventDefault();
      showToast(`${form.dataset.success || 'salvo'} ✅`);
      addRuntimeEvent(form.dataset.success || 'formulário salvo');
    });
  });

  actionButtons.forEach((btn) => {
    btn.addEventListener('click', () => {
      showToast(`${btn.dataset.action} (emulado)`);
      addRuntimeEvent(`${btn.dataset.action} acionado`);
    });
  });

  document.addEventListener('click', (e) => {
    const choose = e.target.closest('.choose-instrutor');
    const mapDest = e.target.closest('[data-map-dest]');
    if (choose) {
      const name = choose.dataset.name;
      if (kpiInstrutor) kpiInstrutor.textContent = name;
      showToast(`instrutor ativo: ${name}`);
      addRuntimeEvent(`match selecionado: ${name}`);
    }
    if (mapDest) {
      const p = bairroToPoint[mapDest.dataset.mapDest];
      if (p) {
        updateMap(p);
        showToast(`destino: ${p.label}`);
      }
    }
  });
}

function bindAuthEvents() {
  formLogin?.addEventListener('submit', (e) => {
    e.preventDefault();
    const role = document.getElementById('auth-role')?.value || 'candidato';
    setRole(role);
    setScreen('view-app');
    showToast(`sessão ${role} iniciada (emulado)`);
    addRuntimeEvent(`login ${role}`);
  });

  formRegister?.addEventListener('submit', (e) => {
    e.preventDefault();
    showToast('cadastro criado (emulado)');
    addRuntimeEvent('cadastro emulado realizado');
  });

  document.getElementById('btn-login-google')?.addEventListener('click', () => {
    setRole('candidato');
    setScreen('view-app');
    showToast('login google emulado');
    addRuntimeEvent('login google emulado');
  });

  btnLogout?.addEventListener('click', () => {
    setScreen('view-auth');
    showToast('sessão encerrada');
    addRuntimeEvent('logout');
  });
}

function bindChatAndMapEvents() {
  candChatSend?.addEventListener('click', async () => {
    const text = candChatInput?.value?.trim();
    if (!text) return;
    candChatInput.value = '';
    await handleCandidateMessage(text);
  });

  mapSearch?.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      const p = findPointByText(mapSearch.value || '');
      if (!p) {
        showToast('destino não reconhecido');
        return;
      }
      updateMap(p);
    }
  });

  btnMapStart?.addEventListener('click', () => {
    if (!state.destination) {
      showToast('defina um destino primeiro');
      return;
    }
    if (mapRouteText) mapRouteText.textContent = `navegação iniciada para ${state.destination}`;
    addRuntimeEvent(`navegação iniciada para ${state.destination}`);
    showToast('navegação iniciada');
  });

  btnMapArrived?.addEventListener('click', () => {
    appendChat('cheguei no destino ✅ ah ops, eu sou só uma ia, programada por Deivison. 😄', 'bot');
    addRuntimeEvent('destino concluído');
    showToast('rota finalizada');
  });
}

function bindAIConfigEvents() {
  btnSaveAI?.addEventListener('click', () => {
    const cfg = {
      endpoint: aiEndpointEl?.value?.trim(),
      model: aiModelEl?.value?.trim(),
      apiKey: aiKeyEl?.value?.trim(),
      temperature: Number(aiTempEl?.value || 0.2)
    };
    if (!cfg.endpoint || !cfg.model || !cfg.apiKey) {
      showToast('preencha endpoint, modelo e chave');
      return;
    }
    setAIConfig(cfg);
    state.aiMode = 'configurada (aguardando uso)';
    syncRuntimeUI();
    addRuntimeEvent('configuração de ia salva localmente');
    showToast('configuração de ia salva localmente');
  });

  btnClearAI?.addEventListener('click', () => {
    clearAIConfig();
    if (aiKeyEl) aiKeyEl.value = '';
    state.aiMode = 'fallback local';
    syncRuntimeUI();
    addRuntimeEvent('configuração de ia limpa');
    showToast('configuração local removida');
  });

  btnTestAI?.addEventListener('click', () => {
    setPanel('panel-cand-chat');
    setRole('candidato');
    setScreen('view-app');
    showToast('agora teste: "me leve para fraga maia"');
  });
}

function startClock() {
  const tick = () => {
    if (systemClock) {
      systemClock.textContent = new Date().toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    }
  };
  tick();
  setInterval(tick, 10000);
}

function bindShortcuts() {
  window.addEventListener('keydown', (event) => {
    if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') {
      event.preventDefault();
      setScreen('view-auth');
      showToast('atalho: autenticação');
    }
    if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'm') {
      event.preventDefault();
      setScreen('view-app');
      setPanel('panel-mapas-full');
      showToast('atalho: mapa full');
    }
  });
}

function initServiceWorker() {
  if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
      navigator.serviceWorker.register('./sw.js').catch(() => {});
    });
  }
}

function init() {
  bindBasicEvents();
  bindAuthEvents();
  bindChatAndMapEvents();
  bindAIConfigEvents();
  bindShortcuts();
  initServiceWorker();
  startClock();
  loadAIConfigToForm();
  setRole('candidato');
  syncRuntimeUI();

  btnRecompute?.addEventListener('click', recomputeSimulation);
  addRuntimeEvent('sistema pronto para emulação contínua');
}

init();
