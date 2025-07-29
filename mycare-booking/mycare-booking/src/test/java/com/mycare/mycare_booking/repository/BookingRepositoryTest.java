package com.mycare.mycare_booking.repository;

import com.mycare.mycare_booking.entity.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void testSaveBooking() {
        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID());
        booking.setPatientId(UUID.randomUUID());
        booking.setLocationId(UUID.randomUUID());
        booking.setBookingDate(LocalDate.now());
        booking.setSlot("MORNING");

        Booking saved = bookingRepository.save(booking);
        assertNotNull(saved);
        assertEquals("MORNING", saved.getSlot());
    }
}