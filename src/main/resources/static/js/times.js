// Train Times & Tickets Page

const DEMO_TRAINS = [
    { id:1, trainNumber:'TR001', trainName:'Hanoi Express', trainType:'Express', source:'Hanoi', destination:'HCMC', departureTime:new Date().toISOString(), arrivalTime:new Date(Date.now()+5*3600*1000).toISOString(), availableSeats:10, totalSeats:200, fare:150000 },
    { id:2, trainNumber:'TR002', trainName:'Coastal Line', trainType:'Regional', source:'Da Nang', destination:'Nha Trang', departureTime:new Date().toISOString(), arrivalTime:new Date(Date.now()+3*3600*1000).toISOString(), availableSeats:0, totalSeats:180, fare:120000 }
];

let allTrains = [];
let selectedTrain = null;

document.addEventListener('DOMContentLoaded', function() {
    // Load all trains on page load
    loadAllTrains();
    
    // Filter form submission
    const filterForm = document.getElementById('filterForm');
    if (filterForm) {
        filterForm.addEventListener('submit', function(e) {
            e.preventDefault();
            filterTrains();
        });
    }
    
    // Clear filter button
    const clearFilterBtn = document.getElementById('clearFilter');
    if (clearFilterBtn) {
        clearFilterBtn.addEventListener('click', function() {
            document.getElementById('filterForm').reset();
            displayTrains(allTrains);
        });
    }
    
    // Sort dropdown
    const sortBy = document.getElementById('sortBy');
    if (sortBy) {
        sortBy.addEventListener('change', function() {
            sortTrains(this.value);
        });
    }
    
    // Modal close button
    const closeModal = document.querySelector('.close-modal');
    if (closeModal) {
        closeModal.addEventListener('click', closeBookingModal);
    }
    
    // Cancel booking button
    const cancelBtn = document.getElementById('cancelBooking');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', closeBookingModal);
    }
    
    // Close modal when clicking outside
    window.addEventListener('click', function(e) {
        const modal = document.getElementById('bookingModal');
        if (e.target === modal) {
            closeBookingModal();
        }
    });
    
    // Booking form submission
    const bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', handleBookingSubmit);
    }
    
    // Update total fare when seats change
    const seatsInput = document.getElementById('numberOfSeats');
    if (seatsInput) {
        seatsInput.addEventListener('input', updateTotalFare);
    }
    
    // Set default date to today
    const dateInput = document.getElementById('filterDate');
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.value = today;
        dateInput.min = today;
    }
});

// Load all trains from API
async function loadAllTrains() {
    const loadingSpinner = document.getElementById('loadingSpinner');
    const trainsList = document.getElementById('trainsList');
    const noResults = document.getElementById('noResults');
    
    try {
        loadingSpinner.style.display = 'flex';
        trainsList.innerHTML = '';
        noResults.style.display = 'none';
        
        // If API_ENABLED is falsy, use demo trains
        if (typeof API_ENABLED === 'undefined' || !API_ENABLED) {
            allTrains = DEMO_TRAINS.slice();
            loadingSpinner.style.display = 'none';
            displayTrains(allTrains);
            return;
        }

        // Try to fetch schedules and construct train list from schedules
        try {
            const schedResp = await fetch(`${API_BASE_URL}/admin/schedules`);
            if (schedResp.ok) {
                const schedData = await schedResp.json();
                const schedules = schedData.schedules || [];

                // Group schedules by trainId
                const trainsMap = new Map();
                schedules.forEach(s => {
                    const tId = s.trainId;
                    if (!trainsMap.has(tId)) trainsMap.set(tId, []);
                    trainsMap.get(tId).push(s);
                });

                const apiTrains = [];
                const today = new Date().toISOString().split('T')[0];

                trainsMap.forEach((scheds, trainId) => {
                    // sort by sequenceNo
                    scheds.sort((a,b)=>a.sequenceNo - b.sequenceNo);
                    const first = scheds[0];
                    const last = scheds[scheds.length-1];

                    // build train object used by displayTrains
                    apiTrains.push({
                        id: trainId,
                        trainNumber: trainId,
                        trainName: first.trainName || trainId,
                        trainType: 'Regular',
                        source: first.stationName || first.stationId,
                        destination: last.stationName || last.stationId,
                        departureTime: `${today}T${first.timeOut || '00:00:00'}`,
                        arrivalTime: `${today}T${last.timeIn || '00:00:00'}`,
                        availableSeats: 50,
                        totalSeats: 200,
                        fare: 100000
                    });
                });

                if (apiTrains.length > 0) {
                    allTrains = apiTrains;
                    loadingSpinner.style.display = 'none';
                    displayTrains(allTrains);
                    return;
                }
            }
        } catch (e) {
            console.warn('Failed to fetch schedules', e);
        }

        // If schedules approach failed, try admin trains endpoint
        try {
            const resp = await fetch(`${API_BASE_URL}/admin/trains`);
            if (resp.ok) {
                const data = await resp.json();
                const trains = data.trains || [];
                allTrains = trains.map(t => ({
                    id: t.trainId,
                    trainNumber: t.trainId,
                    trainName: t.trainName,
                    trainType: 'Regular',
                    source: 'Unknown',
                    destination: 'Unknown',
                    departureTime: new Date().toISOString(),
                    arrivalTime: new Date(Date.now()+3*3600*1000).toISOString(),
                    availableSeats: 50,
                    totalSeats: 200,
                    fare: 100000
                }));
                loadingSpinner.style.display = 'none';
                displayTrains(allTrains);
                return;
            }
        } catch (e) {
            console.warn('Failed to fetch admin trains', e);
        }

        // If all API attempts failed, fallback to demo
        console.warn('API returned no usable train data, using demo data');
        allTrains = DEMO_TRAINS.slice();
        loadingSpinner.style.display = 'none';
        displayTrains(allTrains);
    } catch (error) {
        console.error('Error loading trains:', error);
        loadingSpinner.style.display = 'none';
        // Fallback to demo trains on error
        allTrains = DEMO_TRAINS.slice();
        displayTrains(allTrains);
    }
}

