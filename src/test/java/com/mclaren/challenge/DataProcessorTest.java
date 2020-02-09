package com.mclaren.challenge;

import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.model.inbound.Location;
import com.mclaren.challenge.model.internal.CarSpeedDistance;
import com.mclaren.challenge.model.internal.GridTracking;
import com.mclaren.challenge.model.outbound.CarStatus;
import com.mclaren.challenge.mqtt.MqttPublisher;
import com.mclaren.challenge.service.CarEventService;
import com.mclaren.challenge.service.CarPositionService;
import com.mclaren.challenge.service.CarSpeedService;
import com.mclaren.challenge.service.DataProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class DataProcessorTest {

    @Mock
    private MqttPublisher mqttPublisher;

    @Mock
    private CarPositionService carPositionService;

    @Mock
    private CarSpeedService carSpeedService;

    @Mock
    private CarEventService carEventService;

    @InjectMocks
    private DataProcessor dataProcessor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNullCarCoordinateInput() {
        ConcurrentHashMap<Integer, CarCoordinate> carTracker = dataProcessor.processData(null);
        assertThat(carTracker.size(), is(equalTo(0)));
    }

    @Test
    public void testUntrackedCar() {
        Location location = Location.builder()
            .latitude(52.0699453113489f)
            .longitude(-1.0110300220549107f)
            .build();

        CarCoordinate carCoordinate = CarCoordinate.builder()
            .carIndex(1)
            .timestamp(System.currentTimeMillis())
            .location(location)
            .build();

        ConcurrentHashMap<Integer, CarCoordinate> carTracker = dataProcessor.processData(carCoordinate);
        assertThat(carTracker.size(), is(equalTo(1)));
        assertThat(carTracker.get(1), is(equalTo(carCoordinate)));
    }

    @Test
    public void testTrackedCar() {
        Location location1 = Location.builder()
            .latitude(52.0699453113489f)
            .longitude(-1.0110300220549107f)
            .build();

        CarCoordinate carCoordinate1 = CarCoordinate.builder()
            .carIndex(1)
            .timestamp(System.currentTimeMillis())
            .location(location1)
            .build();

        dataProcessor.processData(carCoordinate1);

        Location location2 = Location.builder()
            .latitude(52.069945353453453f)
            .longitude(-1.01103002342315546f)
            .build();

        CarCoordinate carCoordinate2 = CarCoordinate.builder()
            .carIndex(1)
            .timestamp(System.currentTimeMillis())
            .location(location2)
            .build();

        doNothing().when(mqttPublisher).send(ArgumentMatchers.any(CarStatus.class));
        doNothing().when(mqttPublisher).send(ArgumentMatchers.anyList());

        CarSpeedDistance carSpeedDistance = CarSpeedDistance.builder().distance(750.0).carStatus(new CarStatus()).build();
        when(carSpeedService.updateCarStatusSpeed(ArgumentMatchers.any(CarCoordinate.class), ArgumentMatchers.any(CarCoordinate.class))).thenReturn(carSpeedDistance);

        GridTracking gridTracking = GridTracking.builder().contextCar(carCoordinate2).newCarPositions(new LinkedList<>()).build();
        when(carPositionService.updateCarStatusPosition(ArgumentMatchers.any(CarCoordinate.class), anyDouble())).thenReturn(gridTracking);
        when(carPositionService.fetchCarStatusPositions()).thenReturn(Collections.emptyList());

        when(carEventService.sendCarStatusEvent(ArgumentMatchers.any(GridTracking.class))).thenReturn(null);

        ConcurrentHashMap<Integer, CarCoordinate> carTracker = dataProcessor.processData(carCoordinate2);

        verify(mqttPublisher, atLeast(1)).send(ArgumentMatchers.any(CarStatus.class));
        verify(mqttPublisher, atLeast(1)).send(ArgumentMatchers.anyList());

        assertThat(carTracker.size(), is(equalTo(1)));
    }
}
