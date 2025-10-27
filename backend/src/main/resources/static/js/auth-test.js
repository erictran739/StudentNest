const OUT = document.getElementById('out');

function show(res) {
    OUT.textContent = typeof res === 'string' ? res : JSON.stringify(res, null, 2);
}

function showOut(status) {
    OUT.textContent = status;
}

async function register() {
    const body = {
        firstName: document.getElementById('fn').value,
        lastName: document.getElementById('ln').value,
        email: document.getElementById('em').value,
        password: document.getElementById('pw').value
    };
    try {
        const r = await fetch('/auth/register', {
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

async function login() {
    const body = {
        email: document.getElementById('lem').value,
        password: document.getElementById('lpw').value
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

async function listUsers() {
    try {
        const r = await fetch('/api/users');
        const data = await r.json();
        show({status: r.status, data});
    } catch (e) {
        show(String(e));
    }
}

async function getCourse() {
    try {
        const id = document.getElementById('get_course_id').value

        const r = await fetch("/api/courses/get/" + id)
        const data = await r.json()
        show({status: r.status, data})
    } catch (e) {
        show(String(e));
    }
}

async function createCourse() {
    const url = "/api/courses/create";
    const body = {
        name: document.getElementById('course_name').value,
        description: document.getElementById('course_description').value,
        department: document.getElementById('course_department').value,
        credits: document.getElementById('course_credits').value
    }

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        const data = await response.json();

        show({status: response.status, data});
    } catch (e) {
        show(String(e))
    }
}

async function deleteUser() {
    const id = document.getElementById('delete_id').value;

    const url = '/api/users/' + id;
    const body = {method: 'DELETE'};

    try {
        const r = await fetch(url, body);
        show({status: r.status});
    } catch (e) {
        show(String(e));
    }
}

async function addSectionToCourse(){
    const id = document.getElementById('section_add_course_id').value;
    const url = '/api/courses/add/section';
    const body = {
        courseID: id
    }
    try {
        const r = await fetch(url,{
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        const data = await r.json();
        show({status: r.status, data});
    } catch (e) {
        show(String(e));
    }
}

async function listSections(){
    try {
        const r = await fetch('/api/sections');
        const data = await r.json();
        show({status: r.status, data});
    } catch (e) {
        show(String(e));
    }
}

async function getSectionOfCourse(){
    const course_id = document.getElementById('get_section_course_id').value;
    const section_id = document.getElementById('get_section_section_id').value;
    const url = '/api/courses/' + course_id + '/section/' + section_id;

    try {
        const r = await fetch(url,{
            method: 'GET',
        });
        const data = await r.json();
        show({status: r.status, data});
    } catch (e) {
        show(String(e));
    }
}

async function updateUser() {
    const body = {}
}