package com.example.shopclient.application.model;

import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.Seller;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Product {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private String category;

    @JsonProperty("customers")
    private Set<Client> customers;

    @JsonProperty("seller")
    private Seller seller;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Client> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Client> customers) {
        this.customers = customers;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
