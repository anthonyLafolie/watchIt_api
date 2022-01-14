package com.watchit.api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;

@Getter
@Setter
public class UserDto implements UserBaseDto {
    private String username;
    @Email
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || ! this.getClass().equals(obj.getClass()))
            return false;
            UserDto other = (UserDto) obj;
        if (username.equals(other.getUsername()) && email.equals(other.getEmail()) && password.equals(other.getPassword()))
            return true;
        return false;
    }
}