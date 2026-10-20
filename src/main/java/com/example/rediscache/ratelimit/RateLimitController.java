
package com.example.rediscache.ratelimit;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimitController {

    private final RateLimiterService rateLimiterService;

    public RateLimitController(
            RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test(
            @RequestHeader(
                    value = "X-Client-Id",
                    defaultValue = "demo-client"
            ) String clientId) {

        if (!rateLimiterService.isAllowed(clientId)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of(
                            "error", "Rate limit exceeded",
                            "message", "Maximum 5 requests per minute"
                    ));
        }

        return ResponseEntity.ok(
                Map.of("message", "Request successful")
        );
    }
}
