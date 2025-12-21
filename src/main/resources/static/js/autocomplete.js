// Autocomplete functionality for station search

// Default station list for offline/demo mode
const DEFAULT_STATIONS = [
  { stationId: 'HN', name: 'Hanoi' },
  { stationId: 'HCM', name: 'Ho Chi Minh City' },
  { stationId: 'DN', name: 'Da Nang' },
  { stationId: 'HUE', name: 'Hue' },
  { stationId: 'HP', name: 'Hai Phong' },
  { stationId: 'NT', name: 'Nha Trang' },
  { stationId: 'CT', name: 'Can Tho' },
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
      if (
        !this.input.contains(e.target) &&
        !this.suggestionsBox.contains(e.target)
      ) {
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

      const API_BASE_URL =
        typeof CONFIG !== 'undefined'
          ? CONFIG.API_BASE_URL
          : 'http://localhost:8081/api';
      const response = await fetch(`${API_BASE_URL}/stations`);

      if (response.ok) {
        this.stations = await response.json();
      } else {
        console.warn('Failed to load stations, using defaults');
        this.stations = DEFAULT_STATIONS;
      }
    } catch (error) {
      console.error('Error fetching stations:', error);
      this.stations = DEFAULT_STATIONS;
    }
  }

  filterSuggestions(query) {
    if (!query) return this.stations;

    const q = query.toLowerCase();
    return this.stations.filter(
      (station) =>
        station.name.toLowerCase().includes(q) ||
        station.stationId.toLowerCase().includes(q)
    );
  }

  async handleInput(e) {
    const query = e.target.value.trim();

    if (query.length === 0) {
      this.showSuggestions(this.stations);
    } else {
      const suggestions = this.filterSuggestions(query);
      this.showSuggestions(suggestions, query);
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

    switch (e.key) {
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

  highlightMatch(text, query) {
    if (!query) return text;
    const regex = new RegExp(`(${query})`, 'gi');
    return text.replace(regex, '<strong>$1</strong>');
  }

  showSuggestions(suggestions, query = '') {
    if (!suggestions || suggestions.length === 0) {
      this.suggestionsBox.innerHTML =
        '<div class="suggestion-item no-results">No stations found</div>';
      this.suggestionsBox.style.display = 'block';
      return;
    }

    this.selectedIndex = -1;
    this.suggestionsBox.innerHTML = '';

    suggestions.forEach((station) => {
      const item = document.createElement('div');
      item.className = 'suggestion-item';

      const stationName = document.createElement('span');
      stationName.className = 'station-name';
      stationName.innerHTML = this.highlightMatch(station.name, query);

      const stationId = document.createElement('span');
      stationId.className = 'station-id';
      stationId.textContent = ' ' + station.stationId;

      item.appendChild(stationName);
      item.appendChild(stationId);

      item.addEventListener('click', () => {
        this.input.value = station.name;
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

  // For times filter inputs
  if (document.getElementById('filterSource')) {
    new StationAutocomplete('filterSource', 'filterSource-suggestions');
  }
  if (document.getElementById('filterDestination')) {
    new StationAutocomplete('filterDestination', 'filterDestination-suggestions');
  }

  // For search page
  if (document.getElementById('searchSource')) {
    new StationAutocomplete('searchSource', 'searchSource-suggestions');
  }
  if (document.getElementById('searchDestination')) {
    new StationAutocomplete(
      'searchDestination',
      'searchDestination-suggestions'
    );
  }
});
