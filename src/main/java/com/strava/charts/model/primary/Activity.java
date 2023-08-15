package com.strava.charts.model.primary;

import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "activities")
public class Activity {

     @Id
     private Long id;
     private String name;
     private Float distance;
     private Integer movingTime;
     private Integer elapsedTime;
     private ActivityType type;
     private Long created;
     private Integer maxHeartRate;
     private Float averageWatts;
     private Float maxWatts;
     private Float averageSpeed;
     private Float maxSpeed;

     public static Activity convertToActivity(final SummaryActivity sumActivity) {
          return Activity.builder().id(sumActivity.getId()).name(sumActivity.getName())
                  .distance(sumActivity.getDistance())
                  .elapsedTime(sumActivity.getElapsedTime())
                  .movingTime(sumActivity.getMovingTime())
                  .averageWatts(sumActivity.getAverageWatts()).maxWatts(
                          (float) (sumActivity.getMaxWatts() != null
                                  ? sumActivity.getMaxWatts() : 0))
                  .type(sumActivity.getType())
                  .created(sumActivity.getStartDate().toEpochSecond()).build();
     }

}
