function showEdit() {
    document.getElementById('view-mode').style.display = 'none';
    document.getElementById('edit-mode').style.display = 'block';
    document.getElementById('new-username').focus();
}

function hideEdit() {
    document.getElementById('view-mode').style.display = 'block';
    document.getElementById('edit-mode').style.display = 'none';
    document.getElementById('edit-error').style.display = 'none';
    document.getElementById('edit-success').style.display = 'none';
}

async function saveUsername() {
    const username = document.getElementById('new-username').value.trim();
    const errEl = document.getElementById('edit-error');
    const okEl = document.getElementById('edit-success');
    errEl.style.display = 'none';
    okEl.style.display = 'none';

    const btn = document.getElementById('btn-save-username');
    btn.disabled = true;
    btn.textContent = 'Saving…';

    try {
        const res = await apiFetch('api/v1/account/me/username', {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username}),
        });

        if (res.ok) {
            okEl.textContent = 'Username updated successfully.';
            okEl.style.display = 'block';
            setTimeout(() => {
                hideEdit();
                location.reload();
            }, 1200);
            return;
        }

        const err = await res.json().catch(() => ({}));
        errEl.textContent = err.detail ?? 'Failed to update username.';
        errEl.style.display = 'block';
    } catch {
        errEl.textContent = 'Connection error.';
        errEl.style.display = 'block';
    } finally {
        btn.disabled = false;
        btn.textContent = 'Save';
    }
}
