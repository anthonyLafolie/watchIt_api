package com.watchit.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@RestResource(exported = false)
public class User extends BaseEntity{
    @NotBlank
	@Column(unique=true)
	private String username;

	@NotBlank
	@Column(unique=true)
	@Email
	private String email;

	@NotBlank
	private String password;
}
