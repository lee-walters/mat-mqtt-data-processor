package com.mclaren.challenge.mqtt;

import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.service.DataProcessor;
import com.mclaren.challenge.transformer.InboundMessageTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@MessageEndpoint
public class CarCoordinatesServiceActivator {

    private InboundMessageTransformer inboundMessageTransformer;
    private DataProcessor dataProcessor;

    private static final String DUPLICATES_HEADER = "mqtt_duplicate";

    public CarCoordinatesServiceActivator(InboundMessageTransformer inboundMessageTransformer,
                                          DataProcessor dataProcessor) {
        this.inboundMessageTransformer = inboundMessageTransformer;
        this.dataProcessor = dataProcessor;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void tranformCarCoordinates(Message<?> message) {
        boolean duplicate = (boolean) message.getHeaders().get(DUPLICATES_HEADER);
        if (!duplicate) {
            CarCoordinate carCoordinate = inboundMessageTransformer.convertMqttMessage((String) message.getPayload());
            dataProcessor.processData(carCoordinate);
        }
    }
}
