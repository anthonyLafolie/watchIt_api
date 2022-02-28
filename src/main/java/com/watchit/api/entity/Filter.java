package com.watchit.api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Filter {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "userFilter")
    private User userFilter;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private boolean checked;
}
