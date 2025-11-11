// Autocomplete functionality for station search

// Default station list for offline/demo mode
const DEFAULT_STATIONS = [
    'Hanoi', 'Ho Chi Minh City', 'Da Nang', 'Hue', 'Hai Phong', 'Nha Trang', 'Can Tho'
];

class StationAutocomplete {
    constructor(inputId, suggestionsId) {
        this.input = document.getElementById(inputId);
        this.suggestionsBox = document.getElementById(suggestionsId);
        this.stations = [];
        this.selectedIndex = -1;
        
        if (this.input && this.suggestionsBox) {
            this.init();
        }
    }

    init() {
        // Fetch all stations on load
        this.fetchAllStations();

        // Add event listeners
        this.input.addEventListener('input', (e) => this.handleInput(e));
        this.input.addEventListener('focus', (e) => this.handleFocus(e));
        this.input.addEventListener('keydown', (e) => this.handleKeyDown(e));
        
        // Close suggestions when clicking outside
        document.addEventListener('click', (e) => {
            if (!this.input.contains(e.target) && !this.suggestionsBox.contains(e.target)) {
                this.hideSuggestions();
            }
        });
    }

    async fetchAllStations() {
        try {
            if (typeof API_ENABLED !== 'undefined' && !API_ENABLED) {
                this.stations = DEFAULT_STATIONS;
                return;
            }
            const response = await fetch('/api/trains/stations');
            if (response.ok) {
                this.stations = await response.json();
            }
        } catch (error) {
            console.error('Error fetching stations:', error);
            this.stations = DEFAULT_STATIONS;
        }
    }

    async fetchSuggestions(query) {
        try {
            if (typeof API_ENABLED !== 'undefined' && !API_ENABLED) {
                const q = query.toLowerCase();
                return DEFAULT_STATIONS.filter(s => s.toLowerCase().includes(q));
            }
            const url = query 
                ? `/api/trains/stations/suggestions?query=${encodeURIComponent(query)}`
                : '/api/trains/stations';
            
            const response = await fetch(url);
            if (response.ok) {
                return await response.json();
            }
        } catch (error) {
            console.error('Error fetching suggestions:', error);
        }
        return [];
    }

    async handleInput(e) {
        const query = e.target.value.trim();
        
        if (query.length === 0) {
            this.showSuggestions(this.stations);
        } else if (query.length >= 1) {
            const suggestions = await this.fetchSuggestions(query);
            this.showSuggestions(suggestions);
        } else {
            this.hideSuggestions();
        }
    }

    handleFocus(e) {
        const query = e.target.value.trim();
        if (query.length === 0) {
            this.showSuggestions(this.stations);
        } else {
            this.handleInput(e);
        }
    }

    handleKeyDown(e) {
        const items = this.suggestionsBox.querySelectorAll('.suggestion-item');
        
        if (items.length === 0) return;

        switch(e.key) {
            case 'ArrowDown':
                e.preventDefault();
                this.selectedIndex = Math.min(this.selectedIndex + 1, items.length - 1);
                this.updateSelection(items);
                break;
            case 'ArrowUp':
                e.preventDefault();
                this.selectedIndex = Math.max(this.selectedIndex - 1, -1);
                this.updateSelection(items);
                break;
            case 'Enter':
                e.preventDefault();
                if (this.selectedIndex >= 0 && items[this.selectedIndex]) {
                    items[this.selectedIndex].click();
                }
                break;
            case 'Escape':
                this.hideSuggestions();
                break;
        }
    }

    updateSelection(items) {
        items.forEach((item, index) => {
            if (index === this.selectedIndex) {
                item.classList.add('active');
                item.scrollIntoView({ block: 'nearest' });
            } else {
                item.classList.remove('active');
            }
        });
    }

    showSuggestions(suggestions) {
        if (!suggestions || suggestions.length === 0) {
            this.hideSuggestions();
            return;
        }

        this.selectedIndex = -1;
        this.suggestionsBox.innerHTML = '';
        
        suggestions.forEach(station => {
            const item = document.createElement('div');
            item.className = 'suggestion-item';
            item.textContent = station;
            
            item.addEventListener('click', () => {
                this.input.value = station;
                this.hideSuggestions();
                this.input.dispatchEvent(new Event('change'));
            });
            
            this.suggestionsBox.appendChild(item);
        });
        
        this.suggestionsBox.style.display = 'block';
    }

    hideSuggestions() {
        this.suggestionsBox.style.display = 'none';
        this.selectedIndex = -1;
    }
}

// Initialize autocomplete when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    // For index page
    if (document.getElementById('source')) {
        new StationAutocomplete('source', 'source-suggestions');
    }
    if (document.getElementById('destination')) {
        new StationAutocomplete('destination', 'destination-suggestions');
    }

    // For search page
    if (document.getElementById('searchSource')) {
        new StationAutocomplete('searchSource', 'searchSource-suggestions');
    }
    if (document.getElementById('searchDestination')) {
        new StationAutocomplete('searchDestination', 'searchDestination-suggestions');
    }
});
