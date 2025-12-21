// Authentication handling
document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.getElementById('loginForm');
  const registerForm = document.getElementById('registerForm');
  const messageDiv = document.getElementById('message');

  // Show message function
  function showMessage(message, type) {
    if (messageDiv) {
      messageDiv.textContent = message;
      messageDiv.className = `message ${type}`;
      messageDiv.style.display = 'block';

      if (type === 'success' || type === 'info') {
        setTimeout(() => {
          messageDiv.style.display = 'none';
        }, 5000);
      }
    }
  }

  // Login Form Handler -> POST /api/auth/login, then redirect to index.html
  if (loginForm) {
    loginForm.addEventListener('submit', async function (e) {
      e.preventDefault();

      const username = document.getElementById('username').value.trim();
      const password = document.getElementById('password').value;

      if (!username || !password) {
        showMessage('Please fill in all fields', 'error');
        return;
      }

      // Prefer CONFIG if present, otherwise default to backend /api/auth/login
      const loginEndpoint = 'http://localhost:8081/api/auth/login';

      try {
        showMessage('Logging in...', 'info');

        const response = await fetch(loginEndpoint, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username, password }),
        });

        const data = await response.json().catch(() => ({}));

        if (!response.ok) {
          const errMsg =
            data.error || data.message || 'Invalid username or password';
          showMessage(errMsg, 'error');
          return;
        }

        // Normalize fields from LoginResponseDTO
        const userData = {
          userId: data.userId,
          username: data.username,
          fullname: data.fullName || data.fullname,
          email: data.email,
          phone: data.phone,
          isAdmin: String(data.role || '').toUpperCase() === 'ADMIN',
        };

        localStorage.setItem('currentUser', JSON.stringify(userData));
        showMessage(
          data.message || 'Login successful! Redirecting...',
          'success'
        );

        // Redirect on success
        setTimeout(() => {
          window.location.href = 'index.html';
        }, 800);
      } catch (error) {
        console.error('Login error:', error);
        showMessage('Network error. Please try again.', 'error');
      }
    });
  }

  // ...existing code...
  // Register Form Handler (unchanged)
  if (registerForm) {
    registerForm.addEventListener('submit', async function (e) {
      e.preventDefault();

      const username = document.getElementById('username').value.trim();
      const fullname = document.getElementById('fullname').value.trim();
      const email = document.getElementById('email').value.trim();
      const phone = document.getElementById('phone').value.trim();
      const password = document.getElementById('password').value;
      const confirmPassword = document.getElementById('confirmPassword').value;

      if (
        !username ||
        !fullname ||
        !email ||
        !phone ||
        !password ||
        !confirmPassword
      ) {
        showMessage('Please fill in all fields', 'error');
        return;
      }
      if (!/^[a-zA-Z0-9_]{3,15}$/.test(username)) {
        showMessage(
          'Username must be 3-15 characters (letters, numbers, underscore only)',
          'error'
        );
        return;
      }
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        showMessage('Please enter a valid email address', 'error');
        return;
      }
      if (!/^\d{10,15}$/.test(phone)) {
        showMessage('Phone number must be 10-15 digits', 'error');
        return;
      }
      if (password.length < 6) {
        showMessage('Password must be at least 6 characters', 'error');
        return;
      }
      if (password !== confirmPassword) {
        showMessage('Passwords do not match', 'error');
        return;
      }

      try {
        showMessage('Creating account...', 'info');

        const registerEndpoint =
          typeof CONFIG !== 'undefined' &&
          CONFIG?.API_BASE_URL &&
          CONFIG?.ENDPOINTS?.REGISTER
            ? `${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.REGISTER}`
            : '/api/auth/register';

        const response = await fetch(registerEndpoint, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username, fullname, email, phone, password }),
        });

        const data = await response.json();

        if (response.ok) {
          showMessage(
            data.message || 'Registration successful! Redirecting to login...',
            'success'
          );
          registerForm.reset();
          setTimeout(() => {
            window.location.href = 'login.html';
          }, 2000);
        } else {
          showMessage(
            data.error || 'Registration failed. Please try again.',
            'error'
          );
        }
      } catch (error) {
        console.error('Registration error:', error);
        showMessage(
          'Network error. Please check your connection and try again.',
          'error'
        );
      }
    });
  }
});

// Utilities
function checkAuth() {
  const currentUser = localStorage.getItem('currentUser');
  if (currentUser) return JSON.parse(currentUser);
  return null;
}

function logout() {
  localStorage.removeItem('currentUser');
  window.location.href = 'login.html';
}

function requireAuth() {
  const user = checkAuth();
  if (!user) {
    window.location.href = 'login.html';
    return null;
  }
  return user;
}

function requireAdmin() {
  const user = requireAuth();
  if (user && !user.isAdmin) {
    alert('Access denied. Admin privileges required.');
    window.location.href = 'index.html';
    return null;
  }
  return user;
}
