package com.mycare.mycare_booking.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeoUtilsTest {

    @Test
    public void testCalculateDistanceBetweenTwoPoints() {
        double distance = GeoUtils.calculateDistance(40.7128, -74.0060, 40.730610, -73.935242); // NYC
        assertTrue(distance > 0 && distance < 15);
    }
}