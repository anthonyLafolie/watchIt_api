package com.watchit.api.dto.authentification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    private String token;
    private Long userId;
}
