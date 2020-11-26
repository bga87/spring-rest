package com.jm.task.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jm.task.controllers.converters.RoleDeserializer;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_name"})
})
@JsonDeserialize(using = RoleDeserializer.class)
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = "ROLE_" + roleName.toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof Role) &&
                ((Role) obj).getRoleName().equalsIgnoreCase(getRoleName()));
    }

    @Override
    public String toString() {
        return roleName.substring(5);
    }

}