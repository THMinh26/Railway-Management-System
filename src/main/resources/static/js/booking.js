// Get train data from URL parameters
function getTrainData() {
  const urlParams = new URLSearchParams(window.location.search);

  return {
    trainId: urlParams.get('trainId'),
    trainName: 'Train: ' + urlParams.get('trainName'),
    source: urlParams.get('source'),
    destination: urlParams.get('destination'),
    departureTime: urlParams.get('departureTime'),
    arrivalTime: urlParams.get('arrivalTime'),
    date: urlParams.get('date'),
    duration: calculateDuration(
      urlParams.get('departureTime'),
      urlParams.get('arrivalTime')
    ),
    fare: 1500000,
  };
}

// Calculate duration between departure and arrival times
function calculateDuration(departure, arrival) {
  if (!departure || !arrival) return '0m';

  try {
    const [depHour, depMin] = departure.split(':').map(Number);
    const [arrHour, arrMin] = arrival.split(':').map(Number);

    let hoursDiff = arrHour - depHour;
    let minsDiff = arrMin - depMin;

    if (minsDiff < 0) {
      hoursDiff--;
      minsDiff += 60;
    }

    if (hoursDiff < 0) {
      hoursDiff += 24;
    }

    return `${hoursDiff}h ${minsDiff.toString().padStart(2, '0')}m`;
  } catch (e) {
    return '0m';
  }
}

let availableSeats = [];
let passengerCount = 1;
let train;

// Fetch coaches for the selected train
async function loadCoaches(trainId, travelDate) {
  const coachSelect = document.getElementById('coach');

  try {
    const API_BASE_URL =
      typeof CONFIG !== 'undefined'
        ? CONFIG.API_BASE_URL
        : 'http://localhost:8081/api';
    const response = await fetch(
      `${API_BASE_URL}/coaches/${trainId}?date=${travelDate}`
    );

    if (!response.ok) {
      throw new Error('Failed to load coaches');
    }

    const coaches = await response.json();

    coachSelect.innerHTML = '<option value="">Select Coach</option>';

    coaches.forEach((coach) => {
      const option = document.createElement('option');
      option.value = coach.coachId;
      option.textContent = `${coach.coachName} - ${coach.availableSeats} seats available`;
      option.dataset.coachId = coach.coachId;
      option.dataset.coachName = coach.coachName;

      if (coach.availableSeats === 0) {
        option.disabled = true;
        option.style.color = '#999';
        option.textContent += ' (Full)';
      }

      coachSelect.appendChild(option);
    });
  } catch (error) {
    console.error('Error loading coaches:', error);
    coachSelect.innerHTML = '<option value="">Error loading coaches</option>';
  }
}

// Fetch available seats for selected coach
async function loadSeats(coachId, travelDate) {
  const seatSelect = document.getElementById('seatNumber');

  try {
    const API_BASE_URL =
      typeof CONFIG !== 'undefined'
        ? CONFIG.API_BASE_URL
        : 'http://localhost:8081/api';

    const response = await fetch(
      `${API_BASE_URL}/seats/${coachId}?date=${travelDate}`
    );

    if (!response.ok) {
      throw new Error('Failed to load seats');
    }

    const seats = await response.json();
    availableSeats = seats.filter((seat) => seat.available);

    // Update all passenger seat selects
    updateAllSeatSelects();
  } catch (error) {
    console.error('Error loading seats:', error);
    availableSeats = [];
    updateAllSeatSelects();
  }
}

// Update all seat select dropdowns
function updateAllSeatSelects() {
  for (let i = 1; i <= passengerCount; i++) {
    const seatSelect = document.getElementById(`seat${i}`);
    if (seatSelect) {
      updateSeatSelect(seatSelect, i);
    }
  }
}

