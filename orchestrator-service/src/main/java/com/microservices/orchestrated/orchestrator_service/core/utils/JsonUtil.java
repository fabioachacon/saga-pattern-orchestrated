package com.microservices.orchestrated.orchestrator_service.core.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.orchestrated.orchestrator_service.core.dto.Event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class JsonUtil {
    private final ObjectMapper mapper;

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.toString());
            return "";
        }

    }

    public Event toEvent(String json) {
        try {
            return mapper.readValue(json, Event.class);
        } catch (Exception e) {
            log.error(json, e);
            return null;
        }

    }
}
