// Bookings Page Handler
const bookingsList = document.getElementById('bookingsList');

document.addEventListener('DOMContentLoaded', function() {
    if (!isLoggedIn()) {
        bookingsList.innerHTML = '<p>Please <a href="login.html">login</a> to view your bookings.</p>';
        return;
    }
    
    loadBookings();
});

async function loadBookings() {
    const user = getCurrentUser();
    
    try {
        if (typeof API_ENABLED !== 'undefined' && !API_ENABLED) {
            // Demo bookings for offline frontend
            const bookings = [
                { id:1, bookingReference:'BK1001', train:{ trainName:'Hanoi Express', trainNumber:'TR001', source:'Hanoi', destination:'HCMC', departureTime:new Date().toISOString() }, passengerName:'Nguyen Van A', passengerAge:30, passengerGender:'Male', numberOfSeats:2, totalFare:300000, bookingDate:new Date().toISOString(), status:'CONFIRMED' }
            ];
            displayBookings(bookings);
            return;
        }
        
        const response = await fetch(`${API_BASE_URL}/bookings/user/${user.userId}`);
        const bookings = await response.json();
        
        displayBookings(bookings);
    } catch (error) {
        console.error('Error loading bookings:', error);
        bookingsList.innerHTML = '<p class="error">Error loading bookings. Please try again.</p>';
    }
}

function displayBookings(bookings) {
    if (bookings.length === 0) {
        bookingsList.innerHTML = '<p>You have no bookings yet. <a href="search.html">Search trains</a> to book your first ticket.</p>';
        return;
    }
    
    bookingsList.innerHTML = bookings.map(booking => `
        <div class="booking-card">
            <div class="booking-header">
                <div class="booking-reference">Booking Ref: ${booking.bookingReference}</div>
                <span class="status-badge status-${booking.status.toLowerCase()}">${booking.status}</span>
            </div>
            <div class="train-details">
                <div class="detail-item">
                    <span class="detail-label">Train</span>
                    <span class="detail-value">${booking.train.trainName} (${booking.train.trainNumber})</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">From</span>
                    <span class="detail-value">${booking.train.source}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">To</span>
                    <span class="detail-value">${booking.train.destination}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Departure</span>
                    <span class="detail-value">${formatDate(booking.train.departureTime)}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Passenger</span>
                    <span class="detail-value">${booking.passengerName}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Age</span>
                    <span class="detail-value">${booking.passengerAge}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Gender</span>
                    <span class="detail-value">${booking.passengerGender}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Seats</span>
                    <span class="detail-value">${booking.numberOfSeats}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Total Fare</span>
                    <span class="detail-value">₹${booking.totalFare}</span>
                </div>
                <div class="detail-item">
                    <span class="detail-label">Booking Date</span>
                    <span class="detail-value">${formatDate(booking.bookingDate)}</span>
                </div>
            </div>
            ${booking.status === 'CONFIRMED' ? `
                <button class="btn btn-danger" onclick="cancelBooking(${booking.id})">Cancel Booking</button>
            ` : ''}
        </div>
    `).join('');
}

async function cancelBooking(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/bookings/${bookingId}`, {
            method: 'DELETE'
        });
        
        const data = await response.json();
        
        if (response.ok) {
            alert('Booking cancelled successfully');
            loadBookings(); // Reload bookings
        } else {
            alert(data.error || 'Failed to cancel booking');
        }
    } catch (error) {
        console.error('Cancel booking error:', error);
        alert('Error cancelling booking. Please try again.');
    }
}