// Update individual seat select dropdown
function updateSeatSelect(selectElement, passengerIndex) {
  const selectedSeats = getSelectedSeats();
  const currentSeat = selectElement.value;

  selectElement.innerHTML = '<option value="">Select Seat</option>';

  if (availableSeats.length === 0) {
    selectElement.innerHTML = '<option value="">Select coach first</option>';
    selectElement.disabled = true;
    return;
  }

  selectElement.disabled = false;

  availableSeats.forEach((seat) => {
    const option = document.createElement('option');
    option.value = seat.seatId;
    option.textContent = `Seat ${seat.seatNumber}`;
    option.dataset.seatNumber = seat.seatNumber;

    // Disable if already selected by another passenger
    if (selectedSeats.includes(seat.seatId) && seat.seatId !== currentSeat) {
      option.disabled = true;
      option.textContent += ' (Selected)';
      option.style.color = '#999';
    }

    selectElement.appendChild(option);
  });

  // Restore previous selection if valid
  if (currentSeat && availableSeats.some((s) => s.seatId === currentSeat)) {
    selectElement.value = currentSeat;
  }
}

// Get all currently selected seats
function getSelectedSeats() {
  const selected = [];
  for (let i = 1; i <= passengerCount; i++) {
    const seatSelect = document.getElementById(`seat${i}`);
    if (seatSelect && seatSelect.value) {
      selected.push(seatSelect.value);
    }
  }
  return selected;
}

// Display train information
function displayTrainInfo() {
  document.getElementById('trainName').textContent = train.trainName;
  document.getElementById('trainNumber').textContent = train.trainId;
  document.getElementById('source').textContent = train.source;
  document.getElementById('destination').textContent = train.destination;
  document.getElementById('departureTime').textContent = train.departureTime;
  document.getElementById('arrivalTime').textContent = train.arrivalTime;
  document.getElementById('duration').textContent = train.duration;
  document.getElementById('date').textContent = train.date;
  document.getElementById('farePerSeat').textContent = formatCurrency(
    train.fare
  );
}

// Format currency
function formatCurrency(amount) {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(amount);
}

// Generate passenger forms
function updatePassengerForms() {
  const container = document.getElementById('passengerForms');
  container.innerHTML = '';

  for (let i = 1; i <= passengerCount; i++) {
    const passengerForm = document.createElement('div');
    passengerForm.className = 'passenger-form';
    passengerForm.innerHTML = `
            <h4>Passenger ${i}</h4>
            <div class="form-row">
                <div class="form-group">
                    <label for="name${i}">Full Name</label>
                    <input type="text" id="name${i}" name="name${i}" placeholder="Enter full name" required>
                </div>
                <div class="form-group">
                  <label for="seat${i}">Seat Number</label>
                  <select id="seat${i}" name="seat${i}" required disabled>
                    <option value="">Select coach first</option>
                  </select>
                </div>
            </div>
        `;
    container.appendChild(passengerForm);

    // Add change event listener to update other seat selects
    const seatSelect = document.getElementById(`seat${i}`);
    seatSelect.addEventListener('change', function () {
      updateAllSeatSelects();
    });

    // If coach is already selected, load seats for new passenger forms
    const coachSelect = document.getElementById('coach');
    if (coachSelect.value && train.date) {
      updateAllSeatSelects();
    }
  }
}

// Update booking summary
function updateSummary() {
  const fare = train.fare;
  const subtotal = fare * passengerCount;
  const serviceFee = 50000;
  const total = subtotal + serviceFee;

  document.getElementById('seatCount').textContent = passengerCount;
  document.getElementById('subtotal').textContent = formatCurrency(subtotal);
  document.getElementById('totalAmount').textContent = formatCurrency(total);
}

