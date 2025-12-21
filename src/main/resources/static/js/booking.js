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

    seatSelect.innerHTML = '<option value="">Select Seat</option>';
    seatSelect.disabled = false;

    seats.forEach((seat) => {
      const option = document.createElement('option');
      option.value = seat.seatId;
      option.textContent = seat.available
        ? `Seat ${seat.seatNumber}`
        : `Seat ${seat.seatNumber} (Booked)`;
      option.disabled = !seat.available;
      option.dataset.seatNumber = seat.seatNumber;

      if (!seat.available) {
        option.style.color = '#999';
      }

      seatSelect.appendChild(option);
    });

    if (seats.filter((s) => s.available).length === 0) {
      seatSelect.innerHTML = '<option value="">No seats available</option>';
      seatSelect.disabled = true;
    }
  } catch (error) {
    console.error('Error loading seats:', error);
    seatSelect.innerHTML = '<option value="">Error loading seats</option>';
    seatSelect.disabled = true;
  }
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

// Update passenger count
let passengerCount = 1;
let train;

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
            </div>
        `;
    container.appendChild(passengerForm);
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
    const seatSelect = document.getElementById('seatNumber');

    if (coachId && train.date) {
      loadSeats(coachId, train.date);
    } else {
      seatSelect.innerHTML = '<option value="">Select coach first</option>';
      seatSelect.disabled = true;
    }
  });

  // Event listeners
  document
    .getElementById('passengers')
    .addEventListener('change', function (e) {
      passengerCount = parseInt(e.target.value);
      updatePassengerForms();
      updateSummary();
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
    .addEventListener('click', function () {
      const form = document.getElementById('bookingForm');
      if (!form.checkValidity()) {
        form.reportValidity();
        return;
      }

      const coachSelect = document.getElementById('coach');
      const seatSelect = document.getElementById('seatNumber');
      const coach =
        coachSelect.options[coachSelect.selectedIndex].dataset.coachName ||
        coachSelect.value;
      const seatNumber =
        seatSelect.options[seatSelect.selectedIndex].dataset.seatNumber ||
        seatSelect.value;

      if (!coach) {
        alert('Please select a coach');
        return;
      }
      if (!seatNumber) {
        alert('Please select a seat');
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

      setTimeout(() => {
        const bookingRef = 'BK' + Date.now().toString().slice(-6);
        showSuccessModal(bookingRef, coach, seatNumber);

        this.disabled = false;
        this.innerHTML = 'Confirm & Pay';
      }, 2000);
    });
});

// Show success modal
function showSuccessModal(bookingRef, coach, seatNumber) {
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
                <span class="ref-code">${bookingRef}</span>
            </div>
            <div class="success-details">
                <p><strong>Coach:</strong> ${coach}</p>
                <p><strong>Seat Number:</strong> ${seatNumber}</p>
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
