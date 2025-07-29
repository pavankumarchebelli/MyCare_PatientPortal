package com.mycare.mycare_labcorp.controller;

import com.mycare.mycare_labcorp.entity.LabcorpAvailability;
import com.mycare.mycare_labcorp.entity.LabcorpLocation;
import com.mycare.mycare_labcorp.repository.LabcorpAvailabilityRepository;
import com.mycare.mycare_labcorp.repository.LabcorpLocationRepository;
import com.mycare.mycare_labcorp.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/labcorp")
public class LabcorpController {

    @Autowired private LabcorpLocationRepository locationRepo;
    @Autowired private LabcorpAvailabilityRepository availabilityRepo;
    @Autowired private GeocodingService geocodingService;

    @PostMapping("/locations")
    public ResponseEntity<LabcorpLocation> createLabLocation(@RequestBody LabcorpLocation location) {
        if ((location.getLatitude() == 0.0 || location.getLongitude() == 0.0) && location.getAddress() != null) {
            double[] latLon = geocodingService.getLatLongFromAddress(location.getAddress());
            location.setLatitude(latLon[0]);
            location.setLongitude(latLon[1]);
        }

        LabcorpLocation saved = locationRepo.save(location);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/locations")
    public List<LabcorpLocation> getAllLocations() {
        return locationRepo.findAll();
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<LabcorpLocation> getLocationById(@PathVariable UUID id) {
        return locationRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/availability")
    public LabcorpAvailability addAvailability(@RequestBody LabcorpAvailability availability) {
        return availabilityRepo.save(availability);
    }

    // This is returning the whole response body of the location instead of just the boolean value we can change this later
    @GetMapping("/availability")
    public List<LabcorpAvailability> getAvailability(
            @RequestParam String date,
            @RequestParam String slot // MORNING / AFTERNOON / EVENING
    ) {
        LocalDate parsedDate = LocalDate.parse(date);
        return switch (slot.toUpperCase()) {
            case "MORNING" -> availabilityRepo.findByDateAndMorningSlotTrue(parsedDate);
            case "AFTERNOON" -> availabilityRepo.findByDateAndAfternoonSlotTrue(parsedDate);
            case "EVENING" -> availabilityRepo.findByDateAndEveningSlotTrue(parsedDate);
            default -> List.of();
        };
    }

    @PutMapping("/availability/{id}/booked")
    public ResponseEntity<String> markSlotAsBooked(@PathVariable UUID id, @RequestParam String slot) {
        LabcorpAvailability availability = availabilityRepo.findById(id).orElse(null);
        if (availability == null) {
            return ResponseEntity.notFound().build();
        }

        switch (slot.toUpperCase()) {
            case "MORNING":
                if (!availability.isMorningSlot()) return ResponseEntity.badRequest().body("Slot already booked");
                availability.setMorningSlot(false);
                break;
            case "AFTERNOON":
                if (!availability.isAfternoonSlot()) return ResponseEntity.badRequest().body("Slot already booked");
                availability.setAfternoonSlot(false);
                break;
            case "EVENING":
                if (!availability.isEveningSlot()) return ResponseEntity.badRequest().body("Slot already booked");
                availability.setEveningSlot(false);
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid slot value. Choose MORNING, AFTERNOON, or EVENING");
        }

        availabilityRepo.save(availability);
        return ResponseEntity.ok("Slot marked as booked");
    }

}