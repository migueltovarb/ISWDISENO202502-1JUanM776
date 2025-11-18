const uRegBtn = document.getElementById('u-register');
if (uRegBtn) uRegBtn.onclick = async () => {
  try {
    const payload = {
      nombre: document.getElementById('u-name').value,
      email: document.getElementById('u-email').value,
      password: document.getElementById('u-pass').value,
      role: document.getElementById('u-role').value
    };
    if(!payload.email || !payload.password) return alert('Email y password son obligatorios');
    const res = await apiFetch('/auth/register', { method:'POST', body: JSON.stringify(payload) });
    setToken(res.token); const st = document.getElementById('u-status'); if (st) st.innerText = 'Registrado como ' + res.role;
    const s = document.getElementById('auth-section'); if (s) s.style.display = 'none';
  } catch (e) { alert(e.message); }
};

const uLoginBtn = document.getElementById('u-login');
if (uLoginBtn) uLoginBtn.onclick = async () => {
  try {
    const payload = { email: document.getElementById('u-email').value, password: document.getElementById('u-pass').value };
    const res = await apiFetch('/auth/login', { method:'POST', body: JSON.stringify(payload) });
    setToken(res.token); const st = document.getElementById('u-status'); if (st) st.innerText = 'Sesión iniciada: ' + res.role;
    const s = document.getElementById('auth-section'); if (s) s.style.display = 'none';
  } catch (e) { alert(e.message); }
};

document.getElementById('v-save').onclick = async () => {
  try {
  const body = {
    plate: document.getElementById('v-plate').value,
    brand: document.getElementById('v-brand').value,
    model: document.getElementById('v-model').value,
    type: document.getElementById('v-type').value
    ,color: document.getElementById('v-color').value
  };
    const v = await apiFetch('/vehicles', { method:'POST', body: JSON.stringify(body) });
    alert('Vehículo creado: ' + v.id);
    document.getElementById('v-plate').value = '';
    document.getElementById('v-brand').value = '';
    document.getElementById('v-model').value = '';
    document.getElementById('v-type').value = 'CARRO';
    document.getElementById('v-list').click();
    document.getElementById('v-plate').focus();
  } catch (e) { alert(e.message); }
};

document.getElementById('v-list').onclick = async () => {
  try {
    const list = await apiFetch('/vehicles/my');
    const ul = document.getElementById('v-list-ul'); ul.innerHTML='';
    list.forEach(v => {
      const li=document.createElement('li'); li.textContent=`${v.plate} (${v.brand} ${v.model}) ${v.type?'- '+v.type:''} ${v.color?'- '+v.color:''}`;
      const edit=document.createElement('button'); edit.className='btn'; edit.textContent='Editar'; edit.style.marginLeft='8px';
      edit.onclick = async () => {
        try {
          const np = prompt('Nueva placa:', v.plate);
          const nb = prompt('Nueva marca:', v.brand);
          const nm = prompt('Nuevo modelo:', v.model);
          const nt = prompt('Nuevo tipo (CARRO/MOTO):', v.type||'CARRO');
          const nc = prompt('Nuevo color:', v.color||'');
          await apiFetch('/vehicles/'+v.id, { method:'PUT', body: JSON.stringify({ plate: np, brand: nb, model: nm, type: nt, color: nc }) });
          alert('Vehículo actualizado'); document.getElementById('v-list').click();
        } catch(e){ alert(e.message); }
      };
      const del=document.createElement('button'); del.className='btn'; del.textContent='Eliminar'; del.style.marginLeft='6px';
      del.onclick = async () => { try { await apiFetch('/vehicles/'+v.id, { method:'DELETE' }); alert('Vehículo eliminado'); document.getElementById('v-list').click(); } catch(e){ alert(e.message); } };
      li.appendChild(edit); li.appendChild(del); ul.appendChild(li);
    });
  } catch (e) { alert(e.message); }
};

