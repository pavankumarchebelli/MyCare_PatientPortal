package com.mycare.mycare_patient.dto;

import java.util.UUID;

public class PatientAddressDTO {
    private UUID addressId;
    private String street;
    private String zipcode;
    private String state;
    private String county;
    private double latitude;
    private double longitude;

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public PatientAddressDTO(UUID addressId, String street, String zipcode, String state, String county, double latitude, double longitude) {
        this.addressId = addressId;
        this.street = street;
        this.zipcode = zipcode;
        this.state = state;
        this.county = county;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PatientAddressDTO() {
    }
}

