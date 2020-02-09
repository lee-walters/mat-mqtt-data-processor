package com.mclaren.challenge;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MqttDataProcessor {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MqttDataProcessor.class)
            .run(args);
    }
}
