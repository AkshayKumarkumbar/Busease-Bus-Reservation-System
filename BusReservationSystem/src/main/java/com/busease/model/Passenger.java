package com.busease.model;

import javax.persistence.*;

/**
 * Simplified Passenger model for student projects.
 * Contains basic passenger information for bus bookings.
 */
@Entity
@Table(name = "passenger")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Gender field, not required for simplified approach
    private String gender;

    // Age field, defaulting to 0 when not provided
    private int age;

    // Added email and phone for contact information
    private String email;
    
    private String phone;

    @Column(name = "seat_number")
    private String seatNumber;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // Constructors
    public Passenger() {
    }

    public Passenger(String name, String email, String phone, String seatNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.seatNumber = seatNumber;
    }
    
    // Full constructor
    public Passenger(String name, String gender, int age, String email, String phone, String seatNumber) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.seatNumber = seatNumber;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}