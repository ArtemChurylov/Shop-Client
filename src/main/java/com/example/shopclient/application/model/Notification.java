package com.example.shopclient.application.model;

import com.example.shopclient.security.model.Seller;
import com.fasterxml.jackson.annotation.JsonProperty;

// Notification model
public class Notification {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("seller")
    private Seller seller;

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
