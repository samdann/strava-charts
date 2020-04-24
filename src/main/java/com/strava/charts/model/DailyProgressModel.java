package com.strava.charts.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class DailyProgressModel {

    private List<DailyRideRecord> dailyRideRecords;
    private List<RideByDistanceRecord> rideByDistanceRecords;

    public List<DailyRideRecord> getDailyRideRecords() {
        if (dailyRideRecords == null) {
            dailyRideRecords = new ArrayList<>();
        }
        return dailyRideRecords;
    }

    public List<RideByDistanceRecord> getRideByDistanceRecords() {
        if (rideByDistanceRecords == null) {
            rideByDistanceRecords = new ArrayList<>();
        }
        return rideByDistanceRecords;
    }
}
