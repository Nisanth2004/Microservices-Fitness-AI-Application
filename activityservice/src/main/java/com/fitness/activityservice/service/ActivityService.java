package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.respository.ActitivityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActitivityRepository actitivityRepository;
    public ActivityResponse trackActivity(ActivityRequest request) {
        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .additionalMetrics(request.getAdditionalMetrics())
                .type(request.getType())
                .startTime(request.getStartTime())
                .build();

        Activity savedActivity=actitivityRepository.save(activity);
        return mapToResponse(savedActivity);


    }


    private ActivityResponse mapToResponse(Activity activity)
    {
        ActivityResponse activityResponse=new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());

        return activityResponse;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
       List<Activity> activities=  actitivityRepository.findByUserId(userId);
       return activities.stream()
               .map(this::mapToResponse)
               .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {

       return actitivityRepository.findById(activityId)
               .map(this::mapToResponse)
                .orElseThrow(()->new RuntimeException("Activity Not Found for this Id "+activityId));
    }
}
