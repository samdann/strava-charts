package com.strava.charts.repository;

import com.strava.charts.model.primary.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRepository extends MongoRepository<Activity, String> {

}
