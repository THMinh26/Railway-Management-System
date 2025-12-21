// API Base URL
// Frontend-only/demo mode flag
const API_ENABLED = true; // set to true when backend is available
const API_BASE_URL = API_ENABLED ? 'http://localhost:8081/api' : '';

// Utility function to show messages
function showMessage(elementId, message, type) {
  const messageEl = document.getElementById(elementId);
  if (messageEl) {
    messageEl.textContent = message;
    messageEl.className = `message ${type}`;
    messageEl.style.display = 'block';
    setTimeout(() => {
      messageEl.style.display = 'none';
    }, 5000);
  }
}

// Get current user from localStorage
function getCurrentUser() {
  const userStr = localStorage.getItem('currentUser');
  return userStr ? JSON.parse(userStr) : null;
}

// Save user to localStorage
function saveCurrentUser(user) {
  localStorage.setItem('currentUser', JSON.stringify(user));
}

// Clear current user
function clearCurrentUser() {
  localStorage.removeItem('currentUser');
}

// Check if user is logged in
function isLoggedIn() {
  return getCurrentUser() !== null;
}

// Format date
function formatDate(dateString) {
  const date = new Date(dateString);
  return (
    date.toLocaleDateString() +
    ' ' +
    date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  );
}

// Set minimum date to today
function setMinDateToToday() {
  const dateInputs = document.querySelectorAll('input[type="date"]');
  const today = new Date().toISOString().split('T')[0];
  dateInputs.forEach((input) => {
    input.min = today;
  });
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function () {
  setMinDateToToday();
  updateNavigation();
  initSwapButton();
});

// Swap stations button
function initSwapButton() {
  const swapBtn = document.querySelector('.swap-btn');
  if (swapBtn) {
    swapBtn.addEventListener('click', function () {
      const sourceInput = document.getElementById('source');
      const destInput = document.getElementById('destination');

      if (sourceInput && destInput) {
        const temp = sourceInput.value;
        sourceInput.value = destInput.value;
        destInput.value = temp;
      }
    });
  }
}

// Update navigation based on login status
function updateNavigation() {
  const user = getCurrentUser();
  const navMenu = document.querySelector('.nav-menu');

  if (navMenu && user) {
    const loginLink = navMenu.querySelector('a[href="login.html"]');
    const registerLink = navMenu.querySelector('a[href="register.html"]');

    if (loginLink) {
      loginLink.textContent = `Hi, ${user.username}`;
      loginLink.style.cursor = 'default';
    }

    if (registerLink) {
      registerLink.textContent = 'Logout';
      registerLink.href = '#';
      registerLink.addEventListener('click', function (e) {
        e.preventDefault();
        clearCurrentUser();
        window.location.href = 'index.html';
      });
    }
  }
}

// Main form search handler (for index.html)
const mainSearchForm = document.getElementById('searchForm');
if (mainSearchForm) {
  mainSearchForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const source = document.getElementById('source').value;
    const destination = document.getElementById('destination').value;
    const date = document.getElementById('outbound-date').value;

    if (!source || !destination || !date) {
      alert('Please fill in all fields');
      return;
    }

    // Show loading
    const searchBtn = mainSearchForm.querySelector('.btn-search');
    const originalText = searchBtn.innerHTML;
    searchBtn.innerHTML = '<span>Searching...</span>';
    searchBtn.disabled = true;

    try {
      let trains = [];
      if (!API_ENABLED) {
        // Demo data for search results
        trains = [
          {
            id: 1,
            trainNumber: 'TR001',
            trainName: 'Hanoi Express',
            source,
            destination,
            departureTime: new Date().toISOString(),
            arrivalTime: new Date(
              Date.now() + 1000 * 60 * 60 * 6
            ).toISOString(),
            fare: 150000,
            trainType: 'Express',
            availableSeats: 20,
          },
          {
            id: 2,
            trainNumber: 'TR002',
            trainName: 'Coastal Line',
            source,
            destination,
            departureTime: new Date().toISOString(),
            arrivalTime: new Date(
              Date.now() + 1000 * 60 * 60 * 8
            ).toISOString(),
            fare: 120000,
            trainType: 'Regional',
            availableSeats: 5,
          },
        ];
      } else {
        // Search for trains - POST request with JSON body
        const response = await fetch(`${API_BASE_URL}/trains/search`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            source: source,
            destination: destination,
            date: date,
          }),
        });

        if (!response.ok) {
          throw new Error('Search failed');
        }

        trains = await response.json();
      }

      // Store results in sessionStorage and redirect to results section
      sessionStorage.setItem('searchResults', JSON.stringify(trains));
      sessionStorage.setItem(
        'searchParams',
        JSON.stringify({ source, destination, date })
      );

      // Display results on same page
      displaySearchResults(trains, { source, destination, date });

      // Scroll to results
      document
        .getElementById('searchResults')
        .scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
      alert('Error searching trains: ' + error.message);
    } finally {
      searchBtn.innerHTML = originalText;
      searchBtn.disabled = false;
    }
  });
}

