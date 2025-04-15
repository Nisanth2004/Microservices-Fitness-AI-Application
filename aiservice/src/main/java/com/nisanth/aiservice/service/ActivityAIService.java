package com.nisanth.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisanth.aiservice.model.Activity;
import com.nisanth.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity)
    {
        String prompt=createPromptForActivity(activity);
        String aiResponse=geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI:{} ",aiResponse);
        return  processAiResponse(activity,aiResponse);
    }


    public Recommendation processAiResponse(Activity activity, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode candidatesNode = rootNode.path("candidates");

            if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                JsonNode textNode = candidatesNode.get(0)
                        .path("content")
                        .path("parts");

                if (textNode.isArray() && textNode.size() > 0) {
                    String rawText = textNode.get(0).path("text").asText();

                    String jsonContent = rawText
                            .replaceAll("```json\\n?", "")
                            .replaceAll("\\n?```", "")
                            .trim();

                    //log.info("PARSED RESPONSE FROM AI: {}", jsonContent);

                    // Proceed with deserialization if needed here

                    JsonNode analysisJson= mapper.readTree(jsonContent);
                    JsonNode analysisNode=analysisJson.path("analysis");
                    StringBuilder fullAnalysis=new StringBuilder();
                    addAnalysisection(fullAnalysis,analysisNode,"overall","Overall:");
                    addAnalysisection(fullAnalysis,analysisNode,"pace","Pace:");
                    addAnalysisection(fullAnalysis,analysisNode,"heartRate","Heart rate:");
                    addAnalysisection(fullAnalysis,analysisNode,"caloriesBurned","Calories:");

                    List<String> improvements=extractImprovements(analysisJson.path("improvements"));
                    List<String> suggestions=extractSuggestions(analysisJson.path("suggestions"));
                    List<String> saftey=extractSafteyGuidelines(analysisJson.path("safety"));

                    return Recommendation.builder()
                            .activityId(activity.getId())
                            .userId(activity.getUserId())
                            .activityType(activity.getType())
                            .duration(activity.getDuration())
                            .caloriesBurned(activity.getCaloriesBurned())
                            .recommendation(fullAnalysis.toString().trim())
                            .improvements(improvements)
                            .suggestions(suggestions)
                            .safety(saftey)
                            .createdAt(LocalDateTime.now())
                            .build();



                } else {
                    log.warn("Parts array is missing or empty in the response.");
                    return  CreateDefaultRecommendation(activity);
                }
            } else {
                log.warn("Candidates array is missing or empty in the response.");
                return  CreateDefaultRecommendation(activity);
            }
        } catch (Exception e) {
            log.error("Error parsing AI response", e);
            return  CreateDefaultRecommendation(activity);
        }
    }

    private Recommendation CreateDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to get the generated analysis ")
                .improvements(Collections.singletonList("Continue with current Routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to our body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafteyGuidelines(JsonNode safetyNode) {

        List<String> safety=new ArrayList<>();
        if(safetyNode.isArray())
        {
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty() ?
                Collections.singletonList("Follow general saftey guildelines"):
                safety;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions=new ArrayList<>();
        if(suggestionsNode.isArray())
        {
            suggestionsNode.forEach(suggestion -> {
                String workout=suggestion.path("workout").asText();
                String description= suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s",workout ,description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided"):
                suggestions;

    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements=new ArrayList<>();
       if(improvementsNode.isArray())
       {
           improvementsNode.forEach(improvement -> {
               String area=improvement.path("area").asText();
               String detail= improvement.path("recommendation").asText();
               improvements.add(String.format("%s: %s",area,detail));
           });
       }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific requirements provided"):
                improvements;
    }

    private void addAnalysisection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode())
        {
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }


    private String createPromptForActivity(Activity activity) {
        return String.format("""
                
                Analyze the fitness Activity and provide  detailed Recommendation in the follwing EXACT JSON FORMAT:
                {
                  "analysis":{
                      "overall":"Overall analysis here",
                      "pace":"Pace analysis here",
                      "heartRate":"Heart rate analysis here",
                      "calorieBurned":"Calories analysis here"
                      },
                      "improvements:"[
                      {
                        "area":"Area name",
                        "recommendation":"Deatiled Recommendation"
                      }
                      ],
                      "suggestions":[
                        {
                        "workout":"Workout Name",
                        "description":"Detailed  workout description"
                        }
                      ],
                      "safety":[
                        "Safety Point 1",
                        "Safety Point 2"
                        ]
                        }
                      
                      
                      Analyse the activity:
                      Activity Type: %s
                      Duration: %d minutes
                      Calories Burned: %d
                      Additional Metrics: %s
                      
                      Provide detailed analysis focusing on performance,improvements,next workout suggestions,and saftety guidlines.
                      Ensure the response follows the EXACT JSON fomat shown above.
                
                """,
                activity.getType(),
                activity.getDuration(),

                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
