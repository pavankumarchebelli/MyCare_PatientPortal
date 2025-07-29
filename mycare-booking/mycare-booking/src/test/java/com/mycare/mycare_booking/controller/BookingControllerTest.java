package com.mycare.mycare_booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycare.mycare_booking.dto.LabcorpAvailabilityDTO;
import com.mycare.mycare_booking.dto.LabcorpLocationDTO;
import com.mycare.mycare_booking.dto.PatientAddressDTO;
import com.mycare.mycare_booking.repository.BookingRepository;
import com.mycare.mycare_booking.service.GeocodingService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private GeocodingService geocodingService;
    @MockBean private RestTemplate restTemplate;
    @MockBean private BookingRepository bookingRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String BASE_URL = "/booking/book-from-address";

    @Test
    void testSuccessfulBooking() throws Exception {
        PatientAddressDTO input = new PatientAddressDTO();
        input.setPatientId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        input.setStreet("123 Main St");
        input.setZipcode("10001");
        input.setState("NY");
        input.setCounty("New York");

        when(geocodingService.getLatLongFromAddress(anyString()))
                .thenReturn(new double[]{40.7128, -74.0060});

        LabcorpLocationDTO location = new LabcorpLocationDTO();
        location.setLocationId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        location.setLatitude(40.7138);
        location.setLongitude(-74.0059);
        location.setLocationName("LabCorp NYC");

        LabcorpAvailabilityDTO availability = new LabcorpAvailabilityDTO();
        availability.setLocationId(location.getLocationId());
        availability.setDate(LocalDate.of(2025, 7, 26));
        availability.setMorningSlot(true);

        when(restTemplate.getForObject(contains("/availability"), eq(LabcorpAvailabilityDTO[].class)))
                .thenReturn(new LabcorpAvailabilityDTO[]{availability});

        when(restTemplate.getForObject(contains("/locations"), eq(LabcorpLocationDTO[].class)))
                .thenReturn(new LabcorpLocationDTO[]{location});

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .param("date", "2025-07-26")
                        .param("slot", "MORNING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("LabCorp NYC")));
    }

    @Test
    void testInvalidAddressGeocodingFails() throws Exception {
        PatientAddressDTO input = new PatientAddressDTO();
        input.setPatientId(UUID.randomUUID());
        input.setStreet("Unknown");
        input.setZipcode("00000");
        input.setState("Nowhere");
        input.setCounty("NoCounty");

        when(geocodingService.getLatLongFromAddress(anyString()))
                .thenReturn(new double[]{0.0, 0.0});

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .param("date", "2025-07-26")
                        .param("slot", "MORNING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to resolve address")));
    }

    @Test
    void testNoNearbyLabcorpFound() throws Exception {
        PatientAddressDTO input = new PatientAddressDTO();
        input.setPatientId(UUID.randomUUID());
        input.setStreet("456 Faraway");
        input.setZipcode("99999");
        input.setState("NY");
        input.setCounty("Upstate");

        when(geocodingService.getLatLongFromAddress(anyString()))
                .thenReturn(new double[]{40.0, -75.0});

        LabcorpLocationDTO location = new LabcorpLocationDTO();
        location.setLocationId(UUID.randomUUID());
        location.setLatitude(41.0);  // far
        location.setLongitude(-76.0);
        location.setLocationName("LabCorp Albany");

        LabcorpAvailabilityDTO availability = new LabcorpAvailabilityDTO();
        availability.setLocationId(location.getLocationId());
        availability.setDate(LocalDate.of(2025, 7, 26));
        availability.setMorningSlot(true);

        when(restTemplate.getForObject(contains("/availability"), eq(LabcorpAvailabilityDTO[].class)))
                .thenReturn(new LabcorpAvailabilityDTO[]{availability});

        when(restTemplate.getForObject(contains("/locations"), eq(LabcorpLocationDTO[].class)))
                .thenReturn(new LabcorpLocationDTO[]{location});

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .param("date", "2025-07-26")
                        .param("slot", "MORNING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No labcorp location available")));
    }

    @Test
    void testNoAvailabilityForDate() throws Exception {
        PatientAddressDTO input = new PatientAddressDTO();
        input.setPatientId(UUID.randomUUID());
        input.setStreet("123 Main St");
        input.setZipcode("10001");
        input.setState("NY");
        input.setCounty("New York");

        when(geocodingService.getLatLongFromAddress(anyString()))
                .thenReturn(new double[]{40.7128, -74.0060});

        when(restTemplate.getForObject(contains("/availability"), eq(LabcorpAvailabilityDTO[].class)))
                .thenReturn(new LabcorpAvailabilityDTO[0]); // empty

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .param("date", "2025-07-30")
                        .param("slot", "MORNING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No available slots")));
    }
}