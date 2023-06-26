package com.jwt.server.dto;

import lombok.Data;

@Data
public class ExpirationTime {

    private long day;

    private long hour;

    private long minute;

    private long second;

}
