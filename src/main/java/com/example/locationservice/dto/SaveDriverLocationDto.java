package com.example.locationservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveDriverLocationDto {

    private String driverId;

    private Double latitude;

    private Double longitude;
}
