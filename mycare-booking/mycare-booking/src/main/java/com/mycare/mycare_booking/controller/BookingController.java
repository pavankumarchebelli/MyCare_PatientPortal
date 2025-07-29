package com.mycare.mycare_booking.controller;

import com.mycare.mycare_booking.dto.*;
import com.mycare.mycare_booking.entity.Booking;
import com.mycare.mycare_booking.repository.BookingRepository;
import com.mycare.mycare_booking.service.GeocodingService;
import com.mycare.mycare_booking.util.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired private GeocodingService geocodingService;

    private final String PATIENT_BASE = "http://localhost:8080/patients";
    private final String LABCORP_BASE = "http://localhost:8081/labcorp";

    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestParam UUID patientId,
                                                  @RequestParam String slot,
                                                  @RequestParam String date) {
        LocalDate bookingDate = LocalDate.parse(date);

        // Step 1: Get Patient Address
        PatientAddressDTO address = restTemplate.getForObject(
                PATIENT_BASE + "/" + patientId + "/address", PatientAddressDTO.class);
        if (address == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No address found");

        // Step 2: Get matching availabilities from Labcorp
        LabcorpAvailabilityDTO[] slots = restTemplate.getForObject(
                LABCORP_BASE + "/availability?date=" + date + "&slot=" + slot,
                LabcorpAvailabilityDTO[].class);

        if (slots == null || slots.length == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No slots available");

        // Step 3: Get all locations
        LabcorpLocationDTO[] locations = restTemplate.getForObject(
                LABCORP_BASE + "/locations", LabcorpLocationDTO[].class);
        if (locations == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No locations found");

        // Step 4: Match by 5 mile distance
        for (LabcorpAvailabilityDTO avail : slots) {
            UUID locId = avail.getLocationId();

            Optional<LabcorpLocationDTO> matchedLocation = Arrays.stream(locations)
                    .filter(loc -> loc.getLocationId().equals(locId))
                    .filter(loc -> GeoUtils.calculateDistance(
                            address.getLatitude(), address.getLongitude(),
                            loc.getLatitude(), loc.getLongitude()) <= 5)
                    .findFirst();

            if (matchedLocation.isPresent()) {
                Booking booking = new Booking();
                booking.setPatientId(patientId);
                booking.setLocationId(locId);
                booking.setSlot(slot);
                booking.setBookingDate(bookingDate);
                String updateUrl = LABCORP_BASE + "/availability/" + avail.getAvailabilityId() + "/booked?slot=" + slot;
                restTemplate.put(updateUrl, null);
                bookingRepo.save(booking);
                return ResponseEntity.ok("Booking successful at location: " + matchedLocation.get().getLocationName());
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available slots within 5 miles");
    }

//    @GetMapping("/by-patient/{id}")
//    public List<Booking> getBookings(@PathVariable UUID id) {
//        return bookingRepo.findByPatientId(id);
//    }

    @GetMapping("/by-patient/{id}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByPatient(@PathVariable UUID id) {
        List<Booking> bookings = bookingRepo.findByPatientId(id);

        List<BookingResponseDTO> result = bookings.stream().map(booking -> {
            BookingResponseDTO dto = new BookingResponseDTO();
            dto.setBookingId(booking.getBookingId());
            dto.setBookingDate(booking.getBookingDate().toString());
            dto.setSlot(booking.getSlot());

            // Fetch patient info
            String patientUrl = "http://localhost:8080/patients/" + booking.getPatientId() + "/details";
            PatientAddressDTO patient = restTemplate.getForObject(patientUrl, PatientAddressDTO.class);
            dto.setPatient(patient);

            // Fetch lab info
            String labUrl = "http://localhost:8081/labcorp/locations/" + booking.getLocationId();
            LabcorpLocationDTO lab = restTemplate.getForObject(labUrl, LabcorpLocationDTO.class);
            dto.setLab(lab);

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/book-from-address")
    public ResponseEntity<String> bookFromAddress(@RequestBody PatientAddressDTO patientAddress,
                                                  @RequestParam String date,
                                                  @RequestParam String slot) {
        LocalDate bookingDate = LocalDate.parse(date);

        // Step 1: Build full address string and get coordinates from GeocodingService
        String addressQuery = String.format("%s, %s, %s %s",
                patientAddress.getStreet(),
                patientAddress.getCounty(),
                patientAddress.getState(),
                patientAddress.getZipcode());

        double[] coordinates = geocodingService.getLatLongFromAddress(addressQuery);
        double lat = coordinates[0];
        double lon = coordinates[1];

        if (lat == 0.0 && lon == 0.0) {
            return ResponseEntity.badRequest().body("Unable to resolve address into coordinates");
        }

        // Step 2: Fetch labcorp availability
        LabcorpAvailabilityDTO[] availabilities = restTemplate.getForObject(
                LABCORP_BASE + "/availability?date=" + date + "&slot=" + slot,
                LabcorpAvailabilityDTO[].class
        );

        if (availabilities == null || availabilities.length == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available slots for the requested time.");

        // Step 3: Fetch all labcorp locations
        LabcorpLocationDTO[] locations = restTemplate.getForObject(
                LABCORP_BASE + "/locations", LabcorpLocationDTO[].class
        );

        if (locations == null || locations.length == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No labcorp locations found.");

        // Step 4: Match availability within 5 miles of geocoded patient location
        for (LabcorpAvailabilityDTO availability : availabilities) {
            Optional<LabcorpLocationDTO> match = Arrays.stream(locations)
                    .filter(loc -> loc.getLocationId().equals(availability.getLocationId()))
                    .filter(loc -> GeoUtils.calculateDistance(lat, lon, loc.getLatitude(), loc.getLongitude()) <= 5)
                    .findFirst();

            if (match.isPresent()) {
                Booking booking = new Booking();
                booking.setBookingId(UUID.randomUUID());
                booking.setPatientId(patientAddress.getPatientId());
                booking.setLocationId(match.get().getLocationId());
                booking.setSlot(slot);
                booking.setBookingDate(bookingDate);
                bookingRepo.save(booking);

                String updateUrl = LABCORP_BASE + "/availability/" + availability.getAvailabilityId() + "/booked?slot=" + slot;
                restTemplate.put(updateUrl, null);


                return ResponseEntity.ok("Booking confirmed at: " + match.get().getLocationName());
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No labcorp location available within 5 miles.");
    }

}