// Create booking request
async function createBooking() {
  const userJSON = localStorage.getItem('currentUser');
  if (!userJSON) {
    alert('Please login to continue');
    window.location.href = 'login.html';
    return;
  }

  const user = JSON.parse(userJSON);
  const coachSelect = document.getElementById('coach');
  const coachId = coachSelect.value;

  if (!coachId) {
    alert('Please select a coach');
    return;
  }

  // Collect passenger data
  const passengers = [];
  for (let i = 1; i <= passengerCount; i++) {
    const name = document.getElementById(`name${i}`).value;
    const seatId = document.getElementById(`seat${i}`).value;

    if (!name) {
      alert(`Please enter name for Passenger ${i}`);
      return;
    }
    if (!seatId) {
      alert(`Please select seat for Passenger ${i}`);
      return;
    }

    passengers.push({
      passengerName: name,
      seatId: seatId,
    });
  }

  const bookingData = {
    userId: user.userId,
    trainId: train.trainId,
    travelDate: train.date,
    startStationName: train.source,
    endStationName: train.destination,
    numberOfTickets: passengerCount,
    total: train.fare * passengerCount + 50000,
    passengers: passengers,
  };

  console.log(bookingData);

  try {
    const API_BASE_URL =
      typeof CONFIG !== 'undefined'
        ? CONFIG.API_BASE_URL
        : 'http://localhost:8081/api';

    const response = await fetch(`${API_BASE_URL}/booking`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(bookingData),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Failed to create booking');
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error('Booking error:', error);
    throw error;
  }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function () {
  document.body.classList.add('loaded');

  train = getTrainData();
  displayTrainInfo();

  if (train.trainId && train.date) {
    loadCoaches(train.trainId, train.date);
  }

  updatePassengerForms();
  updateSummary();

  // Coach selection - load seats when coach is selected
  document.getElementById('coach').addEventListener('change', function (e) {
    const coachId = e.target.value;

    if (coachId && train.date) {
      loadSeats(coachId, train.date);
    } else {
      availableSeats = [];
      updateAllSeatSelects();
    }
  });

  // Passenger count change
  document.getElementById('passengers').addEventListener('input', function (e) {
    const value = parseInt(e.target.value);
    if (value >= 1 && value <= 10) {
      passengerCount = value;
      updatePassengerForms();
      updateSummary();
    }
  });

  // Payment method toggle
  const paymentRadios = document.querySelectorAll('input[name="payment"]');
  const cardDetails = document.getElementById('cardDetails');

  paymentRadios.forEach((radio) => {
    radio.addEventListener('change', function () {
      if (this.value === 'credit') {
        cardDetails.style.display = 'block';
      } else {
        cardDetails.style.display = 'none';
      }
    });
  });

  // Card number formatting
  document
    .getElementById('cardNumber')
    ?.addEventListener('input', function (e) {
      let value = e.target.value.replace(/\s/g, '');
      let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
      e.target.value = formattedValue;
    });

  // Expiry date formatting
  document.getElementById('expiry')?.addEventListener('input', function (e) {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length >= 2) {
      value = value.slice(0, 2) + '/' + value.slice(2, 4);
    }
    e.target.value = value;
  });

  // CVV input - numbers only
  document.getElementById('cvv')?.addEventListener('input', function (e) {
    e.target.value = e.target.value.replace(/\D/g, '');
  });

  // Confirm booking
  document
    .getElementById('confirmBooking')
    .addEventListener('click', async function () {
      const form = document.getElementById('bookingForm');
      if (!form.checkValidity()) {
        form.reportValidity();
        return;
      }

      const paymentMethod = document.querySelector(
        'input[name="payment"]:checked'
      ).value;
      if (paymentMethod === 'credit') {
        const cardNumber = document.getElementById('cardNumber').value;
        const expiry = document.getElementById('expiry').value;
        const cvv = document.getElementById('cvv').value;
        if (!cardNumber || !expiry || !cvv) {
          alert('Please fill in all card details');
          return;
        }
      }

      this.disabled = true;
      this.innerHTML = '<span class="spinner"></span> Processing...';

      try {
        const result = await createBooking();
        showSuccessModal(result.bookingId);
      } catch (error) {
        alert('Booking failed: ' + error.message);
        this.disabled = false;
        this.innerHTML = 'Confirm & Pay';
      }
    });
});

// Show success modal
function showSuccessModal(bookingId) {
  // ← Only need bookingId
  const modal = document.createElement('div');
  modal.className = 'success-modal';
  modal.innerHTML = `
        <div class="modal-overlay"></div>
        <div class="modal-content success-content">
            <div class="success-icon">✓</div>
            <h2>Booking Successful!</h2>
            <p>Your train ticket has been booked successfully</p>
            <div class="booking-ref">
                <strong>Booking Reference:</strong>
                <span class="ref-code">${bookingId}</span>
            </div>
            <div class="success-details">
                <p>📧 Confirmation email sent to your email</p>
                <p>📱 E-ticket will be sent to your phone</p>
            </div>
            <div class="modal-actions">
                <button onclick="window.location.href='bookings.html'" class="btn-primary">View My Bookings</button>
                <button onclick="window.location.href='index.html'" class="btn-secondary">Book Another Trip</button>
            </div>
        </div>
    `;
  document.body.appendChild(modal);
  setTimeout(() => modal.classList.add('show'), 10);
}
