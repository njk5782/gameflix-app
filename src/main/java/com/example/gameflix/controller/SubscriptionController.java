package com.example.gameflix.controller;

import com.example.gameflix.dto.SubscriptionRequest;
import com.example.gameflix.dto.SubscriptionResponse;
import com.example.gameflix.service.JwtService;
import com.example.gameflix.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final JwtService jwtService;

    public SubscriptionController(
            SubscriptionService subscriptionService,
            JwtService jwtService
    ) {
        this.subscriptionService = subscriptionService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public SubscriptionResponse getSubscription(HttpServletRequest request) {
        String username = getUsername(request);
        String plan = subscriptionService.getPlan(username);
        return new SubscriptionResponse("Subscription loaded", plan);
    }

    @PutMapping
    public SubscriptionResponse updateSubscription(
            @RequestBody SubscriptionRequest requestBody,
            HttpServletRequest request
    ) {
        String username = getUsername(request);
        String plan = subscriptionService.updatePlan(
                username,
                requestBody.getPlan()
        );
        return new SubscriptionResponse("Subscription updated", plan);
    }

    private String getUsername(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String token = authorization.substring(7);
        return jwtService.readToken(token).getSubject();
    }
}
