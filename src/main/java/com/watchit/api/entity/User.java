package com.watchit.api.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@RestResource(exported = false)
@Table(name = "_user")
public class User extends BaseEntity {
	@NotBlank
	@Column(unique = true, name = "username")
	private String username;

	@NotBlank
	@Email
	@Column(unique = true)
	private String email;

	@NotBlank
	@Column(name = "password")
	private String password;

    @OneToMany(mappedBy = "userFilter", cascade = CascadeType.ALL)
    private List<Filter> filters;
}
