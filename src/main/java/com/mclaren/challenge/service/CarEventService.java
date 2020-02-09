package com.mclaren.challenge.service;

import com.google.common.util.concurrent.RateLimiter;
import com.mclaren.challenge.model.internal.GridTracking;
import com.mclaren.challenge.model.outbound.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static java.lang.String.format;

@Slf4j
@Service
public class CarEventService extends AbstractCarStatusService {

    private RateLimiter throttle = RateLimiter.create(1.0);

    /**
     * Construct overtake event object for the MQTT publisher
     *
     * @param gridTracking
     * @return
     */
    public Event sendCarStatusEvent(final GridTracking gridTracking) {
        //Throttle events output
        throttle.acquire();

        Event event = null;

        if (gridTracking != null) {
            Integer carIndex = gridTracking.getContextCar().getCarIndex();
            LinkedList<Integer> originalCarPositions = gridTracking.getOriginalCarPositions();
            LinkedList<Integer> newCarPositions = gridTracking.getNewCarPositions();

            if (!originalCarPositions.isEmpty() && !newCarPositions.isEmpty() && originalCarPositions.contains(carIndex) && newCarPositions.contains(carIndex)) {
                Integer previousIndexOfCar = originalCarPositions.indexOf(carIndex);
                Integer currentIndexOfCar = newCarPositions.indexOf(carIndex);

                // If the car position has changed
                if (!previousIndexOfCar.equals(currentIndexOfCar)) {
                    // Determine which car was overtaken
                    Integer overtakenCar = newCarPositions.get(previousIndexOfCar);

                    // Build overtake event
                    event = Event.builder()
                        .text(format("Car %d races ahead of Car %d in a dramatic overtake.", carIndex, overtakenCar))
                        .timestamp(System.currentTimeMillis())
                        .build();
                }
            }
        }

        return event;
    }
}
