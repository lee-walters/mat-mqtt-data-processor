package com.mclaren.challenge.service;

import com.mclaren.challenge.model.internal.CarSpeedDistance;
import com.mclaren.challenge.model.internal.GridTracking;
import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.model.outbound.CarStatus;
import com.mclaren.challenge.model.outbound.Event;
import com.mclaren.challenge.mqtt.MqttPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DataProcessor {

    private ConcurrentHashMap<Integer, CarCoordinate> carTracker;

    private MqttPublisher mqttPublisher;
    private CarPositionService carPositionService;
    private CarSpeedService carSpeedService;
    private CarEventService carEventService;

    public DataProcessor(MqttPublisher mqttPublisher,
                         CarPositionService carPositionService,
                         CarSpeedService carSpeedService,
                         CarEventService carEventService) {
        this.carTracker = new ConcurrentHashMap<>();
        this.mqttPublisher = mqttPublisher;
        this.carPositionService = carPositionService;
        this.carSpeedService = carSpeedService;
        this.carEventService = carEventService;
    }

    /**
     * Process and transform car coordinate into meaningful data : car status / event
     *
     * @param currentCarLocation
     */
    public ConcurrentHashMap<Integer, CarCoordinate> processData(final CarCoordinate currentCarLocation) {
        if (currentCarLocation != null) {
            if (!carTracker.containsKey(currentCarLocation.getCarIndex())) {
                carTracker.put(currentCarLocation.getCarIndex(), currentCarLocation);
            } else {
                CarCoordinate previousCarLocation = carTracker.get(currentCarLocation.getCarIndex());
                // Calculate current speed of the car based of GPS coordinates
                CarSpeedDistance carSpeedStatus = carSpeedService.updateCarStatusSpeed(currentCarLocation, previousCarLocation);
                mqttPublisher.send(carSpeedStatus.getCarStatus());

                // Calculate the distance travelled for the car and generate position
                GridTracking gridTracking = carPositionService.updateCarStatusPosition(currentCarLocation, carSpeedStatus.getDistance());

                // Notify publisher of position statuses
                List<CarStatus> carStatuses = carPositionService.fetchCarStatusPositions();
                mqttPublisher.send(carStatuses);

                // Update car tracker with latest coordinates
                carTracker.replace(currentCarLocation.getCarIndex(), currentCarLocation);

                // Run as async background task to throttle events off the main thread
                CompletableFuture.runAsync(() -> {
                    Event overtake = carEventService.sendCarStatusEvent(gridTracking);
                    if (overtake != null) {
                        mqttPublisher.send(overtake);
                    }
                });
            }
        }

        return carTracker;
    }
}
