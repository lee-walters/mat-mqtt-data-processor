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

### Process Flow

Point of entry starts with a Spring Integration `@ServiceActivator` which polls for new messages off the given MQTT host:port address. The string message payload is then serialized into Java object `CarCoordinate` where it is then processed in the `DataProcessor`. The follow steps are then taken with each new car coordinate message:

1. A concurrent tracking map is updated, providing a retrospective reference to the previous car coordinate
2. The speed of the context car is determined via current and previous GPS coordinates
3. The positions of all cars is determined through a second (concurrency-safe) tracking map, where distance is                  accumulated for each car, sorted in a descending order. The data processor receives back a `GridTracking` object
4. Using the `GridTracking` object, the data processor can now determine if an overtake event occurred via the                  comparison of previous track positions vs current track positions.
5. All function deliverables, car statuses of speed/position and events are then published to the MQTT outbound                channel

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
