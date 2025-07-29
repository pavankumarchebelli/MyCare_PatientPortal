package com.mycare.mycare_patient.repository;


import com.mycare.mycare_patient.entity.PatientAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientAddressRepository extends JpaRepository<PatientAddress, UUID> {
    PatientAddress findByPatientId(UUID patientId);
}
