const oLoginBtn = document.getElementById('o-login');
if (oLoginBtn) oLoginBtn.onclick = async () => {
  try {
    const payload = { email: document.getElementById('o-email').value, password: document.getElementById('o-pass').value };
    const res = await apiFetch('/auth/login', { method:'POST', body: JSON.stringify(payload) });
    if(res.role!=='OPERATOR') return alert('No eres operador'); setToken(res.token); const st = document.getElementById('o-status'); if (st) st.innerText='OK operador';
    const s = document.getElementById('operator-auth'); if (s) s.style.display = 'none';
  } catch (e) { alert(e.message); }
};

document.getElementById('o-entry').onclick = async () => {
  try {
    const plate = document.getElementById('o-plate').value.trim().toUpperCase();
    if(!plate) return alert('Ingresa la placa');
    const reA = /^[A-Z]{3}\d{3}$/;
    const reB = /^[A-Z]{3}\d[A-Z]\d{2}$/;
    if(!reA.test(plate) && !reB.test(plate)) return alert('Placa inválida');
    const res = await apiFetch('/ops/entry', { method:'POST', body: JSON.stringify({ plate }) });
    alert(res);
    try {
      const arr = loadLS('ops_history');
      arr.push({ id: uid(), type: 'ENTRY', plate, at: new Date().toISOString() });
      saveLS('ops_history', arr);
      renderHistory();
    } catch {}
  } catch (e) { alert(e.message); }
};

document.getElementById('o-exit').onclick = async () => {
  try {
    const plate = document.getElementById('o-plate').value.trim().toUpperCase();
    if(!plate) return alert('Ingresa la placa');
    const reA = /^[A-Z]{3}\d{3}$/;
    const reB = /^[A-Z]{3}\d[A-Z]\d{2}$/;
    if(!reA.test(plate) && !reB.test(plate)) return alert('Placa inválida');
    const res = await apiFetch('/ops/exit', { method:'POST', body: JSON.stringify({ plate }) });
    alert(res);
    try {
      const arr = loadLS('ops_history');
      arr.push({ id: uid(), type: 'EXIT', plate, at: new Date().toISOString() });
      saveLS('ops_history', arr);
      renderHistory();
    } catch {}
  } catch (e) { alert(e.message); }
};

document.addEventListener('DOMContentLoaded', () => {
  try {
    if (typeof isLoggedIn === 'function' && isLoggedIn()){
      const s = document.getElementById('operator-auth'); if (s) s.style.display = 'none';
      const cu = getCurrentUser();
      const role = (cu && cu.role ? cu.role : '').toUpperCase();
      const email = (cu && cu.email ? cu.email : '').toLowerCase();
      if (role !== 'OPERATOR' && email !== 'ricardo10@gmail.com'){
        const target = role==='ADMIN' ? 'admin.html' : 'user.html';
        alert('Acceso restringido: este panel es para OPERADOR');
        location.href = target;
        return;
      }
    }
    const strip = document.getElementById('o-session-strip');
    if (strip && typeof isLoggedIn==='function' && isLoggedIn()){
      const u = getCurrentUser();
      const pill = document.createElement('span'); pill.className='session-pill';
      const name = (u && (u.nombre || u.email)) ? (u.nombre || u.email) : 'Sesión activa';
      const role = u && u.role ? u.role : '';
      const av = document.createElement('span'); av.className='avatar'; av.textContent = (name||'O').trim().charAt(0).toUpperCase();
      const label = document.createElement('span'); label.textContent = name;
      pill.appendChild(av); pill.appendChild(label);
      if (role){ const r = document.createElement('span'); r.className='role'; r.textContent = role; pill.appendChild(r); }
      strip.innerHTML=''; strip.appendChild(pill);
    }
    const inp = document.getElementById('o-plate'); if (inp){ inp.addEventListener('input', () => { inp.value = inp.value.toUpperCase(); }); }
    renderHistory();
    refreshStats();
  } catch {}
});

function renderHistory(){
  try {
    const ul = document.getElementById('o-history-ul'); if (!ul) return;
    apiFetch('/ops/history').then(arr => {
      ul.innerHTML = '';
      (arr||[]).slice().reverse().forEach(op => {
        const li = document.createElement('li');
        const tag = document.createElement('span'); tag.className = 'tag ' + (op.type==='ENTRY'?'tag-entry':'tag-exit'); tag.textContent = op.type==='ENTRY'?'Entrada':'Salida';
        const when = new Date(op.createdAt||op.at);
        const dd = String(when.getDate()).padStart(2,'0');
        const mm = String(when.getMonth()+1).padStart(2,'0');
        const yyyy = when.getFullYear();
        const hh = String(when.getHours()).padStart(2,'0');
        const mi = String(when.getMinutes()).padStart(2,'0');
        const txt = document.createElement('span'); txt.textContent = ` ${op.plate} ${dd}/${mm}/${yyyy} ${hh}:${mi}`;
        li.appendChild(tag); li.appendChild(txt);
        ul.appendChild(li);
      });
    }).catch(()=>{});
  } catch {}
}

document.getElementById('o-history-refresh').onclick = () => { try { renderHistory(); } catch {} };
document.getElementById('o-history-clear').onclick = async () => { try { await apiFetch('/ops/history', { method: 'DELETE' }); renderHistory(); } catch {} };
document.getElementById('o-stats-refresh').onclick = () => { try { refreshStats(); } catch {} };

function refreshStats(){
  apiFetch('/ops/history').then(arr => {
    const today = new Date(); const y=today.getFullYear(), m=today.getMonth(), d=today.getDate();
    const isToday = (iso) => { const t=new Date(iso); return t.getFullYear()===y && t.getMonth()===m && t.getDate()===d; };
    const list = (arr||[]).filter(op => isToday(op.createdAt||op.at));
    const entries = list.filter(op => op.type==='ENTRY').length;
    const exits = list.filter(op => op.type==='EXIT').length;
    const last = list.slice().sort((a,b)=>new Date(b.createdAt||b.at)-new Date(a.createdAt||a.at))[0];
    const fmt = (iso) => { const t=new Date(iso); const hh=String(t.getHours()).padStart(2,'0'); const mm=String(t.getMinutes()).padStart(2,'0'); return `${hh}:${mm}`; };
    const elE = document.getElementById('o-stat-entry'); if (elE) elE.textContent = String(entries);
    const elX = document.getElementById('o-stat-exit'); if (elX) elX.textContent = String(exits);
    const elL = document.getElementById('o-stat-last'); if (elL) elL.textContent = last ? `${last.type==='ENTRY'?'Entrada':'Salida'} ${last.plate} ${fmt(last.createdAt||last.at)}` : '—';
  }).catch(()=>{});
}