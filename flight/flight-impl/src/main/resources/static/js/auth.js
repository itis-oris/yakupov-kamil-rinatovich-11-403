document.querySelectorAll('.password-toggle').forEach(btn => {
    btn.addEventListener('click', () => {
        const input = document.getElementById(btn.dataset.target);
        input.type = input.type === 'password' ? 'text' : 'password';
        btn.textContent = input.type === 'password' ? '👁' : '🙈';
    });
});

document.addEventListener('keydown', e => {
    if (e.key !== 'Enter') return;
    const loginBtn = document.getElementById('btn-login');
    const regBtn = document.getElementById('btn-register');
    if (loginBtn) loginBtn.click();
    if (regBtn) regBtn.click();
});

const btnLogin = document.getElementById('btn-login');
if (btnLogin) {
    btnLogin.addEventListener('click', async () => {
        clearErrors();

        const email = document.getElementById('login-email').value.trim();
        const password = document.getElementById('login-password').value;

        const errs = [];
        if (!email) errs.push(['login-email', 'Required']);
        if (!password) errs.push(['login-password', 'Required']);
        if (errs.length) {
            showFieldErrors(errs);
            return;
        }

        setBtn(btnLogin, true, 'Signing in…');

        try {
            const res = await fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password}),
            });

            if (!res.ok) {
                const err = await res.json().catch(() => ({}));
                handleApiError(err);
                return;
            }

            window.location.href = '/';

        } catch {
            showBanner('Connection error. Please try again.');
        } finally {
            setBtn(btnLogin, false, 'Sign in');
        }
    });
}

const btnRegister = document.getElementById('btn-register');
if (btnRegister) {
    btnRegister.addEventListener('click', async () => {
        clearErrors();

        const username = document.getElementById('reg-username').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const password = document.getElementById('reg-password').value;
        const password2 = document.getElementById('reg-password2').value;

        const errs = [];
        if (!username) errs.push(['reg-username', 'Required']);
        if (!email) errs.push(['reg-email', 'Required']);
        if (!password) errs.push(['reg-password', 'Required']);
        if (!password2) errs.push(['reg-password2', 'Required']);
        if (password && password2 && password !== password2)
            errs.push(['reg-password2', 'Passwords do not match']);
        if (errs.length) {
            showFieldErrors(errs);
            return;
        }

        setBtn(btnRegister, true, 'Creating account…');

        try {
            const res = await fetch('/api/v1/auth/register', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({username, email, password}),
            });

            if (!res.ok) {
                const err = await res.json().catch(() => ({}));
                handleApiError(err);
                return;
            }

            showSuccess('Account created! Redirecting to sign in…');
            setTimeout(() => {
                window.location.href = '/login';
            }, 1800);

        } catch {
            showBanner('Connection error. Please try again.');
        } finally {
            setBtn(btnRegister, false, 'Create account');
        }
    });
}


const FIELD_MAP = {
    email: 'login-email',
    password: 'login-password',
    username: 'reg-username',
};
const REG_FIELD_MAP = {
    email: 'reg-email',
    password: 'reg-password',
    username: 'reg-username',
};

function handleApiError(err) {
    if (err.errors && Object.keys(err.errors).length) {
        const isRegister = !!document.getElementById('btn-register');
        const map = isRegister ? REG_FIELD_MAP : FIELD_MAP;
        const errs = [];
        for (const [field, messages] of Object.entries(err.errors)) {
            const key = field.split('.').pop();
            if (map[key]) errs.push([map[key], messages[0]]);
        }
        if (errs.length) {
            showFieldErrors(errs);
            return;
        }
    }
    showBanner(err.detail ?? 'Something went wrong. Please try again.');
}

function showFieldErrors(pairs) {
    pairs.forEach(([id, msg]) => {
        const input = document.getElementById(id);
        if (!input) return;
        const field = input.closest('.field');
        field.classList.add('field--error');
        const msgEl = document.createElement('div');
        msgEl.className = 'field__error-msg';
        msgEl.textContent = msg;
        field.appendChild(msgEl);
        input.addEventListener('input', () => {
            field.classList.remove('field--error');
            msgEl.remove();
        }, {once: true});
    });
    document.querySelector('.field--error')
        ?.scrollIntoView({behavior: 'smooth', block: 'center'});
}

function showBanner(msg) {
    const el = document.getElementById('auth-error');
    if (!el) return;
    el.textContent = msg;
    el.classList.remove('hidden');
}

function showSuccess(msg) {
    const el = document.getElementById('auth-success');
    if (!el) return;
    el.textContent = msg;
    el.classList.remove('hidden');
    document.getElementById('auth-error')?.classList.add('hidden');
}

function clearErrors() {
    document.querySelectorAll('.field--error').forEach(f => {
        f.classList.remove('field--error');
        f.querySelector('.field__error-msg')?.remove();
    });
    document.getElementById('auth-error')?.classList.add('hidden');
    document.getElementById('auth-success')?.classList.add('hidden');
}

function setBtn(btn, loading, text) {
    btn.disabled = loading;
    btn.textContent = text;
}
