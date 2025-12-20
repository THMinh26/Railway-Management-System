// Authentication handling
document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.getElementById('loginForm');
  const registerForm = document.getElementById('registerForm');
  const messageDiv = document.getElementById('message');

  // Login Form Handler
  if (loginForm) {
    loginForm.addEventListener('submit', async function (e) {
      e.preventDefault();

      const username = document.getElementById('username').value.trim();
      const password = document.getElementById('password').value;

      // Validate inputs
      if (!username || !password) {
        showMessage('Please fill in all fields', 'error');
        return;
      }

      try {
        showMessage('Logging in...', 'info');

        const response = await fetch(
          `${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.LOGIN}`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              username: username,
              password: password,
            }),
          }
        );

        const data = await response.json();

        if (response.ok) {
          // Store user data in localStorage
          localStorage.setItem(
            'currentUser',
            JSON.stringify({
              username: data.username,
              fullname: data.fullname,
              email: data.email,
              phone: data.phone,
              isAdmin: data.isAdmin,
            })
          );

          showMessage(
            data.message || 'Login successful! Redirecting...',
            'success'
          );

          // Redirect based on user role
          setTimeout(() => {
            if (data.isAdmin) {
              window.location.href = 'admin.html';
            } else {
              window.location.href = 'index.html';
            }
          }, 1000);
        } else {
          // Handle error response
          showMessage(data.error || 'Invalid username or password', 'error');
        }
      } catch (error) {
        console.error('Login error:', error);
        showMessage(
          'Network error. Please check your connection and try again.',
          'error'
        );
      }
    });
  }

  // Register Form Handler
  if (registerForm) {
    registerForm.addEventListener('submit', async function (e) {
      e.preventDefault();

      const username = document.getElementById('username').value.trim();
      const fullname = document.getElementById('fullname').value.trim();
      const email = document.getElementById('email').value.trim();
      const phone = document.getElementById('phone').value.trim();
      const password = document.getElementById('password').value;
      const confirmPassword = document.getElementById('confirmPassword').value;

      // Validate inputs
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

      // Validate username (alphanumeric, 3-15 chars)
      if (!/^[a-zA-Z0-9_]{3,15}$/.test(username)) {
        showMessage(
          'Username must be 3-15 characters (letters, numbers, underscore only)',
          'error'
        );
        return;
      }

      // Validate email
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        showMessage('Please enter a valid email address', 'error');
        return;
      }

      // Validate phone (10-15 digits)
      if (!/^\d{10,15}$/.test(phone)) {
        showMessage('Phone number must be 10-15 digits', 'error');
        return;
      }

      // Validate password
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

        const response = await fetch(
          `${CONFIG.API_BASE_URL}${CONFIG.ENDPOINTS.REGISTER}`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              username: username,
              fullname: fullname,
              email: email,
              phone: phone,
              password: password,
            }),
          }
        );

        const data = await response.json();

        if (response.ok) {
          showMessage(
            data.message || 'Registration successful! Redirecting to login...',
            'success'
          );

          // Clear form
          registerForm.reset();

          // Redirect to login page
          setTimeout(() => {
            window.location.href = 'login.html';
          }, 2000);
        } else {
          // Handle error response
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

  // Show message function
  function showMessage(message, type) {
    if (messageDiv) {
      messageDiv.textContent = message;
      messageDiv.className = `message ${type}`;
      messageDiv.style.display = 'block';

      // Auto-hide success/info messages after 5 seconds
      if (type === 'success' || type === 'info') {
        setTimeout(() => {
          messageDiv.style.display = 'none';
        }, 5000);
      }
    }
  }
});

// Check if user is already logged in
function checkAuth() {
  const currentUser = localStorage.getItem('currentUser');
  if (currentUser) {
    return JSON.parse(currentUser);
  }
  return null;
}

// Logout function
function logout() {
  localStorage.removeItem('currentUser');
  window.location.href = 'login.html';
}

// Protect page (redirect to login if not authenticated)
function requireAuth() {
  const user = checkAuth();
  if (!user) {
    window.location.href = 'login.html';
    return null;
  }
  return user;
}

// Check if user is admin
function requireAdmin() {
  const user = requireAuth();
  if (user && !user.isAdmin) {
    alert('Access denied. Admin privileges required.');
    window.location.href = 'index.html';
    return null;
  }
  return user;
}
