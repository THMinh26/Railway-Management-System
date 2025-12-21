// Admin Panel JavaScript

let currentBookingId = null;
let currentUserId = null;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    initializeTabs();
    loadStatistics();
    loadAllBookings();
    loadAllUsers();
});

// Tab switching
function initializeTabs() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const tabId = btn.dataset.tab;
            switchTab(tabId);
        });
    });
}

function switchTab(tabId) {
    // Update buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-tab="${tabId}"]`).classList.add('active');

    // Update content
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.remove('active');
    });
    document.getElementById(tabId).classList.add('active');

    // Load data for specific tabs
    if (tabId === 'bookings') {
        loadAllBookings();
    } else if (tabId === 'users') {
        loadAllUsers();
    }
}

// Load Statistics
async function loadStatistics() {
    try {
        if (!API_ENABLED) {
            // Demo data when no backend is present
            const stats = {
                totalBookings: 12,
                totalUsers: 5,
                totalTrains: 8,
                cancelledBookings: 1
            };
            document.getElementById('totalBookings').textContent = stats.totalBookings;
            document.getElementById('totalUsers').textContent = stats.totalUsers;
            document.getElementById('totalTrains').textContent = stats.totalTrains;
            document.getElementById('cancelledBookings').textContent = stats.cancelledBookings;
            return;
        }

        const response = await fetch(`${API_BASE_URL}/admin/stats`);
        const stats = await response.json();
        
        document.getElementById('totalBookings').textContent = stats.totalBookings;
        document.getElementById('totalUsers').textContent = stats.totalUsers;
        document.getElementById('totalTrains').textContent = stats.totalTrains || 0;
        document.getElementById('cancelledBookings').textContent = stats.cancelledBookings;
    } catch (error) {
        console.error('Error loading statistics:', error);
    }
}

// Load All Bookings
async function loadAllBookings() {
    try {
        const tbody = document.getElementById('bookingsTableBody');
        if (!API_ENABLED) {
            // Demo bookings
            const bookings = [
                { id: 1, bookingReference: 'BK1001', user: { fullName: 'Nguyen Van A' }, train: { trainNumber: 'TR001', source: 'Hanoi', destination: 'HCMC' }, numberOfSeats: 2, totalFare: 300000, status: 'CONFIRMED' },
                { id: 2, bookingReference: 'BK1002', user: { fullName: 'Tran Thi B' }, train: { trainNumber: 'TR002', source: 'Hue', destination: 'Da Nang' }, numberOfSeats: 1, totalFare: 150000, status: 'CANCELLED' }
            ];
            tbody.innerHTML = bookings.map(booking => `
                <tr>
                    <td><strong>${booking.bookingReference}</strong></td>
                    <td>${booking.user?.fullName || 'N/A'}</td>
                    <td>${booking.train?.trainNumber || 'N/A'}<br>
                        <small>${booking.train?.source} → ${booking.train?.destination}</small>
                    </td>
                    <td>${booking.numberOfSeats}</td>
                    <td>${booking.totalFare}</td>
                    <td><span class="status-badge status-${booking.status.toLowerCase()}">${booking.status}</span></td>
                    <td>
                        <button class="action-btn btn-update" onclick="showUpdateModal(${booking.id})">Update</button>
                        <button class="action-btn btn-cancel" onclick="cancelBooking(${booking.id})">Cancel</button>
                    </td>
                </tr>
            `).join('');
            return;
        }

        const response = await fetch(`${API_BASE_URL}/admin/bookings`);
        const data = await response.json();
        const bookings = data.bookings || [];
        
        if (bookings.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No bookings found</td></tr>';
            return;
        }
        
        tbody.innerHTML = bookings.map(booking => `
            <tr>
                <td><strong>${booking.bookingId || 'undefined'}</strong></td>
                <td>${booking.user?.username || 'N/A'}</td>
                <td>N/A<br>
                    <small>undefined → undefined</small>
                </td>
                <td>${booking.numberOfTickets || 'undefined'}</td>
                <td>₹${booking.total || 'undefined'}</td>
                <td><span class="status-badge status-${(booking.status || '').toLowerCase()}">${booking.status || 'UNKNOWN'}</span></td>
                <td>
                    <button class="action-btn btn-update" onclick="showUpdateModal('${booking.bookingId}')">Update</button>
                    <button class="action-btn btn-cancel" onclick="cancelBooking('${booking.bookingId}')">Cancel</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading bookings:', error);
        document.getElementById('bookingsTableBody').innerHTML = 
            '<tr><td colspan="7" style="text-align: center; color: red;">Error loading bookings</td></tr>';
    }
}

// Load All Users
async function loadAllUsers() {
    try {
        const tbody = document.getElementById('usersTableBody');
        if (!API_ENABLED) {
            const users = [
                { id: 1, username: 'admin', fullName: 'Administrator', email: 'admin@example.com', phoneNumber: '0901234567' },
                { id: 2, username: 'user1', fullName: 'Nguyen Van A', email: 'a@example.com', phoneNumber: '' }
            ];
            tbody.innerHTML = users.map(user => `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.fullName}</td>
                    <td>${user.email}</td>
                    <td>${user.phoneNumber || 'N/A'}</td>
                    <td>
                        <button class="action-btn btn-message" onclick="showMessageModal(${user.id})">Send Message</button>
                    </td>
                </tr>
            `).join('');
            return;
        }

        const response = await fetch(`${API_BASE_URL}/admin/users`);
        const data = await response.json();
        const users = data.users || [];
        
        if (users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No users found</td></tr>';
            return;
        }
        
        tbody.innerHTML = users.map(user => `
            <tr>
                <td>${user.userId}</td>
                <td>${user.username}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.phone || 'N/A'}</td>
                <td>
                    <button class="action-btn btn-message" onclick="showMessageModal(${user.userId})">Send Message</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading users:', error);
        document.getElementById('usersTableBody').innerHTML = 
            '<tr><td colspan="6" style="text-align: center; color: red;">Error loading users</td></tr>';
    }
}

