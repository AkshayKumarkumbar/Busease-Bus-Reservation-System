// JavaScript for seat selection

document.addEventListener('DOMContentLoaded', function() {
    const seatMap = document.getElementById('seatMap');
    const selectedSeatsInput = document.getElementById('selectedSeatsInput');
    const selectedSeatsDisplay = document.getElementById('selectedSeatsDisplay');
    const continueBtn = document.getElementById('continueBtn');
    const seatCountDisplay = document.getElementById('seatCountDisplay');
    
    if (!seatMap) return;
    
    let selectedSeats = [];
    
    // Get booked seats from data attribute
    const bookedSeatsElement = document.getElementById('bookedSeats');
    let bookedSeats = [];
    if (bookedSeatsElement && bookedSeatsElement.dataset.seats) {
        bookedSeats = bookedSeatsElement.dataset.seats.split(',');
    }
    
    // Initialize seats
    initializeSeats();
    
    function initializeSeats() {
        const totalSeats = parseInt(seatMap.dataset.totalSeats || 40);
        const seatsPerRow = 4;
        
        for (let i = 1; i <= totalSeats; i++) {
            const seat = document.createElement('div');
            const seatNumber = i.toString();
            seat.className = 'seat';
            seat.dataset.seatNumber = seatNumber;
            seat.textContent = seatNumber;
            
            // Mark booked seats
            if (bookedSeats.includes(seatNumber)) {
                seat.classList.add('booked');
                seat.title = 'Already booked';
            } else {
                seat.addEventListener('click', function() {
                    toggleSeatSelection(seat);
                });
            }
            
            seatMap.appendChild(seat);
        }
    }
    
    function toggleSeatSelection(seat) {
        const seatNumber = seat.dataset.seatNumber;
        
        if (seat.classList.contains('selected')) {
            // Deselect the seat
            seat.classList.remove('selected');
            const index = selectedSeats.indexOf(seatNumber);
            if (index > -1) {
                selectedSeats.splice(index, 1);
            }
        } else {
            // Select the seat
            seat.classList.add('selected');
            selectedSeats.push(seatNumber);
        }
        
        // Update display and form values
        updateSelectedSeatsDisplay();
    }
    
    function updateSelectedSeatsDisplay() {
        selectedSeats.sort((a, b) => parseInt(a) - parseInt(b));
        selectedSeatsInput.value = selectedSeats.join(',');
        
        if (selectedSeats.length > 0) {
            selectedSeatsDisplay.textContent = selectedSeats.join(', ');
            seatCountDisplay.textContent = selectedSeats.length;
            continueBtn.disabled = false;
        } else {
            selectedSeatsDisplay.textContent = 'None';
            seatCountDisplay.textContent = '0';
            continueBtn.disabled = true;
        }
        
        // If we have a total fare element, update it
        const farePerSeatElement = document.getElementById('farePerSeat');
        const totalFareElement = document.getElementById('totalFare');
        
        if (farePerSeatElement && totalFareElement) {
            const farePerSeat = parseFloat(farePerSeatElement.value);
            const totalFare = farePerSeat * selectedSeats.length;
            totalFareElement.textContent = totalFare.toFixed(2);
        }
    }
});
