package com.jm.task.controllers.converters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.task.domain.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final Set<Role> availableRoles;

    public StringToRoleConverter(Set<Role> availableRoles) {
        this.availableRoles = availableRoles;
    }

    @Override
    public Role convert(String s) {
        return availableRoles.stream()
                .filter(role -> role.getRoleName().equals(s)).findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown role parameter " + s + " in request"));
    }

}