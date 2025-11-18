const aLoginBtn = document.getElementById('a-login');
if (aLoginBtn) aLoginBtn.onclick = async () => {
  try {
    const payload = { email: document.getElementById('a-email').value, password: document.getElementById('a-pass').value };
    const res = await apiFetch('/auth/login', { method:'POST', body: JSON.stringify(payload) });
    if(res.role!=='ADMIN') return alert('No eres admin'); setToken(res.token); const st = document.getElementById('a-status'); if (st) st.innerText='OK admin';
    const s = document.getElementById('admin-auth'); if (s) s.style.display = 'none';
  } catch (e) { alert(e.message); }
};

document.getElementById('p-create').onclick = async () => {
  try {
    const body = { name: document.getElementById('p-name').value, capacity: document.getElementById('p-capacity').value, location: document.getElementById('p-location').value };
    const p = await apiFetch('/parking', { method:'POST', body: JSON.stringify(body) });
    alert('Parqueadero creado ' + p.id);
  } catch (e) { alert(e.message); }
};

document.getElementById('p-list').onclick = async () => {
  try {
    const list = await apiFetch('/parking');
    const ul = document.getElementById('p-list-ul'); ul.innerHTML='';
    const pc = document.getElementById('p-count'); if (pc) pc.textContent = 'Parqueaderos: ' + (list?.length||0);
    list.forEach(p => {
      const li=document.createElement('li'); li.textContent=`${p.name} cap=${p.capacity} loc=${p.location}`;
      const del=document.createElement('button'); del.className='btn'; del.textContent='Eliminar'; del.style.marginLeft='8px';
      del.onclick = async () => { try { await apiFetch('/parking/'+p.id, { method:'DELETE' }); alert('Parqueadero eliminado'); document.getElementById('p-list').click(); } catch(e){ alert(e.message); } };
      li.appendChild(del); ul.appendChild(li);
    });
  } catch (e) { alert(e.message); }
};

document.getElementById('r-admin').onclick = async () => {
  try {
    const r = await apiFetch('/admin/reports/simple');
    const pre = document.getElementById('r-admin-pre'); pre.textContent = `${r.title}\n${r.content}`;
    try {
      const counts = parseCounts(r.content||'');
      renderPieChart('r-admin-chart', counts);
      renderLegend('r-admin-legend', counts);
      try { refreshAdminStatsFromCounts(counts); } catch {}
    } catch {}
  } catch (e) { alert(e.message); }
};

document.addEventListener('DOMContentLoaded', () => {
  try {
    if (typeof isLoggedIn === 'function' && isLoggedIn()){
      const s = document.getElementById('admin-auth'); if (s) s.style.display = 'none';
    }
    try { refreshAdminStats(); } catch {}
  } catch {}
});

function parseCounts(text){
  const pick = (labelVariants) => {
    for (const label of labelVariants){
      const re = new RegExp(label.replace(/[.*+?^${}()|[\]\\]/g, '\\$&') + '\\s*:\\s*(\\d+)', 'i');
      const m = re.exec(text); if (m) return parseInt(m[1],10);
    }
    return 0;
  };
  return {
    Usuarios: pick(['Usuarios']),
    Vehiculos: pick(['Vehículos','Vehiculos']),
    Reservas: pick(['Reservas']),
    Pagos: pick(['Pagos','Pagos(admin)'])
  };
}

function renderPieChart(canvasId, counts){
  const canvas = document.getElementById(canvasId); if (!canvas || !canvas.getContext) return;
  const ctx = canvas.getContext('2d');
  const entries = Object.entries(counts).filter(([,v]) => v>0);
  ctx.clearRect(0,0,canvas.width,canvas.height);
  if (entries.length===0){
    ctx.fillStyle = '#cbd5e1'; ctx.font = '14px system-ui'; ctx.fillText('Sin datos para graficar', 10, 20); return;
  }
  const total = entries.reduce((s,[,v])=>s+v,0);
  const colors = ['#60a5fa','#22d3ee','#d4af37','#93c5fd','#a7f3d0'];
  let start = -Math.PI/2; const cx=120, cy=120, r=100, innerR=56;
  entries.forEach(([label,val],i) => {
    const angle = (val/total) * Math.PI*2;
    const end = start + angle;
    ctx.beginPath();
    ctx.moveTo(cx,cy);
    ctx.arc(cx,cy,r,start,end);
    ctx.closePath();
    ctx.fillStyle = colors[i % colors.length];
    ctx.fill();
    const mid = start + angle/2;
    const percent = Math.round((val/total)*100);
    if (percent >= 5){
      const lx = cx + Math.cos(mid) * (r - (r - innerR)/2);
      const ly = cy + Math.sin(mid) * (r - (r - innerR)/2);
      ctx.fillStyle = '#e2e8f0';
      ctx.font = '12px system-ui';
      ctx.textAlign = 'center'; ctx.textBaseline = 'middle';
      ctx.fillText(percent + '%', lx, ly);
    }
    start = end;
  });
  ctx.beginPath(); ctx.fillStyle = '#0b1220'; ctx.arc(cx,cy,innerR,0,Math.PI*2); ctx.fill();
}

function renderLegend(elId, counts){
  const el = document.getElementById(elId); if (!el) return;
  const entries = Object.entries(counts).filter(([,v]) => v>0);
  const colors = ['#60a5fa','#22d3ee','#d4af37','#93c5fd','#a7f3d0'];
  el.innerHTML = '';
  entries.forEach(([label,val],i)=>{
    const item = document.createElement('div'); item.className = 'legend-item';
    const sw = document.createElement('span'); sw.className='swatch'; sw.style.backgroundColor=colors[i%colors.length];
    const txt = document.createElement('span'); txt.textContent = `${label}: ${val}`;
    item.appendChild(sw); item.appendChild(txt); el.appendChild(item);
  });
}

function refreshAdminStats(){
  apiFetch('/admin/reports/simple').then(r => {
    const counts = parseCounts(r.content||'');
    refreshAdminStatsFromCounts(counts);
  }).catch(()=>{});
}

function refreshAdminStatsFromCounts(counts){
  const u = document.getElementById('a-stat-users'); if (u) u.textContent = String(counts.Usuarios||0);
  const v = document.getElementById('a-stat-vehicles'); if (v) v.textContent = String(counts.Vehiculos||0);
  const r = document.getElementById('a-stat-reservations'); if (r) r.textContent = String(counts.Reservas||0);
  const p = document.getElementById('a-stat-payments'); if (p) p.textContent = String(counts.Pagos||0);
}

const aStatsBtn = document.getElementById('a-stats-refresh'); if (aStatsBtn) aStatsBtn.onclick = () => { try { refreshAdminStats(); } catch {} };