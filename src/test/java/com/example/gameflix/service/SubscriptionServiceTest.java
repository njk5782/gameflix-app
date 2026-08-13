package com.example.gameflix.service;

import com.example.gameflix.model.User;
import com.example.gameflix.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SubscriptionServiceTest {

    private final UserRepository userRepository =
            mock(UserRepository.class);
    private final SubscriptionService subscriptionService =
            new SubscriptionService(userRepository);

    @Test
    void updatePlan_WhenPlanIsValid_ShouldSavePlan() {
        User user = new User("player1", "hashed-password");
        when(userRepository.findByUsername("player1"))
                .thenReturn(Optional.of(user));

        String plan = subscriptionService.updatePlan("player1", "pro");

        assertEquals("PRO", plan);
        assertEquals("PRO", user.getSubscriptionPlan());
        verify(userRepository).save(user);
    }

    @Test
    void updatePlan_WhenPlanIsInvalid_ShouldRejectPlan() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> subscriptionService.updatePlan("player1", "premium")
        );

        assertEquals("Invalid subscription plan", exception.getMessage());
    }

    @Test
    void getPlan_ShouldReturnSavedPlan() {
        User user = new User("player1", "hashed-password");
        user.setSubscriptionPlan("FAMILY");
        when(userRepository.findByUsername("player1"))
                .thenReturn(Optional.of(user));

        String plan = subscriptionService.getPlan("player1");

        assertEquals("FAMILY", plan);
    }

    @Test
    void getPlan_WhenExistingUserHasNoPlan_ShouldReturnNone() {
        User user = new User("older-user", "hashed-password");
        user.setSubscriptionPlan(null);
        when(userRepository.findByUsername("older-user"))
                .thenReturn(Optional.of(user));

        String plan = subscriptionService.getPlan("older-user");

        assertEquals("NONE", plan);
    }
}
