let API = localStorage.getItem('api_base') || 'http://localhost:8080';
const API_CANDIDATES = ['http://localhost:8080','http://localhost:8081'];
function setApiBase(b){ API = b; localStorage.setItem('api_base', b); }
function setToken(t){ localStorage.setItem('garage_token', t); }
function getToken(){ return localStorage.getItem('garage_token'); }
let safeMode = true;
function setSafeMode(v){ safeMode = !!v; }
function uid(){ return Date.now().toString(36)+Math.random().toString(36).slice(2,8); }
function loadLS(k){ try { return JSON.parse(localStorage.getItem(k) || '[]'); } catch { return []; } }
function saveLS(k,v){ localStorage.setItem(k, JSON.stringify(v)); }
function getCurrentUser(){ try { return JSON.parse(localStorage.getItem('current_user')||'null'); } catch { return null; } }
function setCurrentUser(u){ localStorage.setItem('current_user', JSON.stringify(u)); }
function isLoggedIn(){ return !!getToken(); }
function logout(){ localStorage.removeItem('garage_token'); localStorage.removeItem('current_user'); location.href = 'login.html'; }
async function mockFetch(path, opts={}){
  const method = (opts.method||'GET').toUpperCase();
  const body = opts.body ? JSON.parse(opts.body) : {};
  if (path.startsWith('/auth/register') && method==='POST'){
    const users = loadLS('users');
    const u = { id: uid(), nombre: body.nombre||'Usuario', email: body.email, role: body.role||'USER' };
    users.push(u); saveLS('users', users); setCurrentUser(u); const token = 'mock-'+u.role+'-'+u.id; setToken(token);
    return { token, role: u.role };
  }
  if (path.startsWith('/auth/login') && method==='POST'){
    const users = loadLS('users');
    const u = users.find(x => x.email===body.email);
    if (!u) return { token: 'mock-USER-'+uid(), role: 'USER' };
    setCurrentUser(u); const token = 'mock-'+u.role+'-'+u.id; setToken(token); return { token, role: u.role };
  }
  if (path==='/user/me' && method==='GET'){
    const u = getCurrentUser(); if (!u) return { id:'', nombre:'', email:'', role:'' };
    return { id: u.id, nombre: u.nombre, email: u.email, role: u.role };
  }
  if (path==='/vehicles' && method==='POST'){
    const v = { id: uid(), plate: body.plate, brand: body.brand, model: body.model, type: body.type||'CARRO', color: body.color||'', userId: (getCurrentUser()||{}).id };
    const arr = loadLS('vehicles'); arr.push(v); saveLS('vehicles', arr); return { id: v.id, plate: v.plate, brand: v.brand, model: v.model, type: v.type, color: v.color };
  }
  if (path==='/vehicles/my' && method==='GET'){
    const cu = getCurrentUser(); const arr = loadLS('vehicles').filter(v => !cu || v.userId===cu.id);
    return arr.map(v => ({ id: v.id, plate: v.plate, brand: v.brand, model: v.model, type: v.type, color: v.color }));
  }
  if (path.startsWith('/vehicles/') && method==='PUT'){
    const id = path.split('/')[2];
    const arr = loadLS('vehicles');
    const idx = arr.findIndex(v => v.id===id);
    if (idx>=0){ arr[idx] = { ...arr[idx], plate: body.plate||arr[idx].plate, brand: body.brand||arr[idx].brand, model: body.model||arr[idx].model, type: body.type||arr[idx].type, color: body.color||arr[idx].color }; saveLS('vehicles', arr); }
    const v = arr.find(x => x.id===id) || {};
    return { id: v.id, plate: v.plate, brand: v.brand, model: v.model, type: v.type, color: v.color };
  }
  if (path.startsWith('/vehicles/') && method==='DELETE'){
    const id = path.split('/')[2];
    const arr = loadLS('vehicles').filter(v => v.id!==id); saveLS('vehicles', arr);
    return { status: 'DELETED' };
  }
  if (path==='/reservations' && method==='POST'){
    const r = { id: uid(), vehicleId: body.vehicleId, parkingLotId: body.parkingLotId, from: body.from, to: body.to, userId: (getCurrentUser()||{}).id };
    const arr = loadLS('reservations'); arr.push(r); saveLS('reservations', arr); return { id: r.id, vehicleId: r.vehicleId, parkingLotId: r.parkingLotId, from: r.from, to: r.to };
  }
  if (path==='/reservations/my' && method==='GET'){
    const cu = getCurrentUser(); const arr = loadLS('reservations').filter(r => !cu || r.userId===cu.id);
    return arr.map(r => ({ id: r.id, vehicleId: r.vehicleId, parkingLotId: r.parkingLotId, from: r.from, to: r.to }));
  }
  if (path.startsWith('/reservations/') && method==='PUT'){
    const id = path.split('/')[2];
    const arr = loadLS('reservations');
    const idx = arr.findIndex(r => r.id===id);
    if (idx>=0){ arr[idx] = { ...arr[idx], from: body.from||arr[idx].from, to: body.to||arr[idx].to, parkingLotId: body.parkingLotId||arr[idx].parkingLotId }; saveLS('reservations', arr); }
    const r = arr.find(x => x.id===id) || {};
    return { id: r.id, vehicleId: r.vehicleId, parkingLotId: r.parkingLotId, from: r.from, to: r.to };
  }
  if (path.startsWith('/reservations/') && method==='DELETE'){
    const id = path.split('/')[2];
    const arr = loadLS('reservations').filter(r => r.id!==id); saveLS('reservations', arr);
    return { status: 'DELETED' };
  }
  if (path==='/ops/entry' && method==='POST') return 'Entrada registrada para placa '+(body.plate||'');
  if (path==='/ops/exit' && method==='POST') return 'Salida registrada para placa '+(body.plate||'');
  if (path==='/ops/history' && method==='GET'){
    const arr = loadLS('ops_history');
    return arr.map(op => ({ id: op.id, type: op.type, plate: op.plate, createdAt: op.at }));
  }
  if (path==='/ops/history' && method==='DELETE'){
    saveLS('ops_history', []);
    return { status: 'CLEARED' };
  }
  if (path==='/parking' && method==='POST'){
    const p = { id: uid(), name: body.name, capacity: parseInt(body.capacity||'0',10), location: body.location };
    const arr = loadLS('parking'); arr.push(p); saveLS('parking', arr); return { id: p.id, name: p.name, capacity: p.capacity, location: p.location };
  }
  if (path==='/parking' && method==='GET'){
    return loadLS('parking').map(p => ({ id: p.id, name: p.name, capacity: p.capacity, location: p.location }));
  }
  if (path.startsWith('/parking/') && method==='DELETE'){
    const id = path.split('/')[2];
    const arr = loadLS('parking').filter(p => p.id!==id); saveLS('parking', arr);
    return { status: 'DELETED' };
  }
  if (path==='/payments/pay' && method==='POST'){
    const cu = getCurrentUser(); const p = { id: uid(), userId: cu ? cu.id : '', reservationId: body.reservationId, amount: parseFloat(body.amount||'0'), method: body.method||'EFECTIVO', status: 'APPROVED', createdAt: new Date().toISOString() };
    const arr = loadLS('payments'); arr.push(p); saveLS('payments', arr); return { id: p.id, status: p.status };
  }
  if (path==='/payments/history' && method==='GET'){
    const cu = getCurrentUser(); const arr = loadLS('payments').filter(p => !cu || p.userId===cu.id);
    return arr.map(p => ({ id: p.id, reservationId: p.reservationId, amount: p.amount, method: p.method, status: p.status, createdAt: p.createdAt }));
  }
  if (path==='/admin/reports/simple' && method==='GET'){
    const users = loadLS('users'); const vehicles = loadLS('vehicles'); const reservations = loadLS('reservations'); const payments = loadLS('payments');
    return { title: 'Reporte Garage-ID', content: 'Usuarios: '+users.length+', Vehículos: '+vehicles.length+', Reservas: '+reservations.length+', Pagos: '+payments.length };
  }
  return { message: 'OK' };
}
async function apiFetch(path, opts={}){
  if (safeMode) return mockFetch(path, opts);
  try {
    const headers = opts.headers || {};
    const token = getToken();
    if (token) headers['Authorization'] = 'Bearer ' + token;
    headers['Content-Type'] = headers['Content-Type'] || 'application/json';
    const res = await fetch(API + path, { ...opts, headers });
    if (!res.ok) {
      let msg = 'Error';
      try { const j = await res.json(); msg = j.message || JSON.stringify(j); } catch { }
      throw new Error(msg + ' (' + res.status + ')');
    }
    return res.json();
  } catch (e) {
    setSafeMode(true); setApiMode('safe');
    return mockFetch(path, opts);
  }
}

