package com.example.locationservice.services;

import com.example.locationservice.dto.DriverLocationDto;

import java.util.List;

public interface LocationService {

    Boolean saveDriverLocation(String driverId, Double latitude, Double longitude);

    List<DriverLocationDto> getNearByDrivers(Double latitude, Double longitude);
}
