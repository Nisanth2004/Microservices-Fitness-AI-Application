package com.nisanth.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiAPiKey;

    public GeminiService(WebClient webClientBuilder) {
        this.webClient = webClientBuilder.mutate().build();
    }

    public String getAnswer(String question)
    {
        //strcuture the request
        Map<String,Object> requestBody=Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]
                                {
                                        Map.of("text",question)
                                })
                });

        String response=webClient.post()
                .uri(geminiApiUrl+geminiAPiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;

    }
}
