package com.mclaren.challenge.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mclaren.challenge.model.outbound.CarStatus;
import com.mclaren.challenge.model.outbound.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OutboundMessageTransformer {

    private ObjectMapper objectMapper;

    public OutboundMessageTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convertToMqttMessage(final CarStatus carStatus) {
        String json = "";
        try {
            json = objectMapper.writeValueAsString(carStatus);
        } catch (final IOException e) {
            log.error("Unable to convert object...");
        }
        return json;
    }

    public String convertToMqttMessage(final Event event) {
        String json = "";
        try {
            json = objectMapper.writeValueAsString(event);
        } catch (final IOException e) {
            log.error("Unable to convert object...");
        }
        return json;
    }
}
