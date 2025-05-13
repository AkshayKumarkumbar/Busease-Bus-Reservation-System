// Main JavaScript file for Busease

document.addEventListener('DOMContentLoaded', function() {
    // Initialize date pickers
    const dateInputs = document.querySelectorAll('input[type="date"]');
    dateInputs.forEach(input => {
        // Set min date to today
        const today = new Date().toISOString().split('T')[0];
        input.setAttribute('min', today);
        
        // If it's a travel date input (for search form)
        if (input.id === 'travelDate') {
            input.value = today;
        }
    });
    
    // Add validation for the search form
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            const origin = document.getElementById('origin').value;
            const destination = document.getElementById('destination').value;
            const travelDate = document.getElementById('travelDate').value;
            
            if (!origin || !destination || !travelDate) {
                event.preventDefault();
                showAlert('Please fill in all fields to search for buses', 'danger');
            }
            
            if (origin === destination) {
                event.preventDefault();
                showAlert('Origin and destination cannot be the same', 'warning');
            }
        });
    }
    
    // Add validation for booking form
    const bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', function(event) {
            const passengers = document.querySelectorAll('.passenger-detail');
            let valid = true;
            
            passengers.forEach(passenger => {
                const inputs = passenger.querySelectorAll('input[required]');
                inputs.forEach(input => {
                    if (!input.value.trim()) {
                        valid = false;
                        input.classList.add('is-invalid');
                    } else {
                        input.classList.remove('is-invalid');
                    }
                });
            });
            
            if (!valid) {
                event.preventDefault();
                showAlert('Please fill in all required passenger details', 'danger');
            }
        });
    }
    
    // Initialize tooltip
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});

// Utility function to show alerts
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer');
    if (alertContainer) {
        const alert = document.createElement('div');
        alert.className = `alert alert-${type} alert-dismissible fade show`;
        alert.role = 'alert';
        alert.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        
        alertContainer.appendChild(alert);
        
        // Auto dismiss after 5 seconds
        setTimeout(() => {
            alert.classList.remove('show');
            setTimeout(() => {
                alertContainer.removeChild(alert);
            }, 150);
        }, 5000);
    }
}

// Function to toggle password visibility
function togglePasswordVisibility(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// Function to calculate and display total fare
function calculateTotalFare() {
    const farePerSeat = parseFloat(document.getElementById('farePerSeat').value);
    const selectedSeatsCount = document.getElementById('selectedSeatsCount').value;
    
    if (!isNaN(farePerSeat) && selectedSeatsCount > 0) {
        const totalFare = farePerSeat * selectedSeatsCount;
        document.getElementById('totalFare').textContent = totalFare.toFixed(2);
    }
}
