package com.example.locationservice.services;

import com.example.locationservice.dto.DriverLocationDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    private final RedisTemplate redisTemplate;

    private static final String DRIVER_GEO_OPS_KEY = "drivers"; // tells it is a collection of drivers

    private static final Double SEARCH_RADIUS = 5.0;

    public LocationServiceImpl(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean saveDriverLocation(String driverId, Double latitude, Double longitude) {
        GeoOperations<String, String> geoOps =  redisTemplate.opsForGeo();
        geoOps.add(DRIVER_GEO_OPS_KEY,
                new RedisGeoCommands.GeoLocation<>(
                   driverId,
                   new Point(latitude, longitude)
                )
        );
        return true;
    }

    @Override
    public List<DriverLocationDto> getNearByDrivers(Double latitude, Double longitude) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        Distance radius = new Distance(SEARCH_RADIUS, Metrics.KILOMETERS);
        Circle within = new Circle(new Point(latitude, longitude), radius);
        GeoResults<RedisGeoCommands.GeoLocation<String>> results =  geoOps.radius(DRIVER_GEO_OPS_KEY, within);
        List<DriverLocationDto> drivers = new ArrayList<>();
        for(GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
            Point pos = geoOps.position(DRIVER_GEO_OPS_KEY, result.getContent().getName()).get(0);
            DriverLocationDto driverLocation = DriverLocationDto.builder()
                    .driverId(result.getContent().getName())
                    .latitude(pos.getX())
                    .longitude(pos.getY())
                    .build();
            drivers.add(driverLocation);
        }

        return drivers;
    }
}
