package main.java.com;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@SpringBootApplication
@RestController
@CrossOrigin
public class SubscriberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriberApplication.class, args);
    }

    @GetMapping("/dapr/subscribe")
    public List<Map<String, String>> getSubscriptions() {
        Map<String, String> subA = Map.of(
            "pubsubname", "pubsub",
            "topic", "A",
            "route", "A"
        );

        Map<String, String> subC = Map.of(
            "pubsubname", "pubsub",
            "topic", "C",
            "route", "C"
        );

        return List.of(subA, subC);
    }

    @PostMapping(value = "/A", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Map<String, Boolean>> handleATopic(@RequestBody Map<String, Object> body) {
        System.out.println("A: " + body);
        printCustomMessage(body);
        return buildSuccessResponse();
    }

    @PostMapping(value = "/C", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Map<String, Boolean>> handleCTopic(@RequestBody Map<String, Object> body) {
        System.out.println("C: " + body);
        printCustomMessage(body);
        return buildSuccessResponse();
    }

    private void printCustomMessage(Map<String, Object> body) {
        try {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            String messageType = (String) data.get("messageType");
            String message = (String) data.get("message");
            System.out.printf("Received message: [%s] on topic \"%s\"%n", message, messageType);
        } catch (Exception e) {
            System.out.println("Error parsing message: " + e.getMessage());
        }
    }

    private ResponseEntity<Map<String, Boolean>> buildSuccessResponse() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
