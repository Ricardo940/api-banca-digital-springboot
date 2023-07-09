package com.banca.digital.dto;

import lombok.Data;

@Data
public class ValidationError {

    private String code;
    private String message;
}
