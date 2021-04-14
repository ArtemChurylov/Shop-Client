package com.example.shopclient.application.model;

import javax.validation.constraints.NotEmpty;

public class Address {

    @NotEmpty
    private String country;

    @NotEmpty
    private String region;

    @NotEmpty
    private String city;

    @NotEmpty
    private String post_office_number;

    public Address() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPost_office_number() {
        return post_office_number;
    }

    public void setPost_office_number(String post_office_number) {
        this.post_office_number = post_office_number;
    }
}
