package com.watchit.api.dto.authentification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    private String token;
    private Long userId;

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || ! this.getClass().equals(obj.getClass()))
            return false;
        AuthDto other = (AuthDto) obj;
        if (token.equals(other.getToken()) && userId.equals(other.getUserId()))
            return true;
        return false;
    }
}
