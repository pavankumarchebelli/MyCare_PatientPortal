package com.mycare.mycare_patient.repository;

import com.mycare.mycare_patient.entity.PatientAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PatientAvailabilityRepository extends JpaRepository<PatientAvailability, UUID> {
    List<PatientAvailability> findByPatientId(UUID patientId);
}

