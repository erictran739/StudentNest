async function login() {
    const body = {
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    }
    try {
        const r = await fetch('/auth/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        const data = await r.json().catch(() => ({}));
        show({status: r.status, data});
    } catch (e) {
        show(String(e));
    }
}

