package com.mycare.mycare_booking.dto;

import java.time.LocalDate;
import java.util.UUID;

public class LabcorpAvailabilityDTO {
    private UUID availabilityId;
    private UUID locationId;
    private LocalDate date;
    private boolean morningSlot;
    private boolean afternoonSlot;
    private boolean eveningSlot;

    public LabcorpAvailabilityDTO() {
    }

    public LabcorpAvailabilityDTO(UUID availabilityId, UUID locationId, LocalDate date, boolean morningSlot, boolean afternoonSlot, boolean eveningSlot) {
        this.availabilityId = availabilityId;
        this.locationId = locationId;
        this.date = date;
        this.morningSlot = morningSlot;
        this.afternoonSlot = afternoonSlot;
        this.eveningSlot = eveningSlot;
    }

    public UUID getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(UUID availabilityId) {
        this.availabilityId = availabilityId;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isMorningSlot() {
        return morningSlot;
    }

    public void setMorningSlot(boolean morningSlot) {
        this.morningSlot = morningSlot;
    }

    public boolean isAfternoonSlot() {
        return afternoonSlot;
    }

    public void setAfternoonSlot(boolean afternoonSlot) {
        this.afternoonSlot = afternoonSlot;
    }

    public boolean isEveningSlot() {
        return eveningSlot;
    }

    public void setEveningSlot(boolean eveningSlot) {
        this.eveningSlot = eveningSlot;
    }
}