document.getElementById('r-create').onclick = async () => {
  try {
    const dateStr = document.getElementById('r-date').value;
    const timeFrom = document.getElementById('r-time-from').value;
    const timeTo = document.getElementById('r-time-to').value;
    const vid = document.getElementById('r-vehicleId').value;
    const pid = document.getElementById('r-parkingLotId').value;
    if (!dateStr || !timeFrom || !timeTo) return alert('Selecciona fecha (DD/MM/YYYY) y horas');
    const mDate = /^\s*(\d{2})\/(\d{2})\/(\d{4})\s*$/.exec(dateStr);
    if (!mDate) return alert('Formato de fecha inválido (usa DD/MM/YYYY)');
    const dd = parseInt(mDate[1],10); const mm = parseInt(mDate[2],10); const yyyy = parseInt(mDate[3],10);
    const mFrom = /^(\d{2}):(\d{2})$/.exec(timeFrom);
    const mTo = /^(\d{2}):(\d{2})$/.exec(timeTo);
    if (!mFrom || !mTo) return alert('Formato de hora inválido (usa HH:MM)');
    const hf = parseInt(mFrom[1],10), mf = parseInt(mFrom[2],10);
    const ht = parseInt(mTo[1],10), mt = parseInt(mTo[2],10);
    const fromD = new Date(yyyy, mm-1, dd, hf, mf, 0, 0);
    const toD = new Date(yyyy, mm-1, dd, ht, mt, 0, 0);
    if (fromD.getFullYear()!==yyyy || fromD.getMonth()!==mm-1 || fromD.getDate()!==dd) return alert('Fecha inválida');
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const selDay = new Date(yyyy, mm-1, dd);
    if (selDay < today) return alert('No puedes seleccionar días anteriores al actual');
    if (selDay.getTime()===today.getTime() && fromD < now) return alert('La hora de inicio no puede ser anterior a la actual');
    if (toD <= fromD) return alert('La hora de fin debe ser posterior al inicio');
    const pad = n => String(n).padStart(2,'0');
    const toIsoLocal = (d) => `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
    const body = { vehicleId: vid, parkingLotId: pid, from: toIsoLocal(fromD), to: toIsoLocal(toD) };
    const r = await apiFetch('/reservations', { method:'POST', body: JSON.stringify(body) });
    alert('Reserva creada: ' + r.id);
  } catch (e) { alert(e.message); }
};

document.addEventListener('DOMContentLoaded', () => {
  try {
    if (typeof isLoggedIn === 'function' && isLoggedIn()){
      const u = getCurrentUser();
      const strip = document.getElementById('u-session-strip');
      if (strip){
        const pill = document.createElement('span'); pill.className='session-pill';
        const name = (u && (u.nombre || u.email)) ? (u.nombre || u.email) : 'Sesión activa';
        const role = u && u.role ? u.role : '';
        const av = document.createElement('span'); av.className='avatar'; av.textContent = (name||'U').trim().charAt(0).toUpperCase();
        const label = document.createElement('span'); label.textContent = name;
        pill.appendChild(av);
        pill.appendChild(label);
        if (role){ const r = document.createElement('span'); r.className='role'; r.textContent = role; pill.appendChild(r); }
        strip.innerHTML=''; strip.appendChild(pill);
      }
    }
    try { refreshUserStats(); } catch {}
  } catch {}
});

document.getElementById('r-list').onclick = async () => {
  try {
    const list = await apiFetch('/reservations/my');
    const ul = document.getElementById('r-list-ul'); ul.innerHTML='';
    list.forEach(r => {
      const li=document.createElement('li'); li.textContent=`${r.parkingLotId} de ${r.from} a ${r.to}`;
      const edit=document.createElement('button'); edit.className='btn'; edit.textContent='Editar'; edit.style.marginLeft='8px';
      edit.onclick = async () => {
        try {
          const nf = prompt('Nueva fecha desde (YYYY-MM-DDTHH:MM):', r.from);
          const nt = prompt('Nueva fecha hasta (YYYY-MM-DDTHH:MM):', r.to);
          if(!nf || !nt) return;
          await apiFetch('/reservations/'+r.id, { method:'PUT', body: JSON.stringify({ from: nf, to: nt }) });
          alert('Reserva actualizada');
          document.getElementById('r-list').click();
        } catch(e){ alert(e.message); }
      };
      const del=document.createElement('button'); del.className='btn'; del.textContent='Eliminar'; del.style.marginLeft='6px';
      del.onclick = async () => {
        try { await apiFetch('/reservations/'+r.id, { method:'DELETE' }); alert('Reserva eliminada'); document.getElementById('r-list').click(); } catch(e){ alert(e.message); }
      };
      li.appendChild(edit); li.appendChild(del); ul.appendChild(li);
    });
  } catch (e) { alert(e.message); }
};

document.addEventListener('DOMContentLoaded', () => {
  try {
    if (typeof isLoggedIn === 'function' && isLoggedIn()) {
      const s = document.getElementById('auth-section');
      if (s) s.style.display = 'none';
    }
    const d=new Date(); const dd=String(d.getDate()).padStart(2,'0'); const mm=String(d.getMonth()+1).padStart(2,'0'); const yyyy=d.getFullYear();
    const dateEl = document.getElementById('r-date'); if (dateEl && !dateEl.value) dateEl.value = `${dd}/${mm}/${yyyy}`;
    const round = (val, step) => { const p = Math.floor(val/step); return String(p*step).padStart(2,'0'); };
    const h=String(d.getHours()).padStart(2,'0'); const m=round(d.getMinutes(), 5);
    const tf = document.getElementById('r-time-from'); const tt = document.getElementById('r-time-to');
    if (tf && !tf.value) tf.value = `${h}:${m}`;
    const later=new Date(d.getTime()+60*60*1000); const lh=String(later.getHours()).padStart(2,'0'); const lm=round(later.getMinutes(), 5);
    if (tt && !tt.value) tt.value = `${lh}:${lm}`;
  } catch {}
});

document.getElementById('p-pay').onclick = async () => {
  try {
    const body = { reservationId: document.getElementById('p-reservationId').value, amount: parseFloat(document.getElementById('p-amount').value||'0'), method: document.getElementById('p-method').value };
    const res = await apiFetch('/payments/pay', { method:'POST', body: JSON.stringify(body) });
    const pid = res.id || res.paymentId;
    alert('Pago '+res.status+' ID '+pid);
  } catch (e) { alert(e.message); }
};

document.getElementById('p-history').onclick = async () => {
  try {
    const list = await apiFetch('/payments/history');
    const ul = document.getElementById('p-list-ul'); ul.innerHTML='';
    list.forEach(p => { const li=document.createElement('li'); li.textContent=`${p.createdAt} ${p.method} $${p.amount} (${p.status}) Reserva ${p.reservationId}`; ul.appendChild(li); });
  } catch (e) { alert(e.message); }
};

document.getElementById('u-stats-refresh').onclick = () => { try { refreshUserStats(); } catch {} };

async function refreshUserStats(){
  try {
    const [vv, rr, pp] = await Promise.all([
      apiFetch('/vehicles/my'),
      apiFetch('/reservations/my'),
      apiFetch('/payments/history')
    ]);
    const vEl = document.getElementById('u-stat-vehicles'); if (vEl) vEl.textContent = String((vv||[]).length);
    const rEl = document.getElementById('u-stat-reservations'); if (rEl) rEl.textContent = String((rr||[]).length);
    const last = (pp||[]).slice().sort((a,b)=>new Date(b.createdAt)-new Date(a.createdAt))[0];
    const lEl = document.getElementById('u-stat-lastpay');
    if (lEl) lEl.textContent = last ? `${last.method} $${last.amount} ${new Date(last.createdAt).toLocaleDateString()}` : '—';
  } catch {}
}