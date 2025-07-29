package com.mycare.mycare_patient.controller;


import com.mycare.mycare_patient.dto.PatientAddressDTO;
import com.mycare.mycare_patient.entity.*;
import com.mycare.mycare_patient.repository.*;
import com.mycare.mycare_patient.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired private PatientRepository patientRepo;
    @Autowired private PatientAddressRepository addressRepo;
    @Autowired private PatientAvailabilityRepository availabilityRepo;
    @Autowired private GeocodingService geocodingService;

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepo.save(patient);
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable UUID id) {
        return patientRepo.findById(id).orElse(null);
    }

    @PostMapping("/{id}/address")
    public PatientAddress addAddress(@PathVariable UUID id, @RequestBody PatientAddress address) {
        address.setPatientId(id);

        // Automatically fetch lat/long
        double[] latLng = geocodingService.getLatLongFromAddress(
                address.getStreet(),
                address.getCounty(),
                address.getState(),
                address.getZipcode()
        );

        address.setLatitude(latLng[0]);
        address.setLongitude(latLng[1]);

        PatientAddress savedAddress = addressRepo.save(address);

        // Link address to patient
        Patient patient = patientRepo.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setAddressId(savedAddress.getAddressId());
        patientRepo.save(patient);

        return savedAddress;
    }



    @PostMapping("/{id}/availability")
    public PatientAvailability addAvailability(@PathVariable UUID id, @RequestBody PatientAvailability availability) {
        availability.setPatientId(id);
        return availabilityRepo.save(availability);
    }

    @GetMapping("/{id}/availability")
    public List<PatientAvailability> getAvailability(@PathVariable UUID id) {
        return availabilityRepo.findByPatientId(id);
    }

    @GetMapping("/{id}/address")
    public PatientAddress getAddress(@PathVariable UUID id) {
        return addressRepo.findByPatientId(id);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<PatientAddressDTO> getPatientWithAddress(@PathVariable UUID id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        PatientAddress address = addressRepo.findByPatientId(id);

        if (patient == null || address == null) {
            return ResponseEntity.notFound().build();
        }

        PatientAddressDTO dto = new PatientAddressDTO();
        dto.setAddressId(address.getAddressId());
        dto.setStreet(address.getStreet());
        dto.setZipcode(address.getZipcode());
        dto.setState(address.getState());
        dto.setCounty(address.getCounty());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());

        return ResponseEntity.ok(dto);
    }

}
