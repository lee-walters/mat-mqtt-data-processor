package com.mclaren.challenge;

import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.model.inbound.Location;
import com.mclaren.challenge.model.internal.GridTracking;
import com.mclaren.challenge.model.outbound.Event;
import com.mclaren.challenge.service.CarEventService;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

public class CarEventServiceTest {

    private CarEventService carEventService = new CarEventService();

    @Test
    public void testSendCarStatusEvent_InvalidGridTrackingParam_EmptyPositions() {
        CarCoordinate carIdx1Coordinate1 = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        LinkedList<Integer> newCarPositions = new LinkedList<>();
        LinkedList<Integer> oldCarPositions = new LinkedList<>();

        GridTracking gridTracking = GridTracking.builder()
            .contextCar(carIdx1Coordinate1)
            .newCarPositions(newCarPositions)
            .originalCarPositions(oldCarPositions)
            .build();

        assertNull(carEventService.sendCarStatusEvent(gridTracking));
    }

    @Test
    public void testSendCarStatusEvent_InvalidGridTrackingParam_IdxNotContaining() {
        CarCoordinate carIdx1Coordinate1 = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        LinkedList<Integer> newCarPositions = new LinkedList<>(Arrays.asList(2, 3, 4));
        LinkedList<Integer> oldCarPositions = new LinkedList<>(Arrays.asList(2, 3, 4));

        GridTracking gridTracking = GridTracking.builder()
            .contextCar(carIdx1Coordinate1)
            .newCarPositions(newCarPositions)
            .originalCarPositions(oldCarPositions)
            .build();

        assertNull(carEventService.sendCarStatusEvent(gridTracking));
    }

    @Test
    public void testSendCarStatusEvent() {
        CarCoordinate carIdx1Coordinate1 = CarCoordinate.builder()
            .carIndex(2)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        LinkedList<Integer> newCarPositions = new LinkedList<>(Arrays.asList(1, 2, 3, 4));
        LinkedList<Integer> oldCarPositions = new LinkedList<>(Arrays.asList(2, 1, 3, 4));

        GridTracking gridTracking = GridTracking.builder()
            .contextCar(carIdx1Coordinate1)
            .newCarPositions(newCarPositions)
            .originalCarPositions(oldCarPositions)
            .build();

        Event event = carEventService.sendCarStatusEvent(gridTracking);
        assertThat(event.getText(), is("Car 2 races ahead of Car 1 in a dramatic overtake."));
    }
}
