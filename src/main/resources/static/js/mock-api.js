// Mock API - Chạy frontend hoàn toàn không cần backend
// Để bật mock mode, set MOCK_MODE = true
// Để dùng real backend, set MOCK_MODE = false

const MOCK_MODE = true; // ⬅️ Set = false khi có backend thật

// Mock data storage (giả lập database)
const MOCK_DATA = {
    users: [
        {
            id: 1,
            username: 'admin',
            password: 'admin123',
            email: 'admin@railway.com',
            fullName: 'Administrator',
            phoneNumber: '0901234567',
            roles: ['ADMIN', 'USER'],
            enabled: true
        },
        {
            id: 2,
            username: 'user',
            password: 'user123',
            email: 'user@gmail.com',
            fullName: 'John Doe',
            phoneNumber: '0909876543',
            roles: ['USER'],
            enabled: true
        }
    ],
    
    trains: [
        {
            id: 1,
            trainNumber: 'SE1',
            trainName: 'Thống Nhất',
            source: 'Hà Nội',
            destination: 'TP. Hồ Chí Minh',
            departureTime: '2025-11-15T06:00:00',
            arrivalTime: '2025-11-16T04:00:00',
            trainType: 'Express',
            fare: 1500000,
            availableSeats: 45,
            totalSeats: 100,
            isActive: true
        },
        {
            id: 2,
            trainNumber: 'SE2',
            trainName: 'Thống Nhất',
            source: 'TP. Hồ Chí Minh',
            destination: 'Hà Nội',
            departureTime: '2025-11-15T18:00:00',
            arrivalTime: '2025-11-16T16:00:00',
            trainType: 'Express',
            fare: 1500000,
            availableSeats: 52,
            totalSeats: 100,
            isActive: true
        },
        {
            id: 3,
            trainNumber: 'SE3',
            trainName: 'Thống Nhất',
            source: 'Hà Nội',
            destination: 'Đà Nẵng',
            departureTime: '2025-11-15T08:30:00',
            arrivalTime: '2025-11-15T22:30:00',
            trainType: 'Express',
            fare: 800000,
            availableSeats: 30,
            totalSeats: 80,
            isActive: true
        },
        {
            id: 4,
            trainNumber: 'SE4',
            trainName: 'Thống Nhất',
            source: 'Đà Nẵng',
            destination: 'Hà Nội',
            departureTime: '2025-11-15T09:00:00',
            arrivalTime: '2025-11-15T23:00:00',
            trainType: 'Express',
            fare: 800000,
            availableSeats: 28,
            totalSeats: 80,
            isActive: true
        },
        {
            id: 5,
            trainNumber: 'SE5',
            trainName: 'Thống Nhất',
            source: 'Hà Nội',
            destination: 'Nha Trang',
            departureTime: '2025-11-16T07:00:00',
            arrivalTime: '2025-11-17T08:00:00',
            trainType: 'Express',
            fare: 1200000,
            availableSeats: 40,
            totalSeats: 90,
            isActive: true
        }
    ],
    
    bookings: [
        {
            id: 1,
            bookingReference: 'BK001234',
            userId: 2,
            trainId: 1,
            numberOfSeats: 2,
            passengerName: 'John Doe',
            passengerAge: 30,
            passengerGender: 'Male',
            totalFare: 3000000,
            status: 'CONFIRMED',
            bookingDate: '2025-11-10T14:30:00',
            seatNumbers: 'A1, A2'
        },
        {
            id: 2,
            bookingReference: 'BK001235',
            userId: 2,
            trainId: 3,
            numberOfSeats: 1,
            passengerName: 'Jane Smith',
            passengerAge: 25,
            passengerGender: 'Female',
            totalFare: 800000,
            status: 'CONFIRMED',
            bookingDate: '2025-11-09T10:15:00',
            seatNumbers: 'B5'
        }
    ],
    
    currentUser: null,
    nextBookingId: 3
};

