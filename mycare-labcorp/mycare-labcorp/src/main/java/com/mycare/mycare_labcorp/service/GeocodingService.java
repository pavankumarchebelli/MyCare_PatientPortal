package com.mycare.mycare_labcorp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public double[] getLatLongFromAddress(String address) {
        URI uri = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .build()
                .toUri();

        try {
            Map[] response = restTemplate.getForObject(uri, Map[].class);
            if (response != null && response.length > 0) {
                double lat = Double.parseDouble((String) response[0].get("lat"));
                double lon = Double.parseDouble((String) response[0].get("lon"));
                return new double[]{lat, lon};
            }
        } catch (Exception e) {
            System.err.println("Geocoding failed: " + e.getMessage());
        }

        return new double[]{0.0, 0.0}; // default fallback
    }
}