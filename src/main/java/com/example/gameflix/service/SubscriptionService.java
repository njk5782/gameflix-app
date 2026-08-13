package com.example.gameflix.service;

import com.example.gameflix.model.User;
import com.example.gameflix.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SubscriptionService {

    private static final Set<String> AVAILABLE_PLANS =
            Set.of("PLAYER", "PRO", "FAMILY");

    private final UserRepository userRepository;

    public SubscriptionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getPlan(String username) {
        String plan = findUser(username).getSubscriptionPlan();
        return plan == null || plan.isBlank() ? "NONE" : plan;
    }

    public String updatePlan(String username, String requestedPlan) {
        if (requestedPlan == null) {
            throw new IllegalArgumentException("Subscription plan is required");
        }

        String plan = requestedPlan.trim().toUpperCase();

        if (!AVAILABLE_PLANS.contains(plan)) {
            throw new IllegalArgumentException("Invalid subscription plan");
        }

        User user = findUser(username);
        user.setSubscriptionPlan(plan);
        userRepository.save(user);
        return plan;
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
