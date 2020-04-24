package com.strava.charts.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DailyRideRecord {
    private int dayIndex;
    private String distance;
    private String movingTime;
    private String elapsedTime;
    private String stoppedTime;
    private String relativeMovingTime;
    private String relativeStoppedTime;
}
