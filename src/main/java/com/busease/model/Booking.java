package com.busease.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number", nullable = false, unique = true)
    private String bookingNumber;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private String status;  // Confirmed, Cancelled, Pending

    @Column(name = "total_fare", nullable = false)
    private double totalFare;

    @Column(name = "seat_count", nullable = false)
    private int seatCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
    
    @Column(name = "selected_seats")
    private String selectedSeats; // Comma-separated seat numbers

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Passenger> passengers = new ArrayList<>();

    // Constructors
    public Booking() {
    }

    public Booking(String bookingNumber, LocalDateTime bookingDate, String status, 
                   double totalFare, int seatCount, User user, Route route) {
        this.bookingNumber = bookingNumber;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalFare = totalFare;
        this.seatCount = seatCount;
        this.user = user;
        this.route = route;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
    
    public String getSelectedSeats() {
        return selectedSeats;
    }
    
    public void setSelectedSeats(String selectedSeats) {
        this.selectedSeats = selectedSeats;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
        
        // Set the booking for each passenger to maintain bidirectional relationship
        if (passengers != null) {
            for (Passenger passenger : passengers) {
                passenger.setBooking(this);
            }
        }
    }

    // Helper methods
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.setBooking(this);
    }

    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        passenger.setBooking(null);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingNumber='" + bookingNumber + '\'' +
                ", bookingDate=" + bookingDate +
                ", status='" + status + '\'' +
                ", totalFare=" + totalFare +
                ", seatCount=" + seatCount +
                '}';
    }
}