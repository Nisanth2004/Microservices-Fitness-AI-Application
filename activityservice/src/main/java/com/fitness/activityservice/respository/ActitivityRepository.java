package com.fitness.activityservice.respository;

import com.fitness.activityservice.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActitivityRepository extends MongoRepository<Activity,String> {
}
