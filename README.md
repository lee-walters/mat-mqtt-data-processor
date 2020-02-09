# McLaren Applied Technologies - Coding Challenge

### Prerequisites

* docker
* docker-compose
* Java

### Summary

* Spring boot application coupled with Spring Integration provides inbound and outbound channel adapters to support the Message Queueing Telemetry Transport (MQTT) protocol
* Jackson databind for (de)serializing MQTT message payloads
* Maven Jib plugin to simplify containerization of the Java application
* Lombok to remove boiler plate code
* Apache Lucene utils library to calculate distance between GPS coordinates
* Guava utils library in order to use a RateLimiter for throttling events
* Testing with Mockito and Hamcrest

### Execution Options

1. Append new service to MAT docker-compose.yml

        # Data Processor
        data_processor:
                container_name: data-processor
                image: leewalters/mclarenappliedtechnologies-data-processor:latest
                network_mode: host
                depends_on:
                        - broker

2. JAR execution
        
        # Run other services as detached (broker, webapp...)
        docker-compose up -d
        
        # Generate JAR file
        mvn clean install
        
        # Execute JAR file
        java -jar target/MATDataProcessingApp-1.0.0.jar

3. Manual docker execution

        # Pull new data-processor service
        docker pull leewalters/mclarenappliedtechnologies-data-processor:latest

        # Run other services as detached (broker, webapp...)
        docker-compose up -d
        
        # Run new data-processor service
        docker run --rm -it --network=host leewalters/mclarenappliedtechnologies-data-processor:latest
