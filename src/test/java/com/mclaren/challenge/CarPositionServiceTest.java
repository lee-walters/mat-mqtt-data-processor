package com.mclaren.challenge;

import com.mclaren.challenge.model.inbound.CarCoordinate;
import com.mclaren.challenge.model.inbound.Location;
import com.mclaren.challenge.model.internal.GridTracking;
import com.mclaren.challenge.model.outbound.CarStatus;
import com.mclaren.challenge.service.CarPositionService;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CarPositionServiceTest {

    private CarPositionService carPositionService = new CarPositionService();

    @Test
    public void testUpdateCarStatusPosition_Untracked() {
        float lat1 = 52.0699453113489f;
        float long1 = -1.0110300220549107f;

        long timestamp = System.currentTimeMillis();

        CarCoordinate carCoordinate = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(lat1)
                .longitude(long1)
                .build())
            .timestamp(timestamp)
            .build();

        GridTracking gridTracking = carPositionService.updateCarStatusPosition(carCoordinate, 30.0);
        assertThat(gridTracking.getContextCar(), is(equalTo(carCoordinate)));
        assertThat(gridTracking.getNewCarPositions().isEmpty(), is(equalTo(true)));
        assertThat(gridTracking.getOriginalCarPositions().isEmpty(), is(equalTo(true)));
    }

    @Test
    public void testOriginalAndNewPositions_UpdateCarStatusPosition_MultipleTracked() {
        CarCoordinate carIdx1Coordinate1 = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        carPositionService.updateCarStatusPosition(carIdx1Coordinate1, 30.0);

        CarCoordinate carIdx2Coordinate1 = CarCoordinate.builder()
            .carIndex(2)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        carPositionService.updateCarStatusPosition(carIdx2Coordinate1, 25.0);

        CarCoordinate carIdx1Coordinate2 = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        carPositionService.updateCarStatusPosition(carIdx1Coordinate2, 15.0);

        CarCoordinate carIdx2Coordinate2 = CarCoordinate.builder()
            .carIndex(2)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        GridTracking gridTracking = carPositionService.updateCarStatusPosition(carIdx2Coordinate2, 35.0);

        LinkedList<Integer> originalPositions = new LinkedList<>();
        originalPositions.add(1);
        originalPositions.add(2);

        LinkedList<Integer> newPositions = new LinkedList<>();
        newPositions.add(2);
        newPositions.add(1);

        assertThat(gridTracking.getOriginalCarPositions(), is(equalTo(originalPositions)));
        assertThat(gridTracking.getNewCarPositions(), is(equalTo(newPositions)));
    }

    @Test
    public void testFetchCarStatusPositions_NoTrackedCars() {
        assertThat(carPositionService.fetchCarStatusPositions().isEmpty(), is(true));
    }

    @Test
    public void testFetchCarStatusPositions() {
        CarCoordinate carIdx1Coordinate1 = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        carPositionService.updateCarStatusPosition(carIdx1Coordinate1, 30.0);

        CarCoordinate carIdx2Coordinate1 = CarCoordinate.builder()
            .carIndex(2)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        carPositionService.updateCarStatusPosition(carIdx2Coordinate1, 25.0);

        CarCoordinate carIdx1Coordinate2 = CarCoordinate.builder()
            .carIndex(1)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        carPositionService.updateCarStatusPosition(carIdx1Coordinate2, 15.0);

        CarCoordinate carIdx2Coordinate2 = CarCoordinate.builder()
            .carIndex(2)
            .location(Location.builder()
                .latitude(123f)
                .longitude(456f)
                .build())
            .timestamp(System.currentTimeMillis())
            .build();

        carPositionService.updateCarStatusPosition(carIdx2Coordinate2, 35.0);

        List<CarStatus> carPositions = carPositionService.fetchCarStatusPositions();

        assertThat(carPositions.get(0).getCarIndex(), is(equalTo(2)));
        assertThat(carPositions.get(0).getValue(), is(equalTo(1)));
        assertThat(carPositions.get(1).getCarIndex(), is(equalTo(1)));
        assertThat(carPositions.get(1).getValue(), is(equalTo(2)));
    }
}
