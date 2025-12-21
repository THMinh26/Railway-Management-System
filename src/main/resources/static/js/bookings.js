const API_BASE_URL =
  typeof CONFIG !== 'undefined' ? CONFIG.API_BASE_URL : 'http://localhost:8081';
const bookingsList = document.getElementById('bookingsList');

document.addEventListener('DOMContentLoaded', function () {
  const userJSON = localStorage.getItem('currentUser');
  const user = JSON.parse(userJSON);

  if (!user) {
    bookingsList.innerHTML =
      '<p>Please <a href="login.html">login</a> to view your bookings.</p>';
    return;
  }
  loadBookings(user.userId);
});

async function loadBookings(userId) {
  try {
    const response = await fetch(`${API_BASE_URL}/booking/user/${userId}`);

    if (!response.ok) {
      throw new Error('Failed to load bookings');
    }

    const bookings = await response.json();

    displayBookings(bookings);
  } catch (error) {
    console.error('Error loading bookings:', error);
    bookingsList.innerHTML =
      '<p class="error">Error loading bookings. Please try again.</p>';
  }
}

function displayBookings(bookings) {
  if (bookings.length === 0) {
    bookingsList.innerHTML =
      '<p>You have no bookings yet. <a href="search.html">Search trains</a> to book your first ticket.</p>';
    return;
  }

  bookingsList.innerHTML = bookings
    .map((booking) => {
      const firstTicket =
        booking.tickets && booking.tickets.length > 0
          ? booking.tickets[0]
          : null;
      const canCancel = firstTicket
        ? canCancelBooking(firstTicket.travelDate)
        : false;

      return `
        <div class="booking-card ${
          booking.status === 'CANCELLED' ? 'cancelled' : ''
        }">
            <div class="booking-header">
                <div class="booking-reference">Booking ID: </strong> ${
                  booking.bookingId
                }</div>
                <span class="status-badge status-${booking.status.toLowerCase()}">${
        booking.status
      }</span>
            </div>
            <div class="train-details">
                <div class="detail-item">
                    <span class="detail-label">Train Name</span>
                    <span class="detail-value">${firstTicket.trainName}</span>
                </div>
                <div class="train-details">
                <div class="detail-row">
                    <div class="detail-item">
                        <span class="detail-label">Train</span>
                        <span class="detail-value">${firstTicket.trainId} (${
        firstTicket.trainId
      })</span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Travel Date</span>
                        <span class="detail-value">${formatDate(
                          firstTicket.travelDate
                        )}</span>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-item">
                        <span class="detail-label">From</span>
                        <span class="detail-value">${
                          firstTicket.startStationName
                        }</span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">To</span>
                        <span class="detail-value">${
                          firstTicket.endStationName
                        }</span>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-item">
                        <span class="detail-label">Number of Tickets</span>
                        <span class="detail-value">${
                          booking.numberOfTickets
                        }</span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Total Amount</span>
                        <span class="detail-value">${formatCurrency(
                          booking.total
                        )}</span>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-item">
                        <span class="detail-label">Booking Date</span>
                        <span class="detail-value">${formatDate(
                          booking.bookingDate
                        )}</span>
                    </div>
                </div>
            </div>
            <div class="booking-actions">
                <button class="btn btn-primary" onclick="viewBookingDetails('${
                  booking.bookingId
                }')">View Details</button>
                ${
                  booking.status === 'CONFIRMED' && canCancel
                    ? `
                    <button class="btn btn-danger" onclick="cancelBooking('${booking.bookingId}')">Cancel Booking</button>
                `
                    : ''
                }
                ${
                  booking.status === 'CONFIRMED' && !canCancel
                    ? `
                    <span class="text-muted">Cannot cancel (less than 1 day before travel)</span>
                `
                    : ''
                }
            </div>
        </div>
    `;
    })
    .join('');
}

async function viewBookingDetails(bookingId) {
  try {
    const response = await fetch(`${API_BASE_URL}/booking/${bookingId}`);

    if (!response.ok) {
      throw new Error('Failed to load booking details');
    }

    const booking = await response.json();
    displayBookingDetailsModal(booking);
  } catch (error) {
    console.error('Error loading booking details:', error);
    alert('Error loading booking details. Please try again.');
  }
}

function displayBookingDetailsModal(booking) {
  const modal = document.getElementById('detailsModal');
  const modalContent = document.getElementById('modalContent');

  modalContent.innerHTML = `
    <div class="booking-details">
      <div class="detail-section">
        <h3>Booking Information</h3>
        <p><strong>Booking ID:</strong> ${booking.bookingId}</p>
        <p><strong>Booking Date:</strong> ${formatDate(booking.bookingDate)}</p>
        <p><strong>Status:</strong> <span class="status-badge status-${booking.status.toLowerCase()}">${
    booking.status
  }</span></p>
        <p><strong>Total Amount:</strong> ${formatCurrency(booking.total)}</p>
      </div>
      
      <div class="detail-section">
        <h3>Tickets (${booking.tickets.length})</h3>
        ${booking.tickets
          .map(
            (ticket, index) => `
          <div class="ticket-card">
            <h4>Ticket ${index + 1}</h4>
            <div class="ticket-details">
              <p><strong>Ticket ID:</strong> ${ticket.ticketId}</p>
              <p><strong>Passenger:</strong> ${ticket.passengerName}</p>
              <p><strong>Train:</strong> ${ticket.trainName} (${
              ticket.trainId
            })</p>
              <p><strong>Travel Date:</strong> ${formatDate(
                ticket.travelDate
              )}</p>
              <p><strong>From:</strong> ${ticket.startStationName}</p>
              <p><strong>To:</strong> ${ticket.endStationName}</p>
              <p><strong>Coach:</strong> ${ticket.coachName}</p>
              <p><strong>Seat Number:</strong> ${ticket.seatNumber}</p>
            </div>
          </div>
        `
          )
          .join('')}
      </div>
    </div>
  `;

  modal.style.display = 'block';
}

function closeDetailsModal() {
  const modal = document.getElementById('detailsModal');
  modal.style.display = 'none';
}

window.onclick = function (event) {
  const modal = document.getElementById('detailsModal');
  if (event.target == modal) {
    modal.style.display = 'none';
  }
};

async function cancelBooking(bookingId) {
  if (!confirm('Are you sure you want to cancel this booking?')) {
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/booking/${bookingId}`, {
      method: 'DELETE',
    });

    const data = await response.text();

    if (response.ok) {
      alert('Booking cancelled successfully');
      const user = getCurrentUser();
      loadBookings(user.userId);
    } else {
      alert(data || 'Failed to cancel booking');
    }
  } catch (error) {
    console.error('Cancel booking error:', error);
    alert('Error cancelling booking. Please try again.');
  }
}

function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  });
}

function formatCurrency(amount) {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(amount);
}

function canCancelBooking(travelDateStr) {
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const travelDate = new Date(travelDateStr);
  travelDate.setHours(0, 0, 0, 0);

  const oneDayBeforeTravel = new Date(travelDate);
  oneDayBeforeTravel.setDate(oneDayBeforeTravel.getDate() - 1);

  return today <= oneDayBeforeTravel;
}
