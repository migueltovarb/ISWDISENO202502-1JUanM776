const regBtn = document.getElementById('register');
if (regBtn) regBtn.onclick = async () => {
  try {
    const payload = {
      nombre: document.getElementById('name').value,
      email: document.getElementById('email').value,
      password: document.getElementById('pass').value,
      role: document.getElementById('role').value
    };
    if(!payload.email || !payload.password) return alert('Email y password son obligatorios');
    const res = await apiFetch('/auth/register', { method:'POST', body: JSON.stringify(payload) });
    setToken(res.token);
    try { const me = await apiFetch('/user/me'); setCurrentUser(me); } catch {}
    document.getElementById('reg-status').innerText = 'Registrado como ' + res.role;
    redirectByRole(res.role);
  } catch (e) { alert(e.message); }
};

const logBtn = document.getElementById('login');
if (logBtn) logBtn.onclick = async () => {
  try {
    const payload = { email: document.getElementById('lemail').value, password: document.getElementById('lpass').value };
    const res = await apiFetch('/auth/login', { method:'POST', body: JSON.stringify(payload) });
    setToken(res.token);
    try { const me = await apiFetch('/user/me'); setCurrentUser(me); } catch {}
    document.getElementById('log-status').innerText = 'Sesión iniciada: ' + res.role;
    redirectByRole(res.role);
  } catch (e) { alert(e.message); }
};

function redirectByRole(role){
  if(role==='ADMIN') location.href = 'admin.html';
  else if(role==='OPERATOR') location.href = 'operator.html';
  else location.href = 'user.html';
}