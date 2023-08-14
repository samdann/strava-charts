package com.strava.charts.model.primary;

import io.swagger.client.model.ActivityType;
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
     private ActivityType type;
     private Long created;
     private Integer maxHeartRate;

}
