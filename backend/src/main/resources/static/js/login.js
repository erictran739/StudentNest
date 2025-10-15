async function login() {
    const OUT = document.getElementById('out');

    function show(status, message) {
        // OUT.textContent = typeof res === 'string' ? res : JSON.stringify(res, null, 2);
        OUT.textContent = status + ": " + message;
    }

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
        show(r.status, data.message);
    } catch (e) {
        show(String(e));
    }
}

