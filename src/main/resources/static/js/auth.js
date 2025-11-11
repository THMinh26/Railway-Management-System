// NOTE: Config được load từ config.js
// Thay đổi CONFIG.API_BASE_URL trong file config.js

// Helper Functions
function showMessage(elementId, message, type) {
    const messageElement = document.getElementById(elementId);
    if (messageElement) {
        messageElement.textContent = message;
        messageElement.className = `message ${type}`;
        messageElement.style.display = 'block';
    }
}

function saveCurrentUser(userData) {
    localStorage.setItem('currentUser', JSON.stringify(userData));
}

function getCurrentUser() {
    const userData = localStorage.getItem('currentUser');
    return userData ? JSON.parse(userData) : null;
}

function logout() {
    localStorage.removeItem('currentUser');
    window.location.href = 'login.html';
}

// Login Form Handler
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        
        try {
            const response = await fetch(getApiUrl(CONFIG.ENDPOINTS.LOGIN), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });
            
            const data = await response.json();
            
            if (response.ok) {
                showMessage('message', 'Login successful! Redirecting...', 'success');
                saveCurrentUser(data);
                
                // Redirect based on role
                setTimeout(() => {
                    if (data.isAdmin) {
                        window.location.href = 'admin.html';
                    } else {
                        window.location.href = 'index.html';
                    }
                }, 1500);
            } else {
                showMessage('message', data.error || 'Login failed', 'error');
            }
        } catch (error) {
            showMessage('message', 'Error connecting to server', 'error');
            console.error('Login error:', error);
        }
    });
}

// Register Form Handler
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const fullName = document.getElementById('fullName').value;
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const phoneNumber = document.getElementById('phoneNumber').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        // Validate passwords match
        if (password !== confirmPassword) {
            showMessage('message', 'Passwords do not match', 'error');
            return;
        }
        
        try {
            if (!API_ENABLED) {
                // Demo register: pretend registration succeeded
                showMessage('message', 'Demo registration successful! Redirecting to login...', 'success');
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 800);
                return;
            }

            const response = await fetch(getApiUrl(CONFIG.ENDPOINTS.REGISTER), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    fullName,
                    username,
                    email,
                    phoneNumber,
                    password
                })
            });
            
            const data = await response.json();
            
            if (response.ok) {
                showMessage('message', 'Registration successful! Redirecting to login...', 'success');
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            } else {
                showMessage('message', data.error || 'Registration failed', 'error');
            }
        } catch (error) {
            showMessage('message', 'Error connecting to server', 'error');
            console.error('Registration error:', error);
        }
    });
}
