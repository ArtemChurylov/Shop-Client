package com.example.shopclient.application.model;

import com.example.shopclient.application.validation.Price;
import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.Seller;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

public class Product {

    @JsonProperty("id")
    private Long id;

    @NotEmpty
    @Size(min = 5, max = 40, message = "Size should be between 5 and 40 characters")
    @JsonProperty("title")
    private String title;

    @NotEmpty
    @Price
    @JsonProperty("price")
    private String price;

    @NotEmpty
    @Size(max = 1000, message = "Max size is 1000 characters")
    @JsonProperty("description")
    private String description;

    @NotEmpty
    @JsonProperty("category")
    private String category;

    @JsonProperty("customers")
    private Set<Client> customers;

    @JsonProperty("seller")
    private Seller seller;

    @JsonProperty("date_of_adding")
    private Date date_of_adding;

    @JsonProperty("image")
    private String image;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public Date getDate_of_adding() {
        return date_of_adding;
    }

    public void setDate_of_adding(Date date_of_adding) {
        this.date_of_adding = date_of_adding;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
