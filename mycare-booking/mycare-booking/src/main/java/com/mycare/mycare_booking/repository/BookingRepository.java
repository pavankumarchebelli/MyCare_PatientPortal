package com.mycare.mycare_booking.repository;

import com.mycare.mycare_booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByPatientId(UUID patientId);
}