// Mock API Functions
const MockAPI = {
    // Simulate network delay
    delay: (ms = 500) => new Promise(resolve => setTimeout(resolve, ms)),
    
    // Auth APIs
    login: async (username, password) => {
        await MockAPI.delay();
        
        const user = MOCK_DATA.users.find(u => 
            u.username === username && u.password === password
        );
        
        if (user) {
            const { password, ...userWithoutPassword } = user;
            MOCK_DATA.currentUser = userWithoutPassword;
            
            return {
                success: true,
                data: {
                    token: 'mock-jwt-token-' + Date.now(),
                    user: userWithoutPassword
                }
            };
        }
        
        return {
            success: false,
            error: 'Invalid username or password'
        };
    },
    
    register: async (userData) => {
        await MockAPI.delay();
        
        // Check if username exists
        if (MOCK_DATA.users.find(u => u.username === userData.username)) {
            return {
                success: false,
                error: 'Username already exists'
            };
        }
        
        // Check if email exists
        if (MOCK_DATA.users.find(u => u.email === userData.email)) {
            return {
                success: false,
                error: 'Email already exists'
            };
        }
        
        const newUser = {
            id: MOCK_DATA.users.length + 1,
            ...userData,
            roles: ['USER'],
            enabled: true
        };
        
        MOCK_DATA.users.push(newUser);
        
        return {
            success: true,
            data: {
                message: 'Registration successful',
                userId: newUser.id
            }
        };
    },
    
    logout: async () => {
        await MockAPI.delay(200);
        MOCK_DATA.currentUser = null;
        return { success: true };
    },
    
    // Train APIs
    getAllTrains: async () => {
        await MockAPI.delay();
        return {
            success: true,
            data: MOCK_DATA.trains
        };
    },
    
    searchTrains: async (source, destination, date) => {
        await MockAPI.delay();
        
        let results = MOCK_DATA.trains.filter(train => {
            const matchSource = !source || train.source.toLowerCase().includes(source.toLowerCase());
            const matchDest = !destination || train.destination.toLowerCase().includes(destination.toLowerCase());
            return matchSource && matchDest && train.isActive;
        });
        
        return {
            success: true,
            data: results
        };
    },
    
    getTrainById: async (id) => {
        await MockAPI.delay();
        const train = MOCK_DATA.trains.find(t => t.id === parseInt(id));
        
        if (train) {
            return { success: true, data: train };
        }
        return { success: false, error: 'Train not found' };
    },
    
    // Booking APIs
    createBooking: async (bookingData) => {
        await MockAPI.delay();
        
        if (!MOCK_DATA.currentUser) {
            return { success: false, error: 'Please login first' };
        }
        
        const train = MOCK_DATA.trains.find(t => t.id === bookingData.trainId);
        if (!train) {
            return { success: false, error: 'Train not found' };
        }
        
        if (train.availableSeats < bookingData.numberOfSeats) {
            return { success: false, error: 'Not enough seats available' };
        }
        
        const newBooking = {
            id: MOCK_DATA.nextBookingId++,
            bookingReference: 'BK' + Date.now().toString().slice(-6),
            userId: MOCK_DATA.currentUser.id,
            trainId: bookingData.trainId,
            numberOfSeats: bookingData.numberOfSeats,
            passengerName: bookingData.passengerName,
            passengerAge: bookingData.passengerAge,
            passengerGender: bookingData.passengerGender,
            totalFare: train.fare * bookingData.numberOfSeats,
            status: 'CONFIRMED',
            bookingDate: new Date().toISOString(),
            seatNumbers: 'A' + MOCK_DATA.nextBookingId
        };
        
        MOCK_DATA.bookings.push(newBooking);
        train.availableSeats -= bookingData.numberOfSeats;
        
        return {
            success: true,
            data: newBooking
        };
    },
    
    getMyBookings: async () => {
        await MockAPI.delay();
        
        if (!MOCK_DATA.currentUser) {
            return { success: false, error: 'Please login first' };
        }
        
        const userBookings = MOCK_DATA.bookings
            .filter(b => b.userId === MOCK_DATA.currentUser.id)
            .map(booking => {
                const train = MOCK_DATA.trains.find(t => t.id === booking.trainId);
                return { ...booking, train };
            });
        
        return {
            success: true,
            data: userBookings
        };
    },
    
    cancelBooking: async (id) => {
        await MockAPI.delay();
        
        const booking = MOCK_DATA.bookings.find(b => b.id === parseInt(id));
        if (!booking) {
            return { success: false, error: 'Booking not found' };
        }
        
        booking.status = 'CANCELLED';
        
        // Return seats to train
        const train = MOCK_DATA.trains.find(t => t.id === booking.trainId);
        if (train) {
            train.availableSeats += booking.numberOfSeats;
        }
        
        return {
            success: true,
            data: { message: 'Booking cancelled successfully' }
        };
    },
    
    // Admin APIs
    getAdminStats: async () => {
        await MockAPI.delay();
        
        const totalBookings = MOCK_DATA.bookings.length;
        const totalUsers = MOCK_DATA.users.length;
        const totalTrains = MOCK_DATA.trains.filter(t => t.isActive).length;
        const cancelledBookings = MOCK_DATA.bookings.filter(b => b.status === 'CANCELLED').length;
        
        return {
            success: true,
            data: {
                totalBookings,
                totalUsers,
                totalTrains,
                cancelledBookings
            }
        };
    },
    
    getAllBookings: async () => {
        await MockAPI.delay();
        
        const bookingsWithDetails = MOCK_DATA.bookings.map(booking => {
            const user = MOCK_DATA.users.find(u => u.id === booking.userId);
            const train = MOCK_DATA.trains.find(t => t.id === booking.trainId);
            return { ...booking, user, train };
        });
        
        return {
            success: true,
            data: bookingsWithDetails
        };
    },
    
    getAllUsers: async () => {
        await MockAPI.delay();
        
        const usersWithoutPasswords = MOCK_DATA.users.map(({ password, ...user }) => user);
        
        return {
            success: true,
            data: usersWithoutPasswords
        };
    },
    
    updateBookingStatus: async (id, status) => {
        await MockAPI.delay();
        
        const booking = MOCK_DATA.bookings.find(b => b.id === parseInt(id));
        if (!booking) {
            return { success: false, error: 'Booking not found' };
        }
        
        booking.status = status;
        
        return {
            success: true,
            data: { message: 'Booking status updated successfully' }
        };
    }
};

