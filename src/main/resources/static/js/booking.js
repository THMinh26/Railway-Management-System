// Get train data from URL parameters or localStorage
function getTrainData() {
    const urlParams = new URLSearchParams(window.location.search);
    const trainId = urlParams.get('trainId');
    
    // For demo, use sample data
    return {
        id: trainId || '1',
        trainNumber: 'TR001',
        trainName: 'Hanoi',
        source: 'Hanoi',
        destination: 'Ho Chi Minh City',
        departureTime: '06:00',
        arrivalTime: '18:00',
        date: '2025-11-10',
        duration: '12h 00m',
        fare: 1500000
    };
}

// Display train information
function displayTrainInfo() {
    document.getElementById('trainName').textContent = train.trainName;
    document.getElementById('trainNumber').textContent = train.trainNumber;
    document.getElementById('source').textContent = train.source;
    document.getElementById('destination').textContent = train.destination;
    document.getElementById('departureTime').textContent = train.departureTime;
    document.getElementById('arrivalTime').textContent = train.arrivalTime;
    document.getElementById('duration').textContent = train.duration;
    document.getElementById('date').textContent = train.date;
    document.getElementById('farePerSeat').textContent = formatCurrency(train.fare);
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
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
                <div class="form-group">
                    <label for="age${i}">Age</label>
                    <input type="number" id="age${i}" name="age${i}" min="1" max="120" placeholder="Age" required>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="gender${i}">Gender</label>
                    <select id="gender${i}" name="gender${i}" required>
                        <option value="">Select Gender</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Other">Other</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="idNumber${i}">ID Number</label>
                    <input type="text" id="idNumber${i}" name="idNumber${i}" placeholder="ID/Passport number">
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
document.addEventListener('DOMContentLoaded', function() {
    // Remove loading screen
    document.body.classList.add('loaded');
    
    train = getTrainData();
    displayTrainInfo();
    updatePassengerForms();
    updateSummary();
    
    // Event listeners
    document.getElementById('passengers').addEventListener('change', function(e) {
        passengerCount = parseInt(e.target.value);
        updatePassengerForms();
        updateSummary();
    });
    
    // Payment method toggle
    const paymentRadios = document.querySelectorAll('input[name="payment"]');
    const cardDetails = document.getElementById('cardDetails');
    
    paymentRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            if (this.value === 'credit') {
                cardDetails.style.display = 'block';
            } else {
                cardDetails.style.display = 'none';
            }
        });
    });
    
    // Card number formatting
    document.getElementById('cardNumber')?.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\s/g, '');
        let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
        e.target.value = formattedValue;
    });
    
    // Expiry date formatting
    document.getElementById('expiry')?.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length >= 2) {
            value = value.slice(0, 2) + '/' + value.slice(2, 4);
        }
        e.target.value = value;
    });
    
    // CVV input - numbers only
    document.getElementById('cvv')?.addEventListener('input', function(e) {
        e.target.value = e.target.value.replace(/\D/g, '');
    });
    
    // Confirm booking
    document.getElementById('confirmBooking').addEventListener('click', function() {
        // Validate form
        const form = document.getElementById('bookingForm');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        // Validate coach and seat number
        const coach = document.getElementById('coach').value;
        const seatNumber = document.getElementById('seatNumber').value;
        if (!coach) {
            alert('Please select a coach');
            return;
        }
        if (!seatNumber) {
            alert('Please enter a seat number');
            return;
        }

        // Check payment details if credit card selected
        const paymentMethod = document.querySelector('input[name="payment"]:checked').value;
        if (paymentMethod === 'credit') {
            const cardNumber = document.getElementById('cardNumber').value;
            const expiry = document.getElementById('expiry').value;
            const cvv = document.getElementById('cvv').value;
            if (!cardNumber || !expiry || !cvv) {
                alert('Please fill in all card details');
                return;
            }
        }

        // Show loading
        this.disabled = true;
        this.innerHTML = '<span class="spinner"></span> Processing...';

        // Simulate payment processing
        setTimeout(() => {
            // Generate booking reference
            const bookingRef = 'BK' + Date.now().toString().slice(-6);

            // Show success message with coach and seat
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
    // Animate in
    setTimeout(() => modal.classList.add('show'), 10);
}
