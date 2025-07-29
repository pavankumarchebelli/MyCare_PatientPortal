package com.mycare.mycare_labcorp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "labcorp_availability", schema = "labcorpschema")
@NoArgsConstructor @AllArgsConstructor
public class LabcorpAvailability {
    @Id
    private UUID availabilityId = UUID.randomUUID();

    private UUID locationId;
    private LocalDate date;
    private boolean morningSlot;
    private boolean afternoonSlot;
    private boolean eveningSlot;

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