// Override apiCall function khi MOCK_MODE = true
if (MOCK_MODE) {
    console.log('🎭 MOCK MODE ENABLED - Using fake data, no backend needed');
    console.log('📝 Test accounts:');
    console.log('   Admin: username="admin", password="admin123"');
    console.log('   User:  username="user", password="user123"');
    
    // Override apiCall function
    window.apiCall = async (endpoint, options = {}) => {
        console.log('📡 Mock API Call:', endpoint, options);
        
        const method = options.method || 'GET';
        const body = options.body ? JSON.parse(options.body) : null;
        
        // Parse endpoint
        let endpointPath = typeof endpoint === 'string' ? endpoint : endpoint.path;
        if (typeof endpoint === 'object' && endpoint.params) {
            Object.keys(endpoint.params).forEach(key => {
                endpointPath = endpointPath.replace(`{${key}}`, endpoint.params[key]);
            });
        }
        
        // Route to appropriate mock function
        try {
            // Auth endpoints
            if (endpointPath.includes('/auth/login') && method === 'POST') {
                return await MockAPI.login(body.username, body.password);
            }
            
            if (endpointPath.includes('/auth/register') && method === 'POST') {
                return await MockAPI.register(body);
            }
            
            if (endpointPath.includes('/auth/logout')) {
                return await MockAPI.logout();
            }
            
            // Train endpoints
            if (endpointPath.includes('/trains/search')) {
                const url = new URL('http://dummy.com' + endpointPath);
                const source = url.searchParams.get('source');
                const destination = url.searchParams.get('destination');
                const date = url.searchParams.get('date');
                return await MockAPI.searchTrains(source, destination, date);
            }
            
            if (endpointPath.match(/\/trains\/\d+$/)) {
                const id = endpointPath.split('/').pop();
                return await MockAPI.getTrainById(id);
            }
            
            if (endpointPath.includes('/trains')) {
                return await MockAPI.getAllTrains();
            }
            
            // Booking endpoints
            if (endpointPath.includes('/bookings/my-bookings')) {
                return await MockAPI.getMyBookings();
            }
            
            if (endpointPath.match(/\/bookings\/\d+\/cancel/) && method === 'PUT') {
                const id = endpointPath.match(/\/bookings\/(\d+)\/cancel/)[1];
                return await MockAPI.cancelBooking(id);
            }
            
            if (endpointPath.includes('/bookings') && method === 'POST') {
                return await MockAPI.createBooking(body);
            }
            
            // Admin endpoints
            if (endpointPath.includes('/admin/stats')) {
                return await MockAPI.getAdminStats();
            }
            
            if (endpointPath.includes('/admin/bookings')) {
                return await MockAPI.getAllBookings();
            }
            
            if (endpointPath.includes('/admin/users')) {
                return await MockAPI.getAllUsers();
            }
            
            if (endpointPath.match(/\/admin\/bookings\/\d+\/status/) && method === 'PUT') {
                const id = endpointPath.match(/\/admin\/bookings\/(\d+)\/status/)[1];
                return await MockAPI.updateBookingStatus(id, body.status);
            }
            
            // Default: not found
            return {
                success: false,
                error: 'Mock endpoint not implemented: ' + endpointPath
            };
            
        } catch (error) {
            console.error('Mock API Error:', error);
            return {
                success: false,
                error: error.message
            };
        }
    };
}
