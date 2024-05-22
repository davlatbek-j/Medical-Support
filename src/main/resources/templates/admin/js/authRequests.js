// authRequests.js

function sendAuthenticatedRequest(url, method, body = null) {
    const token = localStorage.getItem('token');
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`  // Include the token in the Authorization header
        },
        body: body ? JSON.stringify(body) : null
    });
}
