package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.respository.ActitivityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActitivityRepository actitivityRepository;

    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    public ActivityResponse trackActivity(ActivityRequest request) {
        boolean isValidUser=userValidationService.validateUser(request.getUserId());
        if(!isValidUser)
        {
            throw new RuntimeException("Invalid user: "+request.getUserId());
        }
        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .additionalMetrics(request.getAdditionalMetrics())
                .type(request.getType())
                .startTime(request.getStartTime())
                .build();

        Activity savedActivity=actitivityRepository.save(activity);

        // publish to RqbbitMq for AI microservicing
        try{
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);

        }
        catch(Exception e)
        {
            log.error("Failed to publish the activity to Rabbit: ",e);

        }
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
