package com.mclaren.challenge.mqtt;

import com.mclaren.challenge.model.outbound.CarStatus;
import com.mclaren.challenge.model.outbound.Event;
import com.mclaren.challenge.mqtt.gateway.CarEventDataGateway;
import com.mclaren.challenge.mqtt.gateway.CarStatusDataGateway;
import com.mclaren.challenge.transformer.OutboundMessageTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@MessageEndpoint
public class MqttPublisher {

    private OutboundMessageTransformer outboundMessageTransformer;
    private CarStatusDataGateway carStatusDataGateway;
    private CarEventDataGateway carEventDataGateway;

    public MqttPublisher(OutboundMessageTransformer outboundMessageTransformer,
                         CarStatusDataGateway carStatusDataGateway,
                         CarEventDataGateway carEventDataGateway) {
        this.outboundMessageTransformer = outboundMessageTransformer;
        this.carStatusDataGateway = carStatusDataGateway;
        this.carEventDataGateway = carEventDataGateway;
    }

    /**
     * Helper method for multiple car status objects
     *
     * @param carStatuses
     */
    public synchronized void send(final List<CarStatus> carStatuses) {
        carStatuses.forEach(this::send);
    }

    /**
     * Utilise message gateway to send MQTT payload (CarStatus)
     *
     * @param carStatus
     */
    public synchronized void send(final CarStatus carStatus) {
        log.info("MQTT SEND (CarStatus): {}", carStatus.toString());
        carStatusDataGateway.sendToMqtt(outboundMessageTransformer.convertToMqttMessage(carStatus));
    }

    /**
     * Utilise message gateway to send MQTT payload (Event)
     *
     * @param event
     */
    public synchronized void send(final Event event) {
        log.info("MQTT SEND (CarEvent): {}", event.toString());
        carEventDataGateway.sendToMqtt(outboundMessageTransformer.convertToMqttMessage(event));
    }
}
