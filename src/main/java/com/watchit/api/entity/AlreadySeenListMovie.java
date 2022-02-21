package com.watchit.api.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@RestResource(exported = false)
public class AlreadySeenListMovie extends Movie{
    @NotNull
    @ManyToOne
    @JoinColumn(name = "userAlreadySeenList")
    private User userAlreadySeenList;
}
