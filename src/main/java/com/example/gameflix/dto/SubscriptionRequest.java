package com.example.gameflix.dto;

public class SubscriptionRequest {

    private String plan;

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(String plan) {
        this.plan = plan;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
