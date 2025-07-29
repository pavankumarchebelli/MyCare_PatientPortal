package com.mycare.mycare_labcorp.repository;

import com.mycare.mycare_labcorp.entity.LabcorpAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LabcorpAvailabilityRepository extends JpaRepository<LabcorpAvailability, UUID> {
    List<LabcorpAvailability> findByDateAndMorningSlotTrue(LocalDate date);
    List<LabcorpAvailability> findByDateAndAfternoonSlotTrue(LocalDate date);
    List<LabcorpAvailability> findByDateAndEveningSlotTrue(LocalDate date);
}