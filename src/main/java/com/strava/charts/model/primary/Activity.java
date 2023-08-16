package com.strava.charts.model.primary;

import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "activities")
public class Activity {

     @Id
     @Field
     private Long id;
     private String name;
     private Float distance;
     private Integer movingTime;
     private Integer elapsedTime;
     private ActivityType type;
     private Long created;
     private Long updated;
     private Integer maxHeartRate;
     private Float averageWatts;
     private Float maxWatts;
     private Float averageSpeed;
     private Float maxSpeed;
     private Boolean faultySensorData;

     public static Activity convertToActivity(final SummaryActivity sumActivity) {
          return Activity.builder().id(sumActivity.getId()).name(sumActivity.getName())
                  .distance(sumActivity.getDistance())
                  .movingTime(sumActivity.getMovingTime())
                  .elapsedTime(sumActivity.getElapsedTime()).type(sumActivity.getType())
                  .created(sumActivity.getStartDate().toEpochSecond())
                  .averageWatts(sumActivity.getAverageWatts()).maxWatts(
                          (float) (sumActivity.getMaxWatts() != null
                                  ? sumActivity.getMaxWatts() : 0))
                  .averageSpeed(sumActivity.getAverageSpeed())
                  .maxSpeed(sumActivity.getMaxSpeed()).build();
     }

}
