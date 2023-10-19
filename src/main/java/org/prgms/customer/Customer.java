package org.prgms.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private final UUID customerId;
    private String name;
    private final String email;
    private LocalDateTime lastLoginAt;
    private final LocalDateTime createdAt;

    public Customer(UUID customerId, String name, String email,LocalDateTime lastLoginAt, LocalDateTime createdAt) {
        validatedName(name);
        this.customerId = customerId;
        this.email = email;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }

    private void validatedName(String name){
        if(name.isBlank()) throw new RuntimeException("Name shoud not be blank");
    }

    public void changeName(String name){
        validatedName(name);
        this.name = name;
    }

    public void login(){
        this.lastLoginAt = LocalDateTime.now();
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
