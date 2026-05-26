document.getElementById('btn-register').addEventListener('click', doRegister);

const FIELD_MAP = {username: 'username', email: 'email', password: 'password'};

async function doRegister() {
    clearErrors();

    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    const btn = document.getElementById('btn-register');
    btn.disabled = true;
    btn.textContent = 'Creating account…';

    try {
        const res = await fetch('/api/v1/auth/register', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username, email, password}),
        });

        if (res.ok) {
            const loginRes = await fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password}),
            });
            window.location.href = loginRes.ok ? '/' : '/login';
            return;
        }

        const err = await res.json().catch(() => ({}));

        if (err.errors && Object.keys(err.errors).length) {
            let first = null;
            for (const [field, messages] of Object.entries(err.errors)) {
                const inputId = FIELD_MAP[field];
                if (inputId) {
                    markFieldError(inputId, messages[0]);
                    if (!first) first = document.getElementById('field-' + inputId);
                }
            }
            if (first) first.scrollIntoView({behavior: 'smooth', block: 'center'});
        } else {
            showBannerError(err.detail ?? 'Registration failed. Please try again.');
        }
    } catch {
        showBannerError('Connection error. Please try again.');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Create account';
    }
}

function markFieldError(inputId, message) {
    const input = document.getElementById(inputId);
    if (!input) return;
    const fieldEl = input.closest('.field') ?? input.parentElement;
    fieldEl.classList.add('field--error');
    let msgEl = fieldEl.querySelector('.field__error-msg');
    if (!msgEl) {
        msgEl = document.createElement('div');
        msgEl.className = 'field__error-msg';
        fieldEl.appendChild(msgEl);
    }
    msgEl.textContent = message;
    input.addEventListener('input', () => {
        fieldEl.classList.remove('field--error');
        fieldEl.querySelector('.field__error-msg')?.remove();
    }, {once: true});
}

function clearErrors() {
    document.querySelectorAll('.field--error').forEach(el => {
        el.classList.remove('field--error');
        el.querySelector('.field__error-msg')?.remove();
    });
    document.getElementById('auth-error').style.display = 'none';
    document.getElementById('auth-success').style.display = 'none';
}

function showBannerError(msg) {
    const el = document.getElementById('auth-error');
    el.textContent = msg;
    el.style.display = 'block';
}

function togglePassword(id, btn) {
    const input = document.getElementById(id);
    input.type = input.type === 'password' ? 'text' : 'password';
    btn.textContent = input.type === 'password' ? '👁' : '🙈';
}