function getApiMode(){ return localStorage.getItem('api_mode')||''; }
function setApiMode(m){ localStorage.setItem('api_mode', m); }
async function detectBackend(){
  for (const base of API_CANDIDATES){
    try {
      await fetch(base + '/auth/login', { method:'POST', headers:{ 'Content-Type':'application/json' }, body:'{}' });
      setApiBase(base);
      setSafeMode(false); setApiMode('real');
      return;
    } catch {}
  }
  setSafeMode(true); setApiMode('safe');
}

// Tema (claro/oscuro)
function getSavedTheme(){ return localStorage.getItem('theme')||''; }
function applyTheme(t){ document.documentElement.setAttribute('data-theme', t==='light' ? 'light' : 'dark'); }
function updateThemeToggleUI(t){
  const btn = document.getElementById('theme-toggle');
  if (btn){
    btn.textContent = '';
    const label = t==='light' ? 'Modo oscuro' : 'Modo claro';
    btn.setAttribute('aria-label', label);
    btn.title = label;
  }
}
function initTheme(){
  let t = getSavedTheme();
  if (!t){ t = (window.matchMedia && window.matchMedia('(prefers-color-scheme: light)').matches) ? 'light' : 'dark'; }
  applyTheme(t);
  updateThemeToggleUI(t);
}
function toggleTheme(){ const cur = document.documentElement.getAttribute('data-theme')||'dark'; const next = cur==='light' ? 'dark' : 'light'; applyTheme(next); localStorage.setItem('theme', next); updateThemeToggleUI(next); }

