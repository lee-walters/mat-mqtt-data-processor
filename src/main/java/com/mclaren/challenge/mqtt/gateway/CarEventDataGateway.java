package com.mclaren.challenge.mqtt.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CarEventDataGateway {
    @Gateway( requestChannel = "mqttOutboundChannelCarEvent" )
    void sendToMqtt(String carEvent);
}
