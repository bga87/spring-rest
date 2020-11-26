package com.jm.task.controllers.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jm.task.domain.Role;

import java.io.IOException;
import java.util.Set;


public class RoleDeserializer extends StdDeserializer<Role> {

    private final Set<Role> availableRoles;

    public RoleDeserializer(Set<Role> availableRoles) {
        super((Class<?>) null);
        this.availableRoles = availableRoles;
    }

    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String roleName = node.get("roleName").asText();
        return availableRoles.stream()
                .filter(role -> role.getRoleName().equals(roleName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown role name '" + roleName + "' in request"));
    }

}