// Filter trains based on user input
function filterTrains() {
    const source = document.getElementById('filterSource').value.trim().toLowerCase();
    const destination = document.getElementById('filterDestination').value.trim().toLowerCase();
    const date = document.getElementById('filterDate').value;
    const type = document.getElementById('filterType').value;
    
    let filtered = allTrains.filter(train => {
        let matches = true;
        
        if (source && !train.source.toLowerCase().includes(source)) {
            matches = false;
        }
        
        if (destination && !train.destination.toLowerCase().includes(destination)) {
            matches = false;
        }
        
        if (type && train.trainType !== type) {
            matches = false;
        }
        
        // Note: Date filtering would require backend support to filter by actual dates
        // For now, we just filter by source, destination, and type
        
        return matches;
    });
    
    displayTrains(filtered);
}

// Sort trains
function sortTrains(criteria) {
    let sorted = [...allTrains];
    
    switch(criteria) {
        case 'departure':
            sorted.sort((a, b) => new Date(a.departureTime) - new Date(b.departureTime));
            break;
        case 'fare':
            sorted.sort((a, b) => a.fare - b.fare);
            break;
        case 'fare-high':
            sorted.sort((a, b) => b.fare - a.fare);
            break;
        case 'duration':
            sorted.sort((a, b) => {
                const durationA = new Date(a.arrivalTime) - new Date(a.departureTime);
                const durationB = new Date(b.arrivalTime) - new Date(b.departureTime);
                return durationA - durationB;
            });
            break;
    }
    
    displayTrains(sorted);
}

// Display trains in cards
function displayTrains(trains) {
    const trainsList = document.getElementById('trainsList');
    const noResults = document.getElementById('noResults');
    
    if (trains.length === 0) {
        trainsList.innerHTML = '';
        noResults.style.display = 'block';
        return;
    }
    
    noResults.style.display = 'none';
    
    trainsList.innerHTML = trains.map((train, index) => {
        const departureDate = new Date(train.departureTime);
        const arrivalDate = new Date(train.arrivalTime);
        const duration = calculateDuration(departureDate, arrivalDate);
        
        return `
            <div class="train-ticket-card" style="animation-delay: ${index * 0.05}s">
                <div class="train-header">
                    <div class="train-number">
                        <span class="number">${train.trainNumber}</span>
                    </div>
                    <div class="train-name">${train.trainName}</div>
                </div>
                <div class="train-route">
                    <div class="station departure">
                        <div class="station-name">${train.source}</div>
                        <div class="time">${formatTime(departureDate)}</div>
                        <div class="date">${formatDate(departureDate)}</div>
                    </div>
                    <div class="station arrival">
                        <div class="station-name">${train.destination}</div>
                        <div class="time">${formatTime(arrivalDate)}</div>
                        <div class="date">${formatDate(arrivalDate)}</div>
                    </div>
                </div>
                <div class="train-details">
                    <div class="detail-item">
                        <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M10 2a8 8 0 100 16 8 8 0 000-16zM9 5h2v5H9V5zm0 6h2v2H9v-2z"/>
                        </svg>
                        <span>Available: ${train.availableSeats} seats</span>
                    </div>
                    <div class="detail-item">
                        <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M4 4a2 2 0 00-2 2v8a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2H4zm0 2h12v8H4V6z"/>
                        </svg>
                        <span>Total: ${train.totalSeats} seats</span>
                    </div>
                </div>
                <div class="train-footer">
                    <div class="fare-info">
                        <span class="label">Fare per seat</span>
                        <span class="amount">₹${train.fare.toFixed(2)}</span>
                    </div>
                    <button class="btn-book" onclick="goToBooking(${train.id})" 
                            ${train.availableSeats === 0 ? 'disabled' : ''}>
                        ${train.availableSeats === 0 ? 'Sold Out' : 'Book Now'}
                    </button>
                </div>
            </div>
        `;
    }).join('');
}

// Calculate journey duration
function calculateDuration(departure, arrival) {
    const diff = arrival - departure;
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    
    if (hours > 0) {
        return `${hours}h ${minutes}m`;
    }
    return `${minutes}m`;
}

