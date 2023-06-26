package com.jwt.server.dto.authorization;

import com.jwt.server.dto.AuthType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Company extends AbstractModel  {
    @Override
    public AuthType getAccountType() {
        return AuthType.COMPANY;
    }
}