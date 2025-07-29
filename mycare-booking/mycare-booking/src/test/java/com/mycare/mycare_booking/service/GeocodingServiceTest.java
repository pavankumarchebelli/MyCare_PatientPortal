package com.mycare.mycare_booking.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeocodingServiceTest {

    private final GeocodingService geocodingService = new GeocodingService();

    @Test
    public void testValidAddressReturnsCoordinates() {
        double[] coords = geocodingService.getLatLongFromAddress("New York, NY");
        assertTrue(coords[0] != 0.0 && coords[1] != 0.0, "Coordinates should not be zero for valid address");
    }

    @Test
    public void testInvalidAddressReturnsFallback() {
        double[] coords = geocodingService.getLatLongFromAddress("Fake place 00000");
        assertEquals(0.0, coords[0]);
        assertEquals(0.0, coords[1]);
    }
}