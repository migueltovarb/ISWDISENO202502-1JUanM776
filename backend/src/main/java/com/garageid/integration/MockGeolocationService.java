package com.garageid.integration;

import org.springframework.stereotype.Service;

@Service
public class MockGeolocationService implements GeolocationService {
    @Override
    public Coordinates geocode(String address) {
        return new Coordinates(4.7110, -74.0721);
    }
}