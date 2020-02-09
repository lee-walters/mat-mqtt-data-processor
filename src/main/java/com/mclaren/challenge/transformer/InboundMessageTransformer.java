package com.mclaren.challenge.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mclaren.challenge.model.inbound.CarCoordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class InboundMessageTransformer {

    private ObjectMapper objectMapper;

    public InboundMessageTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CarCoordinate convertMqttMessage(final String message) {
        CarCoordinate carCoordinate = null;
        try {
            carCoordinate = objectMapper.readValue(message, CarCoordinate.class);
        } catch (final IOException e) {
            log.error("Unable to convert message...");
        }
        return carCoordinate;
    }
}