// Display search results
function displaySearchResults(trains, params) {
  let resultsSection = document.getElementById('searchResults');

  // Create results section if it doesn't exist
  if (!resultsSection) {
    resultsSection = document.createElement('section');
    resultsSection.id = 'searchResults';
    resultsSection.className = 'search-results';

    const searchSection = document.querySelector('.search-section');
    searchSection.parentNode.insertBefore(
      resultsSection,
      searchSection.nextSibling
    );
  }

  if (trains.length === 0) {
    resultsSection.innerHTML = `
            <div class="container">
                <div class="no-results">
                    <h3>No trains found</h3>
                    <p>No trains available for ${params.source} to ${params.destination} on ${params.date}</p>
                </div>
            </div>
        `;
    return;
  }

  resultsSection.innerHTML = `
        <div class="container">
            <div class="results-header">
                <h3>Available Trains</h3>
                <p>${trains.length} train(s) found from ${params.source} to ${
    params.destination
  } on ${params.date}</p>
            </div>
            <div class="trains-list">
                ${trains
                  .map(
                    (train) => `
                    <div class="train-card">
                        <div class="train-info">
                            <div class="train-header">
                                <h4>Train: ${train.trainName}</h4>
                                <span class="train-number">${train.trainId}</span>
                            </div>
                            <div class="route-info">
                                <div class="station">
                                    <strong>${train.source}</strong>
                                    <span class="time">
                                      ${train.departureTime}
                                    </span>
                                </div>
                                <div class="route-arrow">→</div>
                                <div class="station">
                                    <strong>${train.destination}</strong>
                                    <span class="time">${train.arrivalTime}</span>
                                </div>
                            </div>
                        </div>
                        <div class="train-actions">
                            <button onclick="bookTrain('${train.trainId}')" class="btn-book">Book Now</button>
                        </div>
                    </div>
                `
                  )
                  .join('')}
            </div>
        </div>
    `;
}

// Book train function
async function bookTrain(trainId) {
  const user = getCurrentUser();

  if (!user) {
    alert('Please login to book a train');
    window.location.href = 'login.html';
    return;
  }

  // Get train data from the displayed results
  const searchResults = JSON.parse(
    sessionStorage.getItem('searchResults') || '[]'
  );
  const train = searchResults.find((t) => t.trainId === trainId);

  if (!train) {
    alert('Train data not found');
    return;
  }

  // Build query parameters
  const params = new URLSearchParams({
    trainId: train.trainId,
    trainName: train.trainName,
    source: train.source,
    destination: train.destination,
    date: sessionStorage.getItem('searchParams')
      ? JSON.parse(sessionStorage.getItem('searchParams')).date
      : '',
    departureTime: train.departureTime,
    arrivalTime: train.arrivalTime,
  });

  // Redirect to booking page with parameters
  window.location.href = `/booking?${params.toString()}`;
}
