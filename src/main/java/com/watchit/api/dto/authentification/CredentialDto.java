package com.watchit.api.dto.authentification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialDto {
    private String username;
    private String password;

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || ! this.getClass().equals(obj.getClass()))
            return false;
        CredentialDto other = (CredentialDto) obj;
        if (username.equals(other.getUsername()) && password.equals(other.getPassword()))
            return true;
        return false;
    }
}
