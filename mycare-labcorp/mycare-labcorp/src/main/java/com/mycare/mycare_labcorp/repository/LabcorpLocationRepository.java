package com.mycare.mycare_labcorp.repository;

import com.mycare.mycare_labcorp.entity.LabcorpLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LabcorpLocationRepository extends JpaRepository<LabcorpLocation, UUID> {
}