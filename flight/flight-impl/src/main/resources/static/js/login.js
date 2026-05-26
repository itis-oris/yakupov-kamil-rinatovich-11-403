document.getElementById('btn-login').addEventListener('click', doLogin);
document.addEventListener('keydown', e => {
    if (e.key === 'Enter') doLogin();
});

async function doLogin() {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const errEl = document.getElementById('auth-error');
    errEl.style.display = 'none';

    const btn = document.getElementById('btn-login');
    btn.disabled = true;
    btn.textContent = 'Signing in…';

    try {
        const res = await fetch('/api/v1/auth/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email, password}),
        });

        if (res.ok) {
            window.location.href = '/';
            return;
        }

        const err = await res.json().catch(() => ({}));
        errEl.textContent = err.detail ?? 'Invalid credentials. Please try again.';
        errEl.style.display = 'block';
    } catch {
        errEl.textContent = 'Connection error. Please try again.';
        errEl.style.display = 'block';
    } finally {
        btn.disabled = false;
        btn.textContent = 'Sign in';
    }
}

function togglePassword(id, btn) {
    const input = document.getElementById(id);
    input.type = input.type === 'password' ? 'text' : 'password';
    btn.textContent = input.type === 'password' ? '👁' : '🙈';
}
