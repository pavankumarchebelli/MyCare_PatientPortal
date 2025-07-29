package com.mycare.mycare_patient.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patient_availability", schema = "patientschema")
@NoArgsConstructor @AllArgsConstructor
public class PatientAvailability {
    @Id
    private UUID availabilityId = UUID.randomUUID();

    private UUID patientId;
    private LocalDate date;
    private String preferredSlot; // e.g., MORNING, AFTERNOON, EVENING

    public UUID getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(UUID availabilityId) {
        this.availabilityId = availabilityId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPreferredSlot() {
        return preferredSlot;
    }

    public void setPreferredSlot(String preferredSlot) {
        this.preferredSlot = preferredSlot;
    }
}
