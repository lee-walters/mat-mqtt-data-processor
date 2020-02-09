package com.mclaren.challenge.configuration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.UUID;

import static com.mclaren.challenge.utils.Constants.*;

@Configuration
public class MqttOutboundConfiguration {

    private static final Integer MAX_INFLIGHT = 100000;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { MQTT_HOST });
        options.setMaxInflight(MAX_INFLIGHT);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean(name = "mqttOutboundChannelCarStatus")
    public MessageChannel mqttOutboundChannelCarStatus() {
        return new DirectChannel();
    }

    @Bean(name = "mqttOutboundChannelCarEvent")
    public MessageChannel mqttOutboundChannelCarEvent() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannelCarEvent")
    public MessageHandler mqttOutboundCarEvent() {
        MqttPahoMessageHandler messageHandler =
            new MqttPahoMessageHandler(UUID.randomUUID().toString(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(OUTBOUND_TOPIC_EVENTS);
        return messageHandler;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannelCarStatus")
    public MessageHandler mqttOutboundCarStatus() {
        MqttPahoMessageHandler messageHandler =
            new MqttPahoMessageHandler(UUID.randomUUID().toString(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(OUTBOUND_TOPIC_CAR_STATUS);
        return messageHandler;
    }
}
