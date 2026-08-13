package com.example.gameflix.dto;

public class SubscriptionResponse {

    private String message;
    private String plan;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(String message, String plan) {
        this.message = message;
        this.plan = plan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
