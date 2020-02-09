package com.mclaren.challenge.mqtt.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CarStatusDataGateway {
    @Gateway( requestChannel = "mqttOutboundChannelCarStatus" )
    void sendToMqtt(String carStatus);
}
