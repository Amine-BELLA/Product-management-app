package com.product.trial.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userName;
    private String firstName;
    private String email;
    private String password;
}
