// API Configuration
// 🎭 MOCK_MODE: Set = true để test frontend không cần backend
// 📡 REAL_MODE: Set = false để kết nối với backend thật

// NOTE: MOCK_MODE được define trong mock-api.js
// Nếu mock-api.js được load, apiCall sẽ tự động dùng mock data

const CONFIG = {
    // API Base URL - Sửa thành URL của backend Spring Boot mới
    API_BASE_URL: 'http://localhost:8081/api',
    
    // Các endpoint API
    ENDPOINTS: {
        // Auth
        LOGIN: '/auth/login',
        REGISTER: '/auth/register',
        LOGOUT: '/auth/logout',
        
        // Trains
        TRAINS: '/trains',
        SEARCH_TRAINS: '/trains/search',
        
        // Bookings
        BOOKINGS: '/bookings',
        MY_BOOKINGS: '/bookings/my-bookings',
        CANCEL_BOOKING: '/bookings/{id}/cancel',
        
        // Admin
        ADMIN_STATS: '/admin/stats',
        ADMIN_BOOKINGS: '/admin/bookings',
        ADMIN_USERS: '/admin/users',
        UPDATE_BOOKING_STATUS: '/admin/bookings/{id}/status',
        
        // Users
        USERS: '/users',
        USER_PROFILE: '/users/profile'
    },
    
    // Timeout settings
    REQUEST_TIMEOUT: 30000, // 30 seconds
    
    // Pagination
    DEFAULT_PAGE_SIZE: 10,
    
    // Date format
    DATE_FORMAT: 'YYYY-MM-DD',
    TIME_FORMAT: 'HH:mm'
};

// Helper function để build full URL
function getApiUrl(endpoint, params = {}) {
    let url = CONFIG.API_BASE_URL + endpoint;
    
    // Replace path parameters (e.g., {id})
    Object.keys(params).forEach(key => {
        url = url.replace(`{${key}}`, params[key]);
    });
    
    return url;
}

// Helper function để make API calls với error handling
async function apiCall(endpoint, options = {}) {
    const url = typeof endpoint === 'string' ? endpoint : getApiUrl(endpoint.path, endpoint.params);
    
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        },
        ...options
    };
    
    try {
        const response = await fetch(url, defaultOptions);
        
        // Check if response is ok
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
        }
        
        // Parse JSON response
        const data = await response.json();
        return { success: true, data };
        
    } catch (error) {
        console.error('API Call Error:', error);
        return { success: false, error: error.message };
    }
}

// Export cho các file khác sử dụng
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { CONFIG, getApiUrl, apiCall };
}
