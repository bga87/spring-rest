package com.jm.task.controllers.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jm.task.domain.Job;

import java.io.IOException;

public class JobDeserializer extends StdDeserializer<Job> {

    public JobDeserializer() {
        super((Class<?>) null);
    }

    @Override
    public Job deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String jobName = node.get("name").asText();
        if (jobName.isBlank()) {
            return null;
        }
        String salary = node.get("salary").asText();
        return new Job(jobName, salary.isBlank() ? null : Integer.valueOf(salary)
        );
    }
}
