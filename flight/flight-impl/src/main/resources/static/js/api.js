async function apiFetch(url, options = {}) {
    const res = await fetch(url, options);

    if (res.status === 401) {
        if (res.headers.get('X-Token-Refreshed') === 'true') {
            return fetch(url, options);
        }
        window.location.href = '/login';
        return new Response(null, {status: 401});
    }
    return res;
}
