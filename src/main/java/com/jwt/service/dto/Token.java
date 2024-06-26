package com.jwt.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Token {

    private String token;

    private Date expired;

}
