package com.nisanth.aiservice.service;

import com.nisanth.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


import org.springframework.stereotype.Service;
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    @RabbitListener(queues = "activity.queue")
    public void processActivity(String message) {
        System.out.println("Received: " + message);
        log.info("Received Activity for processing: {}", message);
    }

}