// Format time (HH:MM)
function formatTime(date) {
    return date.toLocaleTimeString('en-US', { 
        hour: '2-digit', 
        minute: '2-digit',
        hour12: true 
    });
}

// Format date (DD MMM YYYY)
function formatDate(date) {
    return date.toLocaleDateString('en-US', { 
        day: '2-digit',
        month: 'short',
        year: 'numeric'
    });
}

// Open booking modal
// Redirect to booking page
function goToBooking(trainId) {
    window.location.href = `booking.html?trainId=${trainId}`;
}

function openBookingModal(trainId) {
    selectedTrain = allTrains.find(t => t.id === trainId);
    
    if (!selectedTrain) {
        alert('Train not found!');
        return;
    }
    
    // Check if user is logged in
    const token = localStorage.getItem('token');
    if (!token) {
        alert('Please login to book tickets');
        window.location.href = 'login.html';
        return;
    }
    
    // Populate modal with train details
    const modalDetails = document.getElementById('modalTrainDetails');
    const departureDate = new Date(selectedTrain.departureTime);
    const arrivalDate = new Date(selectedTrain.arrivalTime);
    
    modalDetails.innerHTML = `
        <div class="modal-train-info">
            <h3>${selectedTrain.trainName}</h3>
            <p class="train-number">${selectedTrain.trainNumber} - ${selectedTrain.trainType}</p>
            <div class="route-info">
                <div class="route-station">
                    <strong>${selectedTrain.source}</strong>
                    <span>${formatTime(departureDate)}, ${formatDate(departureDate)}</span>
                </div>
                <div class="arrow">→</div>
                <div class="route-station">
                    <strong>${selectedTrain.destination}</strong>
                    <span>${formatTime(arrivalDate)}, ${formatDate(arrivalDate)}</span>
                </div>
            </div>
        </div>
    `;
    
    // Reset form
    document.getElementById('bookingForm').reset();
    document.getElementById('numberOfSeats').value = 1;
    
    // Update fare display
    document.getElementById('baseFare').textContent = `₹${selectedTrain.fare.toFixed(2)}`;
    document.getElementById('seatsCount').textContent = '1';
    document.getElementById('totalFare').textContent = `₹${selectedTrain.fare.toFixed(2)}`;
    
    // Show modal
    document.getElementById('bookingModal').style.display = 'flex';
}

// Close booking modal
function closeBookingModal() {
    document.getElementById('bookingModal').style.display = 'none';
    selectedTrain = null;
}

// Update total fare based on number of seats
function updateTotalFare() {
    if (!selectedTrain) return;
    
    const seats = parseInt(document.getElementById('numberOfSeats').value) || 1;
    const total = selectedTrain.fare * seats;
    
    document.getElementById('seatsCount').textContent = seats;
    document.getElementById('totalFare').textContent = `₹${total.toFixed(2)}`;
}

// Handle booking form submission
async function handleBookingSubmit(e) {
    e.preventDefault();
    
    if (!selectedTrain) {
        alert('No train selected!');
        return;
    }
    
    const token = localStorage.getItem('token');
    if (!token) {
        alert('Please login to book tickets');
        window.location.href = 'login.html';
        return;
    }
    
    // Get form data
    const bookingData = {
        trainId: selectedTrain.id,
        passengerName: document.getElementById('passengerName').value.trim(),
        passengerAge: parseInt(document.getElementById('passengerAge').value),
        passengerGender: document.getElementById('passengerGender').value,
        numberOfSeats: parseInt(document.getElementById('numberOfSeats').value),
        bookingDate: new Date(selectedTrain.departureTime).toISOString().split('T')[0]
    };
    
    // Validate
    if (!bookingData.passengerName || !bookingData.passengerAge || !bookingData.passengerGender) {
        alert('Please fill in all required fields');
        return;
    }
    
    if (bookingData.numberOfSeats > selectedTrain.availableSeats) {
        alert(`Only ${selectedTrain.availableSeats} seats available!`);
        return;
    }
    
    try {
        const submitBtn = e.target.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Processing...';
        
        if (!API_ENABLED) {
            // Demo booking response
            const bookingReference = 'BK' + Date.now().toString().slice(-6);
            alert(`Demo booking confirmed! Reference: ${bookingReference}`);
            closeBookingModal();
            window.location.href = 'bookings.html';
            return;
        }

        const response = await fetch(`${API_BASE_URL}/bookings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(bookingData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Booking failed');
        }
        
        const booking = await response.json();
        
        // Show success message
        alert(`Booking confirmed! Your booking reference is: ${booking.bookingReference}`);
        
        // Close modal and redirect to bookings page
        closeBookingModal();
        window.location.href = 'bookings.html';
        
    } catch (error) {
        console.error('Booking error:', error);
        alert(`Booking failed: ${error.message}`);
        
        const submitBtn = e.target.querySelector('button[type="submit"]');
        submitBtn.disabled = false;
        submitBtn.textContent = 'Confirm Booking';
    }
}
