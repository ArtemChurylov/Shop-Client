package com.example.shopclient.security.model;


import com.example.shopclient.security.validation.ConfirmDBPassword;
import com.example.shopclient.security.validation.ConfirmPassword;
import com.example.shopclient.security.validation.PhoneNumber;
import com.example.shopclient.security.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


// User to validate form
@UniqueEmail
@ConfirmPassword
public class TempUser {

    @NotEmpty
    @Email
    @Size(max = 255)
    private String email;

    @NotEmpty
    @Size(min = 3, max = 50)
    private String name;

    @NotEmpty
    @Size(min = 3, max = 50)
    private String surname;

    @PhoneNumber
    @NotEmpty
    @Size(min = 6, max = 25)
    private String phone;

    @NotEmpty
    @Size(min = 6, max = 50)
    private String password;

    @NotEmpty
    @Size(min = 6, max = 50)
    private String confirmPassword;

    @ConfirmDBPassword
    private String confirmDBPassword;

    public TempUser() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmDBPassword() {
        return confirmDBPassword;
    }

    public void setConfirmDBPassword(String confirmDBPassword) {
        this.confirmDBPassword = confirmDBPassword;
    }
}