function initSessionUI(){
  const header = document.querySelector('.nav');
  if (!header) return;
  const right = header.querySelector('.nav-right') || header;
  let container = right.querySelector('.session');
  if (!container){
    container = document.createElement('div');
    container.className = 'session';
    right.appendChild(container);
  }
  container.innerHTML = '';
  const nav = header.querySelector('nav');
  if (isLoggedIn()){
    if (nav){
      const href = location.pathname.split('/').pop().toLowerCase();
      if (href!=='login.html' && href!=='register.html'){
        const links = Array.from(nav.querySelectorAll('a'));
        links.forEach(a => {
          const h = (a.getAttribute('href')||'').toLowerCase();
          if (h==='login.html' || h==='register.html') a.style.display = 'none';
        });
      }
    }
    const u = getCurrentUser();
    const info = document.createElement('span');
    info.textContent = u ? ((u.nombre||u.email||'Sesión activa') + (u.role ? ' (' + u.role + ')' : '')) : 'Sesión activa';
    info.style.marginRight = '8px';
    const btn = document.createElement('button');
    btn.className = 'btn';
    btn.textContent = 'Cerrar sesión';
    btn.onclick = logout;
    container.appendChild(info);
    container.appendChild(btn);
  } else {
    if (nav){
      const links = Array.from(nav.querySelectorAll('a'));
      links.forEach(a => {
        const h = (a.getAttribute('href')||'').toLowerCase();
        if (h==='login.html' || h==='register.html') a.style.display = '';
      });
    }
  }
}

document.addEventListener('DOMContentLoaded', () => {
  const m = getApiMode();
  if (m){ setSafeMode(m!=='real'); } else { detectBackend().then(()=>{}); }
  initTheme();
  const tbtn = document.getElementById('theme-toggle');
  if (tbtn){ tbtn.onclick = toggleTheme; }
  initSessionUI();
});