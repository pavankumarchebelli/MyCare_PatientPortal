package com.mycare.mycare_booking.dto;

import java.util.UUID;

public class LabcorpLocationDTO {
    private UUID locationId;
    private String locationName;
    private String address;
    private double latitude;
    private double longitude;

    public LabcorpLocationDTO() {
    }

    public LabcorpLocationDTO(UUID locationId, String locationName, String address, double latitude, double longitude) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}