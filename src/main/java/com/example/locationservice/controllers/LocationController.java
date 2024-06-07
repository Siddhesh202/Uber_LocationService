package com.example.locationservice.controllers;

import com.example.locationservice.dto.DriverLocationDto;
import com.example.locationservice.dto.NearbyDriversRequestDto;
import com.example.locationservice.dto.SaveDriverLocationDto;
import com.example.locationservice.services.LocationServiceImpl;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationServiceImpl locationService;

    public LocationController(LocationServiceImpl locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/drivers")
    public ResponseEntity<Boolean> saveDriverLocation(@RequestBody SaveDriverLocationDto saveDriverLocationDto) {
        try{
            Boolean ans = locationService.saveDriverLocation(saveDriverLocationDto.getDriverId(),
                    saveDriverLocationDto.getLatitude(),
                    saveDriverLocationDto.getLongitude()
            );
            return new ResponseEntity<>(ans, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nearby/drivers")
    public ResponseEntity<List<DriverLocationDto>> getNearbyDrivers(@RequestBody NearbyDriversRequestDto nearbyDriversRequestDto) {
        try{
            List<DriverLocationDto> driverLocations = locationService.getNearByDrivers(nearbyDriversRequestDto.getLatitude(),
                    nearbyDriversRequestDto.getLongitude());
            return new ResponseEntity<>(driverLocations, HttpStatus.OK);

        }catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
