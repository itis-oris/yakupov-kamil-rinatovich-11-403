document.getElementById('btn-logout')?.addEventListener('click', async () => {
    await fetch('/api/v1/auth/logout', {method: 'POST'});
    window.location.href = '/login';
});