// Show Update Status Modal
function showUpdateModal(bookingId) {
    currentBookingId = bookingId;
    document.getElementById('updateModal').classList.add('active');
}

// Submit Update Status
async function submitUpdateStatus() {
    const status = document.getElementById('updateStatus').value;
    const reason = document.getElementById('updateReason').value;
    
    try {
        if (!API_ENABLED) {
            alert('Demo mode: booking status not sent to backend. Status would be: ' + status);
            closeModal('updateModal');
            return;
        }

        const response = await fetch(`${API_BASE_URL}/admin/bookings/${currentBookingId}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status, reason })
        });

        if (response.ok) {
            alert('Booking status updated successfully!');
            closeModal('updateModal');
            loadAllBookings();
            loadStatistics();
        } else {
            alert('Failed to update booking status');
        }
    } catch (error) {
        console.error('Error updating booking:', error);
        alert('Error updating booking status');
    }
}

// Cancel Booking
async function cancelBooking(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
        return;
    }
    
    const reason = prompt('Enter cancellation reason (optional):');
    
    try {
        if (!API_ENABLED) {
            alert('Demo mode: booking cancellation not sent to backend. Reason: ' + (reason || ''));
            return;
        }

        const response = await fetch(`${API_BASE_URL}/admin/bookings/${bookingId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ reason: reason || '' })
        });

        if (response.ok) {
            alert('Booking cancelled successfully!');
            loadAllBookings();
            loadStatistics();
        } else {
            alert('Failed to cancel booking');
        }
    } catch (error) {
        console.error('Error cancelling booking:', error);
        alert('Error cancelling booking');
    }
}

// Show Message Modal
function showMessageModal(userId) {
    currentUserId = userId;
    document.getElementById('messageModal').classList.add('active');
}

// Submit Send Message
async function submitSendMessage() {
    const title = document.getElementById('messageTitle').value;
    const message = document.getElementById('messageContent').value;
    
    if (!title || !message) {
        alert('Please fill in all fields');
        return;
    }
    
    try {
        if (!API_ENABLED) {
            alert('Demo mode: message not sent to backend. Title: ' + title);
            closeModal('messageModal');
            document.getElementById('messageTitle').value = '';
            document.getElementById('messageContent').value = '';
            return;
        }

        const response = await fetch(`${API_BASE_URL}/admin/users/${currentUserId}/notify`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ 
                title, 
                message,
                link: '/messages.html'
            })
        });
        
        if (response.ok) {
            alert('Message sent successfully!');
            closeModal('messageModal');
            document.getElementById('messageTitle').value = '';
            document.getElementById('messageContent').value = '';
        } else {
            alert('Failed to send message');
        }
    } catch (error) {
        console.error('Error sending message:', error);
        alert('Error sending message');
    }
}

// Close Modal
function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

// Close modal when clicking outside
document.querySelectorAll('.modal').forEach(modal => {
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.classList.remove('active');
        }
    });
});
