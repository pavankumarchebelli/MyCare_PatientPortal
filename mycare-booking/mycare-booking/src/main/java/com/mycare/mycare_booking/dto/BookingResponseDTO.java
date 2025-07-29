package com.mycare.mycare_booking.dto;

import com.mycare.mycare_booking.entity.Booking;
import java.util.UUID;

public class BookingResponseDTO {
    private UUID bookingId;
    private String slot;
    private String bookingDate;

    private PatientAddressDTO patient;
    private LabcorpLocationDTO lab;

    // Getters and Setters
    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public PatientAddressDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientAddressDTO patient) {
        this.patient = patient;
    }

    public LabcorpLocationDTO getLab() {
        return lab;
    }

    public void setLab(LabcorpLocationDTO lab) {
        this.lab = lab;
    }
}
