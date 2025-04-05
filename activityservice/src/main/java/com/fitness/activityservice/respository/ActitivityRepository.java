package com.fitness.activityservice.respository;

import com.fitness.activityservice.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActitivityRepository extends MongoRepository<Activity,String> {
    List<Activity>  findByUserId(String userId);
}
